package com.mianasad.chatsapp.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mianasad.chatsapp.Adapter.UserAdapter
import com.mianasad.chatsapp.Model.User
import com.mianasad.chatsapp.R
import com.mianasad.chatsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var binding : ActivityMainBinding? = null
    var database: FirebaseDatabase? = null
    var users   : ArrayList<User>? = null
    var usersAdapter: UserAdapter? = null
    var dialog  : ProgressDialog? = null
    var user: User? = null
    var mtoolbar: Toolbar? = null
    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        dialog = ProgressDialog(this@MainActivity)
        dialog!!.setMessage("Uploading Image...")
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        users = ArrayList<User>()
        usersAdapter = UserAdapter(this@MainActivity,users!!)
        val layoutManager = GridLayoutManager(this@MainActivity,1)
        mtoolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mtoolbar)
        val drawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_more_vert_24)
        mtoolbar!!.overflowIcon = drawable
        binding!!.mRec.layoutManager = layoutManager
        database!!.reference.child("users")
            .child(FirebaseAuth.getInstance().uid!!)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    user = snapshot.getValue(User::class.java)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        binding!!.mRec.adapter = usersAdapter
        database!!.reference.child("users").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                users!!.clear()
                for (snapshot1 in snapshot.children){
                    val user:User? = snapshot1.getValue(User::class.java)
                    if (!user!!.uid.equals(FirebaseAuth.getInstance().uid)) users!!.add(user)
                }
                usersAdapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.settings -> Toast.makeText(
                applicationContext,
                "Setting is clicked",
                Toast.LENGTH_SHORT
            ).show()
            R.id.log_out -> {
                auth!!.signOut()
                val intent = Intent(this,VerificationActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }


    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("presence")
            .child(currentId!!).setValue("Online")
    }

    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("presence")
            .child(currentId!!).setValue("offline")
    }
}