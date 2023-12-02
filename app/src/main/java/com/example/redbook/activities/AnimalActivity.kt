package com.example.redbook.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.view.menu.MenuView.ItemView
import com.bumptech.glide.Glide
import com.example.redbook.R
import com.example.redbook.databinding.ActivityAnimalBinding
import com.example.redbook.datasource.ServiceBuilder
import com.example.redbook.interfaces.Api
import com.example.redbook.model.Animal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnimalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnimalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animalId = intent.getStringExtra("animalId")!!

        getAnimalById(animalId)

        binding.backButton.setOnClickListener {
            val userId = intent.getStringExtra("USER_ID")
            val intent = Intent(this@AnimalActivity,MainActivity::class.java)
            intent.putExtra("USER_ID",userId)
            startActivity(intent)
        }

    }

    private fun getAnimalById(animalId: String) {
        val api = ServiceBuilder.buildService(Api::class.java)
        api.getAnimalById(animalId).enqueue(object: Callback<Animal>{
            override fun onResponse(call: Call<Animal>, response: Response<Animal>) {

                    binding.animalName.text = response.body()!!.name
                    binding.animalDesc.text = response.body()!!.description
                    Glide
                        .with(binding.root)
                        .load(ServiceBuilder.baseUrl + response.body()!!.animalImage)
                        .into(binding.animalImage)

            }
            override fun onFailure(call: Call<Animal>, t: Throwable) {
                Log.e("AnimalActivity", t.message.toString())
            }

        })
    }
}