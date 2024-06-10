package com.example.tourpin2

import User
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tourpin2.databinding.ActivityRegistrationBinding
import com.example.tourpin2.dialog.LoadingDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class Registration : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var name: EditText
    private lateinit var surname: EditText
    private lateinit var phone: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var loading: LoadingDialog
    private lateinit var userId: String


    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initual()

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, Authentication::class.java)
            startActivity(intent)
            finish()
        }


        binding.button.setOnClickListener {
            if (validateFields(surname, name, phone, email, password, confirmPassword)) {
                sendDataToFirebase()
            }
        }
    }


    private fun validateFields(vararg fields: EditText): Boolean {
        var isValid = true

        for (field in fields) {
            if (field.text!!.isEmpty()) {
                field.error = "Поле не должно быть пустым"
                isValid = false
            }
        }


        if (phone.text!!.length != 10) {
            phone.error = "Введите корректный номер телефона"
            isValid = false
        }


        if (!isEmailValid(email.text.toString())) {
            email.error = "Введите корректный email"
            isValid = false
        }

        if (password.text!!.length < 6) {
            password.error = "Пароль должен содержать минимум 6 символов"
            isValid = false
        }

        if (password.text.toString().trim() != confirmPassword.text.toString()
                .trim() && confirmPassword.text.toString().isNotEmpty() && password.text.toString()
                .isNotEmpty()
        ) {
            password.error = "Поля паролей не совпадают"
            isValid = false
        }

        return isValid
    }

    private fun sendDataToFirebase() {

        loading.start()

        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val userAuth = auth.currentUser
                    userId = userAuth?.uid.toString()

                    val databaseReference = FirebaseDatabase.getInstance().getReference("User")
                        .child(userAuth?.uid.toString())

                    val user = User(
                        name = name.text.toString(),
                        surname = surname.text.toString(),
                        phone = "+7${phone.text}",
                        email = email.text.toString(),
                    )

                    databaseReference.setValue(user).addOnSuccessListener {
                            goToLogin()
                        }.addOnFailureListener { exception ->
                            Toast.makeText(
                                this,
                                "Ошибка при регистрации: ${exception.message}",
                                Toast.LENGTH_LONG
                            ).show()
                            loading.error()
                        }
                } else {
                    Toast.makeText(
                        this, "Такой пользователь уже зарегистрирован", Toast.LENGTH_SHORT
                    ).show()
                    loading.error()
                }
            }
    }

    private fun initual() {
        auth = FirebaseAuth.getInstance()
        surname = binding.editTextSurname
        name = binding.editTextName
        phone = binding.editTextPhone
        email = binding.editTextEmail
        password = binding.editTextPass
        confirmPassword = binding.editTextPassSec
        loading = LoadingDialog(this)
    }

    private fun goToLogin() {
        val intent = Intent(this, Authentication::class.java)
        val handler = Handler(Looper.getMainLooper())

        loading.end()
        handler.postDelayed({
            startActivity(intent)
            finish()
        }, 3000)
    }
}
