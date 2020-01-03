package com.mobile.fkpn.model

import android.R.style.Theme_Translucent_NoTitleBar
import android.app.Dialog
import androidx.fragment.app.FragmentActivity
import com.mobile.fkpn.R

class LoadingFragment(fragmentActivity: FragmentActivity) {
    private val dialog = Dialog(fragmentActivity, Theme_Translucent_NoTitleBar)

    init {
        val view = fragmentActivity.layoutInflater.inflate(R.layout.loading, null)
        dialog.setContentView(view)
        dialog.setCancelable(false)
    }

    fun openDialog() {
        dialog.show()
    }

    fun closeDialog() {
        dialog.dismiss()
    }
}