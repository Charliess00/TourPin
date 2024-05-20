package com.example.tourpin2;

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tourpin2.databinding.ActivityRegistrationBinding
import android.util.Patterns
import androidx.core.content.ContextCompat


class Registration : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            goToLogin()
        }

        // Добавляем слушатель для кнопки отправки формы
        binding.button.setOnClickListener {
            if (validateFields()) {
                goToLogin()
            }
        }
    }

    private fun validateFields(): Boolean {
        // Предполагаем, что у вас есть переменная для хранения состояния валидации
        var isValid = true

        val pass = binding.editTextPass.text
        val passSec = binding.editTextPassSec.text

        // Проверяем каждое поле
        if (binding.editTextSurname.text!!.isEmpty()) {
            binding.editTextSurname.error = "Введите фамилию"
            isValid = false
        }
        if (binding.editTextName.text!!.isEmpty()) {
            binding.editTextName.error = "Введите имя"
            isValid = false
        }
        if (binding.editTextPhone.text!!.isEmpty()) {
            binding.editTextPhone.error = "Введите номер телефона"
            isValid = false
        }
        if (binding.editTextPhone.text!!.length!= 10) {
            binding.editTextPhone.error = "Введите корректный номер телефона"
            isValid = false
        }
        if (binding.editTextEmail.text!!.isEmpty()) {
            binding.editTextEmail.error = "Введите email"
            isValid = false
        }
        if (!isEmailValid(binding.editTextEmail.text.toString())) {
            binding.editTextEmail.error = "Введите корректный email"
            isValid = false
        }
        if (passSec!!.isEmpty()) {
            binding.editTextPassSec.error = "Повторите пароль"
            isValid = false
        }
        if (pass!!.isEmpty()) {
            binding.editTextPass.error = "Введите пароль"
            isValid = false
        }
        if (pass.length < 6) { // Проверка на минимальную длину пароля
            binding.editTextPass.error = "Пароль должен содержать минимум 6 символов"
            isValid = false
        }
        if (pass.trim() != passSec.trim() && passSec.isNotEmpty() && pass.isNotEmpty()) {
            Log.d("pass", "Value of pass: $pass")
            Log.d("pass", "Value of passSec: $passSec")
            binding.editTextPass.error = "Поля паролей не совпадают"
            isValid = false
        }
        // Изменяем фон для полей, которые не прошли валидацию

        return isValid
    }



    private fun goToLogin() {
        val intent = Intent(this, Authentication::class.java)
        startActivity(intent)
        finish()
        finish()
    }
}
