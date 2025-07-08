package com.practise.taski_event.base

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.practise.taski_event.frags.loading.LoadingFragment

abstract class BaseAppFragment: Fragment(), BaseApp {
    private val dialog = LoadingFragment()

    override fun showToast(msg : String) { Toast.makeText(requireContext(),msg,Toast.LENGTH_SHORT).show() }

    override fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun showProgress() { dialog.show(parentFragmentManager,"Loading") }

    override fun hideProgress() { dialog.dismiss() }
}