package com.example.tourpin2.dialog

import android.app.Activity
import android.app.AlertDialog
import android.os.Handler
import android.os.Looper
import com.example.tourpin2.R

class LoadingDialog(val mActivity: Activity) {
    private lateinit var isdialog: AlertDialog
    private lateinit var newDialog: AlertDialog

    fun start() {
        val inflater = mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.loading_item, null)
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        isdialog = builder.create()
        isdialog.show()

        Handler(Looper.getMainLooper()).postDelayed({
            if (isdialog.isShowing) {
                error()
            }
        }, 20000)
    }

    fun dismiss(){
        isdialog.dismiss()
    }

    fun end() {
        isdialog.dismiss()

        val newInflator = mActivity.layoutInflater
        val newDialogView = newInflator.inflate(R.layout.successful_item, null)
        val newBuilder = AlertDialog.Builder(mActivity)
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

    fun error() {
        isdialog.dismiss()

        val newInflator = mActivity.layoutInflater
        val newDialogView = newInflator.inflate(R.layout.error_item, null)
        val newBuilder = AlertDialog.Builder(mActivity)
            .setView(newDialogView)
            .setCancelable(true)

        newDialog = newBuilder.create()
        newDialog.show()

        Handler(Looper.getMainLooper()).postDelayed({
            if (newDialog.isShowing) {
                newDialog.dismiss()
            }
        }, 3000)
    }
}