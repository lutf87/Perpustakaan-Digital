package com.kuning.perpus2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Adapter.dataListener {
    private var recyclerView:RecyclerView? = null
    private var adapter:RecyclerView.Adapter<*>? = null
    private var layoutManager:RecyclerView.LayoutManager? = null

    val database = FirebaseDatabase.getInstance()
    private var list_book = ArrayList<Books>()
    private var auth: FirebaseAuth? = null

    private lateinit var button: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.title = "List Buku"


        recyclerView = findViewById(R.id.datalist)
        auth = FirebaseAuth.getInstance()
        MyRecyclerView()
        GetData()

        btn_add.setOnClickListener(this)
        btn_logout.setOnClickListener(this)
    }

    private fun MyRecyclerView() {
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
    }

    private fun GetData() {
        Toast.makeText(applicationContext, "Please wait a sec....", Toast.LENGTH_SHORT).show()
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference()
        getReference.child("Admin").child(getUserID).child("book")
            .addValueEventListener(object: ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    if(datasnapshot.exists()) {
                        list_book.clear()
                        for (snapshot in datasnapshot.children) {
                            val book = snapshot.getValue(Books::class.java)
                            book?.key = snapshot.key
                            list_book.add(book!!)
                        }
                        adapter = Adapter(list_book, this@MainActivity)
                        recyclerView?.adapter = adapter
                        (adapter as Adapter).notifyDataSetChanged()
                        Toast.makeText(applicationContext, "Data Berhasil Dimuat", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(applicationContext, "Data Gagal Dimuat", Toast.LENGTH_SHORT).show()
                    Log.e("MainActivity", databaseError.details + "" +
                    databaseError.message)
                }
            })
    }

    override fun onDeleteData(books: Books?, position: Int) {
        val getUID: String = auth?.getCurrentUser()?.getUid().toString()
        val getRef = database.getReference()
        if (getRef != null) {
            getRef.child("Admin")
                .child(getUID)
                .child("book")
                .child(books?.key.toString())
                .removeValue()
                .addOnSuccessListener {

                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this@MainActivity, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                } 
        } else {
            Toast.makeText(this@MainActivity, "Ref tidak ada", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View) {

        button = findViewById(R.id.btn_logout)

        when(v.getId()) {
            R.id.btn_add -> {
                val intent = Intent(applicationContext, AddBuku::class.java)
                startActivity(intent)
                finish()
            }

            R.id.btn_logout -> {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(p0: Task<Void>) {
                            Toast.makeText(this@MainActivity, "Logout Berhasil",
                                Toast.LENGTH_SHORT).show()
                            intent = Intent(applicationContext, Login::class.java)
                            startActivity(intent)
                            finish()
                        }
                    })
            }
        }
    }

}