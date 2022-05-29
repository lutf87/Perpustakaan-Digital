package com.kuning.perpus2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity(), View.OnClickListener {

    private var auth: FirebaseAuth? = null
    private val RC_SIGN_IN = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.title = "Login"

        progress.visibility = View.GONE
        btn_login.setOnClickListener(this)
        auth = FirebaseAuth.getInstance()

        if (auth!!.currentUser == null) {

        } else {
            intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
                intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Login Batal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(v: View?) {
        val provider = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(provider)
                .build(), RC_SIGN_IN
        )
    }
}