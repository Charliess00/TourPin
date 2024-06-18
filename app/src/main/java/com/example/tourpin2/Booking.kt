package com.example.tourpin2

import User
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tourpin2.`class`.BookingTour
import com.example.tourpin2.`class`.Proposals
import com.example.tourpin2.databinding.ActivityBookingBinding
import com.example.tourpin2.dialog.LoadingDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Booking : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding
    private lateinit var loading: LoadingDialog

    private var auth = FirebaseAuth.getInstance()
    private val cUser = auth.currentUser
    private val dbRef = FirebaseDatabase.getInstance().getReference("User")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loading = LoadingDialog(this)
        loading.start()

        val tourKey = intent.getStringExtra("tourKey").toString()

        dbRef.child(cUser?.uid.toString()).addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                // Получаем данные пользователя
                val user = snapshot.getValue(User::class.java)

                if (user != null) {
                    binding.name.text = "${user.surname} ${user.name}"
                    binding.email.text = user.email

                    binding.phone.text = formatPhoneNumber(user.phone)

                }

                loading.dismiss()
            }

            private fun formatPhoneNumber(number: String): String {
                val cleanedPhone = number.replace("+7", "").trimStart('-')
                val pattern = Regex("(\\d{3})(\\d{3})(\\d{2})(\\d{2})")

                return pattern.replace(cleanedPhone, "+7 ($1) $2-$3-$4")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("User", "Failed to read value.", error.toException())
                loading.error()
            }
        })

        binding.button.setOnClickListener {
            loading.start()
            booking(tourKey)
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, Tour::class.java)
            intent.putExtra("proposalKey", tourKey)
            startActivity(intent)
        }
    }

    private fun booking(key: String) {
        val proposalRef = FirebaseDatabase.getInstance().getReference("Proposal/$key")
        val bookingRef = FirebaseDatabase.getInstance().getReference("Booking/$key")

        proposalRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val proposalData = snapshot.getValue(Proposals::class.java)
                if (proposalData != null) {
                    val bookingData = BookingTour(
                        hotel_name = proposalData.hotel_name,
                        hotel_desc = proposalData.hotel_desc,
                        hotel_img = proposalData.hotel_img,
                        country = proposalData.country,
                        city_tour = proposalData.city_tour,
                        data_start = proposalData.data_start,
                        data_end = proposalData.data_end,
                        night = proposalData.night,
                        person = proposalData.person,
                        city = proposalData.city,
                        price = proposalData.price,
                        uid = cUser?.uid ?: "", // Используйте UID текущего пользователя
                        agent_ID = proposalData.uid // Используйте UID предложения как agent_ID
                    )

                    bookingRef.setValue(bookingData).addOnSuccessListener {
                        val orderID = proposalData.order_ID
                        val orderRef = FirebaseDatabase.getInstance().getReference("Order/$orderID")
                        orderRef.removeValue().addOnSuccessListener {
                            proposalRef.removeValue()
                            goToMain()
                        }.addOnFailureListener { exception ->
                            loading.error()
                            Toast.makeText(
                                this@Booking,
                                "Ошибка при переносе записи: ${exception.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    loading.error()
                    Toast.makeText(
                        this@Booking,
                        "Запись не найдена для удаления",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            private fun goToMain() {
                val intent = Intent(this@Booking, MainActivity::class.java)
                val handler = Handler(Looper.getMainLooper())

                loading.end()
                handler.postDelayed({
                    startActivity(intent)
                    finish()
                }, 3000)
            }

            override fun onCancelled(error: DatabaseError) {
                loading.error()
                Toast.makeText(
                    this@Booking,
                    "Ошибка при чтении данных: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }


}