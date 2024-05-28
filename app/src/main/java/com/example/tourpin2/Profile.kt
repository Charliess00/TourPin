package com.example.tourpin2

import User
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tourpin2.dialog.LoadingDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Profile : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var auth = FirebaseAuth.getInstance()
    private val cUser = auth.currentUser
    private val dbRef = FirebaseDatabase.getInstance().getReference("User")

    private lateinit var loading: LoadingDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading = LoadingDialog(requireActivity())
        loading.start()

        dbRef.child(cUser?.uid.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Получаем данные пользователя
                val user = snapshot.getValue(User::class.java)
                Log.d("UserCur", "Полученные данные пользователя: $user")

                if (user != null) {
                    // Обновляем текст surname и name
                    view.findViewById<TextView>(R.id.textName).text = user.name
                    view.findViewById<TextView>(R.id.textSurname).text = user.surname

                    // Форматируем профильный текст
                    val formattedText = "${user.surname[0]}${user.name[0]}"
                    view.findViewById<TextView>(R.id.profileText).text = formattedText
                }

                loading.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("User", "Failed to read value.", error.toException())
                loading.error()
            }
        })

        view.findViewById<Button>(R.id.btnLogOut).setOnClickListener {
            auth.signOut()
            val intent = Intent(requireActivity(), Authentication::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
}