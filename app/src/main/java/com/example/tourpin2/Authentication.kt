package com.example.tourpin2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tourpin2.databinding.AuthenticationBinding
import com.example.tourpin2.dialog.LoadingDialog
import com.google.firebase.auth.FirebaseAuth


class Authentication : AppCompatActivity() {

    private lateinit var binding: AuthenticationBinding
    var auth = FirebaseAuth.getInstance()
    private var loading = LoadingDialog(this)
    private val cUser = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        loading.start()
        if (cUser != null) {
            goToMain()
        } else {
            loading.dismiss()
        }

        binding.buttonReg.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPass.text.toString()

            if (email.isEmpty()) {
                binding.editTextEmail.error = "Поле не должно быть пустым"
            }

            if (password.isEmpty()) {
                binding.editTextPass.error = "Поле не должно быть пустым"
            }

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loading.start()
                signInWithEmailAndPassword(email, password)
            }
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    goToMain()
                }
            } else {
                Toast.makeText(this, "Неверный email или пароль!", Toast.LENGTH_SHORT).show()
                binding.editTextEmail.error = "Неверный email или пароль!"
                binding.editTextPass.error = "Неверный email или пароль!"
                loading.dismiss()
            }
        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)

        loading.dismiss()
        startActivity(intent)
        finish()

    }
}
