package com.example.tourpin2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tourpin2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    public lateinit var searchFragment: Search // Измените модификатор доступа на public

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Search()) // Инициализируйте SearchFragment и сохраните ссылку в глобальной переменной

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){

                R.id.search -> replaceFragment(Search())
                R.id.order -> replaceFragment(Order())
                R.id.chat -> replaceFragment(Chat())
                R.id.profile -> replaceFragment(Profile())

                else ->{

                }

            }

            true

        }
    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()

        // Обновите ссылку на SearchFragment каждый раз, когда он заменяется
        if (fragment is Search) {
            searchFragment = fragment
        }
    }
}
