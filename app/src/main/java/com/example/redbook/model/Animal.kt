package com.example.redbook.model

import com.google.gson.annotations.SerializedName

data class Animal(
    @SerializedName("name")var name: String? = null,
    @SerializedName("description")var description: String? = null,
    var animalImage: String? = null,
    @SerializedName("_id")var animalId: String? = null,
)




