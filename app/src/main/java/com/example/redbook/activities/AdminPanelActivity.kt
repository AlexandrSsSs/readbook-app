package com.example.redbook.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.redbook.R
import com.example.redbook.adapters.AnimalAdminAdapter
import com.example.redbook.databinding.ActivityAdminPanelBinding
import com.example.redbook.datasource.ServiceBuilder
import com.example.redbook.interfaces.Api
import com.example.redbook.model.Animal
import com.example.redbook.model.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminPanelBinding
    private lateinit var animalAdminAdapter: AnimalAdminAdapter
     var animalList = ArrayList<Animal>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAdminPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        animalAdminAdapter = AnimalAdminAdapter()

        binding.backButton.setOnClickListener {

            val userId = intent.getStringExtra("USER_ID")!!
            val intent = Intent(this@AdminPanelActivity,UserActivity::class.java)
            intent.putExtra("USER_ID",userId)
            startActivity(intent)

        }

        binding.addButton.setOnClickListener {
            val userId = intent.getStringExtra("USER_ID")!!
            val intent = Intent(this@AdminPanelActivity,AddNewAnimalActivity::class.java)
            intent.putExtra("USER_ID",userId)
            startActivity(intent)
        }

        getAllAnimals()
    }

    private fun getAllAnimals() {
        val api = ServiceBuilder.buildService(Api::class.java)
        api.getAllAnimals().enqueue(object: Callback<ApiResponse<Animal>> {
            override fun onResponse(call: Call<ApiResponse<Animal>>, response: Response<ApiResponse<Animal>>) {
                animalList = response.body()!!.result as ArrayList<Animal>
                animalAdminAdapter.setAnimalList(animalList)
                binding.recyclerView.apply {
                    adapter = animalAdminAdapter
                }

                animalAdminAdapter.onUpdateClick = { animal->
                    val userId = intent.getStringExtra("USER_ID")!!
                    val intent = Intent(this@AdminPanelActivity,EditAnimalActivity::class.java)
                    intent.putExtra("USER_ID",userId)
                    intent.putExtra("ANIMAL_ID",animal.animalId)
                    startActivity(intent)
                }

                animalAdminAdapter.onDeleteClick = {animal->

                    api.deleteAnimal(animal.animalId.toString()).enqueue(object:Callback<Animal>{
                        override fun onResponse(call: Call<Animal>, response: Response<Animal>) {
                            Toast.makeText(applicationContext, "Animal deleted", Toast.LENGTH_SHORT).show()
                            getAllAnimals()
                        }

                        override fun onFailure(call: Call<Animal>, t: Throwable) {
                            Log.e("AdminPanelActivity", t.message.toString())
                        }

                    })
                }
            }
            override fun onFailure(call: Call<ApiResponse<Animal>>, t: Throwable) {
                Log.e("MainActivity", t.message.toString())
            }

        })
    }
}