package com.androidrider.crud_operation_sqlite.Database


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.androidrider.crud_operation_sqlite.Model.ContactModel


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME,null, DB_VERSION) {

    companion object{

        private val DB_NAME = "task"
        private val DB_VERSION = 1

        private val TABLE_NAME = "contactTable"
        private val ID = "id"
        private val CONTACT_NAME = "contactName"
        private val CONTACT_NUMBER = "contactNumber"

    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY,$CONTACT_NAME TEXT,$CONTACT_NUMBER TEXT);"
        p0?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        p0?.execSQL(DROP_TABLE)
        onCreate(p0)
    }

    // get All Task
    @SuppressLint("Range")
    fun getAllUser(): ArrayList<ContactModel>{
        val taskList = ArrayList<ContactModel>()
        val db = writableDatabase
        val selectQuery = "SELECT *FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery,null)
        if (cursor !=null){
            if (cursor.moveToFirst()){
                do {
                    val tasks = ContactModel()
                    tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    tasks.contactName = cursor.getString(cursor.getColumnIndex(CONTACT_NAME))
                    tasks.contactNumber = cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER))
                    taskList.add(tasks)
                }while (cursor.moveToNext())
            }
        }
        cursor.close()
        return taskList
    }

// insert data

    fun addUser(task: ContactModel): Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(CONTACT_NAME,task.contactName)
        values.put(CONTACT_NUMBER,task.contactNumber)
        val _success = db.insert(TABLE_NAME,null, values)
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    // select the data of particular id
    @SuppressLint("Range")
    fun getUser(_id: Int) : ContactModel{
        val tasks = ContactModel()
        val db = writableDatabase
        val selectQuery = "SELECT *FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery,null)

        cursor?.moveToFirst()
        tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
        tasks.contactName = cursor.getString(cursor.getColumnIndex(CONTACT_NAME))
        tasks.contactNumber = cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER))
        cursor.close()
        return tasks
    }

    // delete data
    fun deleteUser(_id: Int) : Boolean{
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, "$ID=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    // update data
    fun updateUser(task: ContactModel) : Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(CONTACT_NAME,task.contactName)
        values.put(CONTACT_NUMBER,task.contactNumber)
        val _success = db.update(TABLE_NAME, values, "$ID=?", arrayOf(task.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

}