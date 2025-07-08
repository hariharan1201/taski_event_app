package com.practise.taski_event.base

import android.view.View

interface BaseApp {
    fun showToast(msg: String)
    fun hideKeyboard(view: View)
    fun showProgress()
    fun hideProgress()
}