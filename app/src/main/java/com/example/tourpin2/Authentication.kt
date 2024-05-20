package com.example.tourpin2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tourpin2.databinding.AuthenticationBinding


class Authentication : AppCompatActivity() {

    private lateinit var binding: AuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonReg.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }

        binding.buttonLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            finish()
        }
    }
}
