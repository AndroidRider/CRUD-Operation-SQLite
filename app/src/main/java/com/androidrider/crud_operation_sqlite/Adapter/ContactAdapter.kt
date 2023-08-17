package com.androidrider.crud_operation_sqlite.Adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.androidrider.crud_operation_sqlite.Database.DatabaseHelper
import com.androidrider.crud_operation_sqlite.Model.ContactModel
import com.androidrider.crud_operation_sqlite.R


class ContactAdapter(val c:Context, val contactList:ArrayList<ContactModel>, val dbHandler: DatabaseHelper):
    RecyclerView.Adapter<ContactAdapter.UserViewHolder>()
{
    inner class UserViewHolder( v : View):RecyclerView.ViewHolder(v){
        var name:TextView
        var mbNum:TextView
        var mMenus:ImageView

        init {
            name = v.findViewById(R.id.mTitle)
            mbNum = v.findViewById(R.id.mSubTitle)
            mMenus = v.findViewById(R.id.mMenus)

            mMenus.setOnClickListener {
                popupMenus(it)
            }
        }

        private fun popupMenus(v:View) {

            val position = contactList[adapterPosition]
            val popupMenus = PopupMenu(c,v)
            popupMenus.inflate(R.menu.show_menu)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.editText->{

                        val v = LayoutInflater.from(c).inflate(R.layout.add_item,null)

                        val name = v.findViewById<EditText>(R.id.userName)
                        val number = v.findViewById<EditText>(R.id.userNo)

                        // Retrieve the data for the current position
                        val currentItem = contactList[adapterPosition]
                        name.setText(currentItem.contactName)
                        number.setText(currentItem.contactNumber)

                        AlertDialog.Builder(c)
                            .setView(v)
                            .setPositiveButton("Ok"){ dialog,_->

                                position.id
                                position.contactName = name.text.toString()
                                position.contactNumber = number.text.toString()

                                val success = dbHandler.updateUser(position)
                                if (success) {
                                    Toast.makeText(c, "Data Successfully Inserted", Toast.LENGTH_SHORT).show()
                                    dialog.dismiss()
                                }
                                notifyDataSetChanged()
                            }
                            .setNegativeButton("Cancel"){
                                    dialog,_->
                                dialog.dismiss()
                            }
                            .create()
                            .show()

                        true
                    }
                    R.id.delete->{
                        /**set delete*/
                        AlertDialog.Builder(c)
                            .setTitle("Delete")
                            .setIcon(R.drawable.baseline_warning)
                            .setMessage("Are you sure delete this Information")
                            .setPositiveButton("Yes"){dialog,_->

                                val success = dbHandler.deleteUser(position.id) // Pass the id to deleteUser
                                if (success) {
                                    Toast.makeText(c, "Data Successfully Deleted", Toast.LENGTH_SHORT).show()
                                    dialog.dismiss()

                                    // Remove the deleted item from the list
                                    contactList.removeAt(adapterPosition)
                                    notifyDataSetChanged()
                                }
                            }
                            .setNegativeButton("No"){dialog,_->

                                dialog.dismiss()
                            }
                            .create()
                            .show()

                        true
                    }
                    else-> true
                }
            }
            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                .invoke(menu,true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v  = inflater.inflate(R.layout.list_item,parent,false)
        return UserViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val newList = contactList[position]
        holder.name.text = newList.contactName
        holder.mbNum.text = newList.contactNumber
    }

    override fun getItemCount(): Int {
        return  contactList.size
    }
}