package com.kuning.perpus2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kuning.perpus2.databinding.ActivityAddBukuBinding
import kotlinx.android.synthetic.main.activity_add_buku.*
import kotlinx.android.synthetic.main.list_buku.*

class AddBuku : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddBukuBinding

    private var auth: FirebaseAuth? = null
    private val RC_SIGN_IN = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = "Tambah Buku"

        appCompatButtonAdd.setOnClickListener(this)
        auth = FirebaseAuth.getInstance()


    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    override fun onClick(v: View) {
        when (v.getId()) {
            R.id.appCompatButtonAdd -> {
                val getUID = auth!!.currentUser!!.uid
                val db = FirebaseDatabase.getInstance()

                val getJudul: String = textInputEditTextJudul.getText().toString()
                val getAuthor: String = textInputEditTextAuthor.getText().toString()
                val getTahun: String = textInputEditTextTahun.getText().toString()
                val getHarga: String = textInputEditTextHarga.getText().toString()

                val getRef: DatabaseReference
                getRef = db.reference

                if (isEmpty(getJudul!!) || isEmpty(getAuthor!!) || isEmpty(getTahun!!) || isEmpty(getHarga!!)) {
                    Toast.makeText(this@AddBuku, "Data TIdak boleh null", Toast.LENGTH_SHORT).show()
                } else {
                    getRef.child("Admin").child(getUID).child("book").push()
                        .setValue(Books(getJudul, getAuthor, getTahun, getHarga))
                        .addOnCompleteListener(this) {
                            textInputEditTextJudul.setText("")
                            textInputEditTextAuthor.setText("")
                            textInputEditTextTahun.setText("")
                            textInputEditTextHarga.setText("")
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                            Toast.makeText(this@AddBuku, "Data Tersimpan", Toast.LENGTH_SHORT).show()

                        }
                }
            }
        }
    }
}