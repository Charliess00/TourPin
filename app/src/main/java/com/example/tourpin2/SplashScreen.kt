package com.example.tourpin2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cuser = FirebaseAuth.getInstance().currentUser
        if (cuser == null){
            val tologin = Intent(this, Authentication::class.java)
            startActivity(tologin)
            finish()
        } else{
            if (cuser != null){
                val tohome = Intent(this, MainActivity::class.java)
                // start your next activity
                startActivity(tohome)
                finish()
            }
        }
    }
}