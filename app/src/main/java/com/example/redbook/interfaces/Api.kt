package com.example.redbook.interfaces

import com.example.redbook.model.Animal
import com.example.redbook.model.ApiResponse
import com.example.redbook.model.User
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface Api {
    //User
    @Headers("Content-Type:application/json")
    @POST("user/login")
    fun login(
        @Body body: JsonObject
    ): Call<User>

    @Headers("Content-Type:application/json")
    @POST("user/create")
    fun registration(
        @Body body: JsonObject
    ): Call<User>

    @Headers("Content-Type:application/json")
    @POST("user/{userId}")
    fun getUserById(
        @Path("userId") id: String
    ): Call<User>

    @Headers("Content-Type:application/json")
    @POST("user/update/{userId}")
    fun updateUser(
        @Path("userId") id : String,
        @Body body: JsonObject
    ): Call<User>

    @Headers("Content-Type:application/json")
    @POST("user/delete/{userId}")
    fun deleteUser(
        @Path("userId") id : String
    ): Call<User>

    //Animal
    @Headers("Content-Type:application/json")
    @POST("animal/all")
    fun getAllAnimals(
    ): Call<ApiResponse<Animal>>

    @Headers("Content-Type:application/json")
    @POST("animal/id/{animalId}")
    fun getAnimalById(
        @Path("animalId") id : String
    ): Call<Animal>

    @Headers("Content-Type:application/json")
    @POST("animal/update/{animalId}")
    fun updateAnimal(
        @Path("animalId") id : String,
        @Body body: JsonObject
    ): Call<Animal>


    @Headers("Content-Type:application/json")
    @POST("animal/delete/{animalId}")
    fun deleteAnimal(
        @Path("animalId") id : String
    ): Call<Animal>

//    //create new animal with image
//    @POST("animal/create")
//    @Multipart
//    fun createAnimal(
////        @Part("name") animalName: RequestBody,
////        @Part("description") description: RequestBody,
//        @Part image: MultipartBody.Part
//    ): Call<Animal>

    //create animal with out image
    @Headers("Content-Type:application/json")
    @POST("animal/create/noImage")
    fun createAnimalNoImage(
        @Body body: JsonObject
    ): Call<Animal>

}