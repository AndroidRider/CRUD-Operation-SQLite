package com.androidrider.crud_operation_sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrider.crud_operation_sqlite.Adapter.ContactAdapter
import com.androidrider.crud_operation_sqlite.Database.DatabaseHelper
import com.androidrider.crud_operation_sqlite.Model.ContactModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var btn_AddData:FloatingActionButton
    private lateinit var recView:RecyclerView
    private lateinit var contactList:ArrayList<ContactModel>//taskList
    private lateinit var contactAdapter:ContactAdapter
    var dbHandler : DatabaseHelper ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_AddData = findViewById(R.id.btnAddData) /**set find Id*/
        recView = findViewById(R.id.mRecycler)

        /**set List*/
        contactList = ArrayList()
        dbHandler = DatabaseHelper(this)
        contactList = dbHandler!!.getAllUser()//Loading Database Data
        contactAdapter = ContactAdapter(this,contactList, dbHandler!!)/**set Adapter*/
        recView.layoutManager = LinearLayoutManager(this)  /**setRecycler view Adapter*/
        recView.adapter = contactAdapter

        /**set Dialog*/
        btn_AddData.setOnClickListener {addInformation() }

    }

    private fun addInformation() {
        var success = false
        val tasks = ContactModel()

        val inflter = LayoutInflater.from(this)
        val v = inflter.inflate(R.layout.dialog_add_item,null)
        /**set view*/
        val contactName = v.findViewById<EditText>(R.id.userName)
        val contactNumber = v.findViewById<EditText>(R.id.userNo)

        val addDialog = AlertDialog.Builder(this)
        addDialog.setView(v)
        addDialog.setPositiveButton("Ok"){ dialog,_->

            // insert Data
            tasks.contactName = contactName.text.toString()
            tasks.contactNumber = contactNumber.text.toString()
            success = dbHandler?.addUser(tasks) as Boolean
            if (success) {
                Toast.makeText(this, "Data Successfully Inserted", Toast.LENGTH_SHORT).show()

                // Update userList with new data from the database
                contactList.clear()
                contactList.addAll(dbHandler!!.getAllUser())

                contactAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        }
        addDialog.setNegativeButton("Cancel"){ dialog,_->

            dialog.dismiss()
            Toast.makeText(this,"Canceled",Toast.LENGTH_SHORT).show()
        }
        addDialog.create()
        addDialog.show()
    }
    /**ok now run this */

}
