package com.example.newrecycler




import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


/**
 * Let's start by creating our database CRUD helper class
 * based on the SQLiteHelper.
 * This will run once on startup and will create our sql table where we will store our data
 */

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 1)

{




    //On create method
    //This will run on startup and will create our sql table where we will carry out our crud functionality


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NAME (ID INTEGER PRIMARY KEY " +
                "AUTOINCREMENT, CARID TEXT, CARNAMETYPE TEXT, MODIFICATIONSMADE TEXT, BHPAMOUNT TEXT," +
                " TORQUETOTAL TEXT, COSTOFMODIFICATIONS TEXT, ACCELERATION TEXT, IMAGE BLOB)")



    }


    //The Update Table Method
    //This will be called when the data in the table needs to be updated
    //####################################################################################################################################//
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)

    }


//####################################################################################################################################//

    //Insert Method to add values from text boxes into our table that has been created
    //Id value will be left out as we have the value to auto increment

    fun insertData(carType: String, modificationsMade: String, bhpAmount: String, torqueTotal: String,
                   CostOfModifications: String, acceleration: String){

        val cv = ContentValues()

        val db = this.writableDatabase
        //  Each Var will be assigined a rol
        val contentValues = ContentValues()


        contentValues.put(COL_1, carType)
        contentValues.put(COL_2, modificationsMade)
        contentValues.put(COL_3, bhpAmount)
        contentValues.put(COL_4, torqueTotal)
        contentValues.put(COL_5, CostOfModifications)
        contentValues.put(COL_6, acceleration)

        //adding image to sql needs to be saved as byte array





        db.insert(TABLE_NAME, null, contentValues)

        if (carType == "") {
            print("you left the field empty")
        }


    }


    //####################################################################################################################################//
    //Grab Id so we can delete that value with the same id

    fun deleteData(id : String) : Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME,"ID = ?", arrayOf(id))
    }

    //####################################################################################################################################//
    //Update Method
    //Using array of id which will search from our array of ids and select the specific value we wish to update

    fun updateData(id: String, carType: String, modificationsMade: String, bhpAmount: String, torqueTotal: String,
                   CostOfModifications: String, acceleration: String):
            Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_0, id)
        contentValues.put(COL_1, carType)
        contentValues.put(COL_2, modificationsMade)
        contentValues.put(COL_3, bhpAmount)
        contentValues.put(COL_4, torqueTotal)
        contentValues.put(COL_5, CostOfModifications)
        contentValues.put(COL_6, acceleration)


        db.update(TABLE_NAME, contentValues, "ID = ?", arrayOf(id))
        return true
    }


    fun getdata(): ByteArray {
        val db = writableDatabase
        val res = db.rawQuery("select * from " + TABLE_NAME, null)

        if (res.moveToFirst()) {
            do {
                return res.getBlob(0)
            } while (res.moveToNext())
        }
        return byteArrayOf()
    }


    //####################################################################################################################################//
    //ON Cursor Click
    /** Cursors
    Cursors are what contain the result set of a query made against a database in Android.
    The Cursor class has an API that allows an app to read (in a type-safe manner) the columns
    that were returned from the query as well as iterate over the rows of the result set.

    https://stackoverflow.com/questions/39066267/kotlin-on-android-map-a-cursor-to-a-list
     */

    val allData : Cursor
        get() {
            val db = this.writableDatabase
            val res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
            return res
        }

    //####################################################################################################################################//
    //Companion Object
    /**
     * https://kotlinlang.org/docs/tutorials/kotlin-for-py/objects-and-companion-objects.html
     *
     * The companion object is a singleton, and its members can be accessed directly via the name of the containing
     * class (although you can also insert the name of the companion object if you want to be explicit about accessing
     * the companion object)
     */

    companion object {
        val DATABASE_NAME = "carModificationsnew1211111.db"
        val TABLE_NAME = "car_Database222"
        val COL_0 = "CARID"
        val COL_1 = "CARNAMETYPE"
        val COL_2 = "MODIFICATIONSMADE"
        val COL_3 = "BHPAMOUNT"
        val COL_4 = "TORQUETOTAL"
        val COL_5 = "COSTOFMODIFICATIONS"
        val COL_6 = "ACCELERATION"




    }


}