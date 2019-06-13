package com.example.practiclehome.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.practiclehome.R
import com.example.practiclehome.base.ActivityBase
import com.example.practiclehome.databinding.ActivityMainBinding
import com.example.practiclehome.databinding.ItemEmployeeListBinding
import com.example.practiclehome.model.ModelEmployee
import com.example.practiclehome.utility.SwipeToDeleteCallback
import com.example.practiclehome.utility.UDF
import com.example.practiclehome.viewmodel.ViewModelActivityMain
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ActivityBase<ViewModelActivityMain>() {
    override val viewModel: ViewModelActivityMain
        get() = ViewModelProviders.of(this).get(ViewModelActivityMain::class.java)
    private lateinit var mContext: Context
    private lateinit var toolbar: Toolbar
    private var adapterEmployeeList: AdapterEmployeeList? = null
    private lateinit var bindingObj: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        bindingObj = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bindingObj.vmObj = this.viewModel


        initComponent()
        setUpToolbar()
        initiateEmployeelistApiCall()
        setUpEvents()

    }

    /**
     * initalization of view
     */
    private fun initComponent() {
        val linearLayoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        rvEmployee.setLayoutManager(linearLayoutManager);
        rvEmployee.setNestedScrollingEnabled(false);
    }

    /**
     * set toolbar
     */
    private fun setUpToolbar() {
        toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setTitle(R.string.title_user_list)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        setSupportActionBar(toolbar)
    }

    /**
     * callEmployee List api
     */
    private fun initiateEmployeelistApiCall() {
        if (UDF.isOnline(mContext)) {
            viewModel.CallApiGetEmployee();
        }
    }

    private fun setUpEvents() {
        viewModel.getLiveClickEvent().observe(this, Observer { viewId ->
            if (viewId != null) {
                when (viewId) {
                    1 -> {
                        setUpRecyclerAdapter()
                    }
                    2 -> {
                        viewModel.isLoading.set(true)
                        initiateEmployeelistApiCall()
                    }

                }
            }
        })
        viewModel.selectedEmployeePosition.observe(this, Observer { pos ->
            run {
                val intent = Intent(mContext, ActivityEmployeeDetail::class.java)
                var modelEmployee: ModelEmployee = viewModel.alEmployeeList.get(pos!!)
                intent.putExtra("employeedetail", modelEmployee)
                startActivity(intent)
                overridePendingTransition(R.anim.pull_from_right, R.anim.stay)
            }
        })
    }


    private fun setUpRecyclerAdapter() {
        if (!(viewModel.alEmployeeList).isEmpty()) {
            if (adapterEmployeeList == null) {
                adapterEmployeeList = AdapterEmployeeList()
                rvEmployee.adapter = adapterEmployeeList


                val swipeHandler = object : SwipeToDeleteCallback(mContext) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        viewModel.callApiDeleteEmployee(viewModel.alEmployeeList.get(viewHolder.adapterPosition).id)
                        val adapter = rvEmployee.adapter as AdapterEmployeeList
                        adapter.removeAt(viewHolder.adapterPosition)
                    }
                }
                val itemTouchHelper = ItemTouchHelper(swipeHandler)
                itemTouchHelper.attachToRecyclerView(rvEmployee)


            } else {
                adapterEmployeeList?.notifyDataSetChanged()
            }
        }
    }


    internal inner class AdapterEmployeeList : RecyclerView.Adapter<AdapterEmployeeList.MyViewHolder>() {
        lateinit var itemEmployeeList: com.example.practiclehome.databinding.ItemEmployeeListBinding

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterEmployeeList.MyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            itemEmployeeList = DataBindingUtil.inflate(layoutInflater, R.layout.item_employee_list, parent, false)
            return MyViewHolder(itemEmployeeList)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val employee = viewModel.alEmployeeList.get(position)
            if (employee != null) {
                holder.bind(employee, position)
            }
        }

        fun removeAt(position: Int) {
            viewModel.alEmployeeList.removeAt(position)
            notifyItemRemoved(position)
        }

        override fun getItemCount(): Int {
            return viewModel.alEmployeeList.size
        }

        internal inner class MyViewHolder(var itemEmployeeList: ItemEmployeeListBinding) :
            RecyclerView.ViewHolder(itemEmployeeList.getRoot()) {
            fun bind(employee: ModelEmployee, position: Int) {
                itemEmployeeList.vmObj = viewModel
                itemEmployeeList.itemObjEmployee = employee
                itemEmployeeList.position = position
                itemEmployeeList.executePendingBindings()
            }

        }


    }
}