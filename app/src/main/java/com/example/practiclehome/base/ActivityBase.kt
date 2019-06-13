package com.example.practiclehome.base

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.example.practiclehome.R

/**
 * Created by Jaydip.
 */

abstract class ActivityBase<V : ViewModelBase> : AppCompatActivity() {
    private var mViewModel: V? = null
    private var loadingDialog: AlertDialog? = null

    /**
     * Override for get view model
     *
     * @return
     */
    abstract val viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpViewModel()
        setUpSnackbar()
        setupLoadingDialog();
    }

    /**
     * Get View model of current page
     */
    private fun setUpViewModel() {
        this.mViewModel = if (mViewModel == null) viewModel else mViewModel
    }

    /**
     * Method observe for show snackBar
     */
    private fun setUpSnackbar() {
        mViewModel!!.snackbarMessage.observe(this, Observer { data ->
            if (data != null) {
                if (data is Int) {
                    showSnackbar(getString((data as Int?)!!))
                } else if (data is String) {
                    showSnackbar(data as String)
                }
            }
        }

        )
    }

    private fun setupLoadingDialog() {
        mViewModel!!.loadingDialog.observe(this, Observer { data ->
            if (data != null) {
                if (data) {
                    createLoadingDialog()
                } else {
                    loadingDialog?.dismiss()
                }
            }
        })
    }

    /**
     * Method for show snackBar
     *
     * @param message
     */
    fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            message, Snackbar.LENGTH_LONG
        )

        val view = snackbar.view
        val snackTV = view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        snackTV.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        snackbar.show()
    }

    /*Mehod for hide keyboard*/

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    @SuppressLint("InflateParams")
    fun createLoadingDialog() {
        val adb = AlertDialog.Builder(this)
        adb.setCancelable(false)
        adb.setIcon(R.mipmap.ic_launcher)
        loadingDialog = adb.create()
        loadingDialog?.show()
    }

}
