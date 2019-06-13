package com.example.practiclehome.network


import com.example.practiclehome.model.ModelEmployee
import com.example.practiclehome.model.ModelSucessResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
/**
 * Created by Jaydip.
 */

interface ApiHelper {

    @GET("employees")
    abstract fun getEmployeeList(): Observable<Response<ArrayList<ModelEmployee>>>

    @DELETE("delete/{id}")
    abstract fun getDeleteuser(@Path("id") userId: String): Observable<ModelSucessResponse>


}
