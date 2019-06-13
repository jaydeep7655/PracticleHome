package com.example.practiclehome.model

import java.io.Serializable

data class ModelEmployee(
    val employee_age: String,
    val employee_name: String,
    val employee_salary: String,
    val id: String,
    val profile_image: String
): Serializable