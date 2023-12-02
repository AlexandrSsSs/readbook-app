package com.example.redbook.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.redbook.adapters.AnimalAdapter
import com.example.redbook.databinding.ActivityMainBinding
import com.example.redbook.datasource.ServiceBuilder
import com.example.redbook.interfaces.Api
import com.example.redbook.model.Animal
import com.example.redbook.model.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var animalAdapter: AnimalAdapter
    var animalList = ArrayList<Animal>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        animalAdapter = AnimalAdapter()
        getAllAnimals()

        binding.accountDetailsButton.setOnClickListener {
            val userId = intent.getStringExtra("USER_ID")
            val intent = Intent(this@MainActivity,UserActivity::class.java)
            intent.putExtra("USER_ID",userId)
            startActivity(intent)
        }
    }

    private fun getAllAnimals() {

        val api = ServiceBuilder.buildService(Api::class.java)
        api.getAllAnimals().enqueue(object: Callback<ApiResponse<Animal>>{
            override fun onResponse(call: Call<ApiResponse<Animal>>, response: Response<ApiResponse<Animal>>) {
                animalList = response.body()!!.result as ArrayList<Animal>
                animalAdapter.setAnimalList(animalList)
                binding.recyclerView.apply {
                    adapter = animalAdapter
                }

                animalAdapter.onItemClick = { animal->
                    val userId = intent.getStringExtra("USER_ID")
                    val intent = Intent(this@MainActivity,AnimalActivity::class.java)
                    intent.putExtra("USER_ID",userId)
                    intent.putExtra("animalId",animal.animalId)
                    startActivity(intent)

                }
            }
            override fun onFailure(call: Call<ApiResponse<Animal>>, t: Throwable) {
                Log.e("MainActivity", t.message.toString())
            }

        })
    }
}