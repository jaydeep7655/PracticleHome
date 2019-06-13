package com.example.practiclehome.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.databinding.ObservableField
import com.example.practiclehome.R
import com.example.practiclehome.base.ViewModelBase
import com.example.practiclehome.model.ModelEmployee
import com.example.practiclehome.utility.SingleLiveEvent
import com.example.practiclehome.utility.UDF
import com.seekmi.logistics.network.ApiCallFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


@Suppress("UNCHECKED_CAST")
class ViewModelActivityMain(application: Application) : ViewModelBase(application) {

    var alEmployeeList = ArrayList<ModelEmployee>()
    private var liveClickEvent = SingleLiveEvent<Int>()
    var isLoading = ObservableField<Boolean>(false)
    val selectedEmployeePosition = SingleLiveEvent<Int>()


    fun getLiveClickEvent(): SingleLiveEvent<Int> {
        return liveClickEvent
    }

    fun onClickEvent(viewId: Int) {


    }

    fun onClickEvent(viewId: Int, position: Int, id: String) {
        when (viewId) {
            R.id.llEmployee -> {
                selectedEmployeePosition.value = position
                liveClickEvent.value = R.id.llEmployee
                liveClickEvent.call()

            }
        }

    }

    @SuppressLint("CheckResult")
    fun CallApiGetEmployee() {
        if (UDF.isOnline(getApplication())) {
            ApiCallFactory.create().getEmployeeList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoadingDialog(true) }
                .doOnComplete { showLoadingDialog(false) }
                .subscribe({ result ->
                    onReady()
                    if (result.isSuccessful && result.body() != null && !result.body()!!.isEmpty()) {
                        alEmployeeList = result.body() as ArrayList<ModelEmployee>
                        liveClickEvent.value = 1
                        liveClickEvent.call()
                    } else {
                        showSnackbarMessage("No user Found")
                    }
                }, { error ->
                    onError();
                    showSnackbarMessage(R.string.general_error)
                })

        } else {
            showSnackbarMessage(R.string.message_no_connection)
        }
    }

    @SuppressLint("CheckResult")
    fun callApiDeleteEmployee(employeeId: String) {
        if (UDF.isOnline(getApplication())) {
            ApiCallFactory.create().getDeleteuser(employeeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoadingDialog(true) }
                .doOnComplete { showLoadingDialog(false) }
                .subscribe({ result ->
                    onReady()
                    if (result != null) {
                        showSnackbarMessage(result.success.text)
                    } else {
                        showSnackbarMessage("No user Found")
                    }
                }, { error ->
                    onError();
                    showSnackbarMessage(R.string.general_error)
                })

        } else {
            showSnackbarMessage(R.string.message_no_connection)
        }
    }


    fun onRefresh() {
        liveClickEvent.value = 2
        liveClickEvent.call()
    }

    fun onReady() {
        isLoading.set(false)
    }

    fun onError() {
        isLoading.set(false)
    }
}