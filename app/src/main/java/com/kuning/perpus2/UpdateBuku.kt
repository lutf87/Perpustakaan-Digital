package com.kuning.perpus2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_update_buku.*
import kotlinx.android.synthetic.main.list_buku.*

class UpdateBuku : AppCompatActivity() {

    private var db: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var Judul: String? = null
    private var Author: String? = null
    private var Tahun: String? = null
    private var Harga: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_buku)
        supportActionBar!!.title = "Update Data"

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        data
        appCompatButtonUpdate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Judul = judul_baru.getText().toString()
                Author = author_baru.getText().toString()
                Tahun = tahun_baru.getText().toString()
                Harga = harga_baru.getText().toString()

                if (isEmpty(Judul!!) || isEmpty(Author!!) || isEmpty(Tahun!!) || isEmpty(Harga!!)) {
                    Toast.makeText(this@UpdateBuku, "Data Tidak Boleh Null", Toast.LENGTH_SHORT).show()
                } else {
                    val setBooks = Books()
                    setBooks.judul = judul_baru.getText().toString()
                    setBooks.author = author_baru.getText().toString()
                    setBooks.tahun = tahun_baru.getText().toString()
                    setBooks.harga = harga_baru.getText().toString()
                    upBook(setBooks)
                }
            }
        })
    }

    private val data: Unit
    private get() {
        val getJudul = intent.extras!!.getString("judulBuku")
        val getAuthor = intent.extras!!.getString("authorBuku")
        val getTahun = intent.extras!!.getString("tahunBuku")
        val getHarga = intent.extras!!.getString("hargaBuku")


        judul_baru!!.setText(getJudul)
        author_baru!!.setText(getAuthor)
        tahun_baru!!.setText(getTahun)
        harga_baru!!.setText(getHarga)
    }

    private fun upBook(books: Books) {
        val userID = auth!!.uid
        val getKey = intent.extras!!.getString("getPrimaryKey")
        db!!.child("Admin")
            .child(userID!!)
            .child("book")
            .child(getKey!!)
            .setValue(books)
            .addOnSuccessListener {
                judul_baru!!.setText("")
                author_baru!!.setText("")
                tahun_baru!!.setText("")
                harga_baru!!.setText("")
                Toast.makeText(this@UpdateBuku, "Data Berhasil diubah",
                Toast.LENGTH_SHORT).show()
                finish()
            }


    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

}