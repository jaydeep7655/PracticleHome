package com.example.practiclehome.model

import java.io.Serializable

data class ModelSucessResponse(
    val success: Success
) {
    data class Success(
        val text: String
    ): Serializable
}