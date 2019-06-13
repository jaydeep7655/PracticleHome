package com.example.practiclehome.viewmodel

import android.app.Application
import android.databinding.ObservableField
import com.example.practiclehome.base.ViewModelBase
import com.example.practiclehome.model.ModelEmployee
import com.example.practiclehome.utility.SingleLiveEvent

class ViewModelActivityEmployeeDetail(application: Application) : ViewModelBase(application) {
    var obsId = ObservableField<String>("")
    var obsEmployeeName = ObservableField<String>("")
    var obsEmployeeSalary = ObservableField<String>("")
    var obsEmployeeAge = ObservableField<String>("")
    var obsProfileImage = ObservableField<String>("")
    private var liveClickEvent = SingleLiveEvent<Int>()
    fun getLiveClickEvent(): SingleLiveEvent<Int> {
        return liveClickEvent
    }

    fun onClickEvent(viewId: Int) {

        liveClickEvent.value = viewId
        liveClickEvent.call()

    }
}