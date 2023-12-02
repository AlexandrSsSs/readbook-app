package com.example.redbook.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.redbook.R
import com.example.redbook.databinding.ActivityEditAnimalBinding
import com.example.redbook.datasource.ServiceBuilder
import com.example.redbook.interfaces.Api
import com.example.redbook.model.Animal
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditAnimalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditAnimalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAnimalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animalId = intent.getStringExtra("ANIMAL_ID")!!

        binding.backButton.setOnClickListener {
            val userId = intent.getStringExtra("USER_ID")!!
            val intent = Intent(this@EditAnimalActivity,AdminPanelActivity::class.java)
            intent.putExtra("USER_ID",userId)
            startActivity(intent)
        }

        getAnimalById(animalId)

        binding.editButton.setOnClickListener {

                val name = binding.animalNameEditText.text.toString().trim()
                val desc = binding.animalDescEditText.text.toString().trim()

            if(name.isNotEmpty()){
                val data = JsonObject()
                data.addProperty("name",name)
                editAnimal(data)
                binding.animalNameEditText.text.clear()
            }
            if(desc.isNotEmpty()){
                val data = JsonObject()
                data.addProperty("description",desc)
                editAnimal(data)
                binding.animalDescEditText.text.clear()
            }
            getAnimalById(animalId)
        }

    }

    private fun editAnimal(data: JsonObject) {
        val api = ServiceBuilder.buildService(Api::class.java)
        val animalId = intent.getStringExtra("ANIMAL_ID")!!
        api.updateAnimal(animalId,data).enqueue(object:Callback<Animal>{
            override fun onResponse(call: Call<Animal>, response: Response<Animal>) {

            }
            override fun onFailure(call: Call<Animal>, t: Throwable) {
               Log.e("zcx123",t.message.toString())
            }

        })
    }

    private fun getAnimalById(animalId: String) {
        val api = ServiceBuilder.buildService(Api::class.java)
        api.getAnimalById(animalId).enqueue(object:Callback<Animal>{
            override fun onResponse(call: Call<Animal>, response: Response<Animal>) {
                if(response.isSuccessful){
                    binding.animalNameEditText.hint = response.body()!!.name
                    binding.animalDescEditText.hint = response.body()!!.description
                }
            }
            override fun onFailure(call: Call<Animal>, t: Throwable) {
                Log.e("zc123",t.message.toString())
            }

        })
    }
}