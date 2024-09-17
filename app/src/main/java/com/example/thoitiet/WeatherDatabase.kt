package com.example.thoitiet

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class WeatherDatabase(context: Context, ) : SQLiteOpenHelper ( context , DATABASE_NAME , null , DATABASE_VERSION) {
    companion object{


        private const val DATABASE_NAME = "weather.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "weather"
        private const val COLUMN_ID = "id"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_NHIETDO = "nhietdo"
        private const val COLUMN_DOAM = "doam"
        private const val COLUMN_NANG = "nang"


    }


    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY  AUTOINCREMENT ," +
                " $COLUMN_DATE TEXT , $COLUMN_NHIETDO TEXT , $COLUMN_DOAM TEXT , $COLUMN_NANG  INTEGER )".trimIndent()

        db?.execSQL(createTable)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db?.execSQL("DROP TABLE IF  EXISTS $TABLE_NAME")
        onCreate(db)


    }
    fun insertWeather ( weatherData: WeatherData) : Long {

        val db = writableDatabase
        val values = ContentValues().apply {

            put(COLUMN_DATE , weatherData.date)
            put(COLUMN_NHIETDO , weatherData.nhietdo)
            put(COLUMN_DOAM , weatherData.doam)
            put(COLUMN_NANG , if (weatherData.nang) 1 else 0)

        }
        return  db.insert(TABLE_NAME , null , values)
    }

    fun updateWeather (weatherData: WeatherData) : Int{

        val db = writableDatabase
        val values = ContentValues() . apply {
            put(COLUMN_DATE , weatherData.date)
            put(COLUMN_NHIETDO , weatherData.nhietdo)
            put(COLUMN_DOAM , weatherData.doam)
            put(COLUMN_NANG , if (weatherData.nang) 1 else 0)


        }
        return db.update( TABLE_NAME, values , "$COLUMN_ID = ?", arrayOf(weatherData.id.toString()))


    }
     fun GetAll () : List<WeatherData>{

         val weatherList = mutableListOf<WeatherData>()
         val db = readableDatabase
         val selectQuery  = "SELECT * FROM $TABLE_NAME"
         val cursor = db.rawQuery(selectQuery, null)


         cursor.use {
             while (it.moveToNext()){
                 val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                 val date = it.getString(it.getColumnIndexOrThrow(COLUMN_DATE))
                 val nhietdo = it.getString(it.getColumnIndexOrThrow(COLUMN_NHIETDO))
                 val doam = it.getString(it.getColumnIndexOrThrow(COLUMN_DOAM))
                 val nang = it.getInt(it.getColumnIndexOrThrow(COLUMN_NANG)) == 1
                 weatherList.add(WeatherData(id, date, nhietdo , doam , nang))
             }
         }
         return weatherList

     }




}