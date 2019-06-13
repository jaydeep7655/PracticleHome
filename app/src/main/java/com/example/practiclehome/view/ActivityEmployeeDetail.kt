package com.example.practiclehome.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.example.practiclehome.R
import com.example.practiclehome.base.ActivityBase
import com.example.practiclehome.model.ModelEmployee
import com.example.practiclehome.viewmodel.ViewModelActivityEmployeeDetail

class ActivityEmployeeDetail : ActivityBase<ViewModelActivityEmployeeDetail>() {
    private lateinit var toolbar: Toolbar
    override val viewModel: ViewModelActivityEmployeeDetail
        get() = ViewModelProviders.of(this).get(ViewModelActivityEmployeeDetail::class.java)
    private lateinit var mContext: Context
    private lateinit var bindingObj: com.example.practiclehome.databinding.ActivityEmployeeDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        bindingObj = DataBindingUtil.setContentView(this, R.layout.activity_employee_detail)
        bindingObj.vmObj = this.viewModel
        getIntentData()
        setUpToolbar()
        setUpEvents();
    }

    private fun getIntentData() {
        if (intent != null && intent.hasExtra("employeedetail")) {
           var employeeDetail:ModelEmployee=this.intent.getSerializableExtra("employeedetail") as ModelEmployee

            viewModel.obsEmployeeAge.set(employeeDetail.employee_age)
            viewModel.obsId.set(employeeDetail.id)
            viewModel.obsEmployeeName.set(employeeDetail.employee_name)
            viewModel.obsEmployeeSalary.set(employeeDetail.employee_salary)
            viewModel.obsProfileImage.set(employeeDetail.profile_image)

        }

    }

    private fun setUpToolbar() {
        toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setTitle(R.string.employee_detail_title)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        setSupportActionBar(toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }

    private fun setUpEvents() {
        viewModel.getLiveClickEvent().observe(this, Observer { viewId ->
            if (viewId != null) {
                when (viewId) {
                }
            }

        })
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.stay, R.anim.push_to_right)
    }
}
