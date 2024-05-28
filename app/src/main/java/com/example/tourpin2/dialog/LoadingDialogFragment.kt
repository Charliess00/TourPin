package com.example.tourpin2.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.example.tourpin2.R

class LoadingDialogFragment(private val mContext: Context) { // Используйте Context вместо Fragment
    private lateinit var isdialog: AlertDialog
    private lateinit var newDialog: AlertDialog

    fun start() {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.loading_item, null)
        val builder = AlertDialog.Builder(mContext) // Передайте mContext здесь
        builder.setView(dialogView)
        builder.setCancelable(false)
        isdialog = builder.create()
        isdialog.show()
    }

    fun dismiss() {
        isdialog.dismiss()
    }

    fun end() {
        isdialog.dismiss()

        val newInflator = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val newDialogView = newInflator.inflate(R.layout.successful_item, null)
        val newBuilder = AlertDialog.Builder(mContext) // Передайте mContext здесь
            .setView(newDialogView)
            .setCancelable(true)

        newDialog = newBuilder.create()
        newDialog.show()

        // Запускаем таймер для автоматического закрытия нового диалога через 5 секунд
        Handler(Looper.getMainLooper()).postDelayed({
            if (newDialog.isShowing) {
                newDialog.dismiss()
            }
        }, 3000)
    }
}
