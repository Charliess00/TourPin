package com.example.tourpin2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tourpin2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Search())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.search -> replaceFragment(Search())
                R.id.order -> replaceFragment(Order())
                R.id.profile -> replaceFragment(Profile())
                R.id.tour -> replaceFragment(Bookings())

                else -> {
                }
            }

            true

        }
    }


    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()

    }
}