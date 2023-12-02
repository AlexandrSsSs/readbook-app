package com.example.redbook.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.redbook.R
import com.example.redbook.databinding.ActivityUserBinding
import com.example.redbook.datasource.ServiceBuilder
import com.example.redbook.interfaces.Api
import com.example.redbook.model.User
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            val userId = intent.getStringExtra("USER_ID")
            val intent = Intent(this@UserActivity,MainActivity::class.java)
            intent.putExtra("USER_ID",userId)
            startActivity(intent)
        }

        binding.editButton.setOnClickListener {
            if(binding.loginEditText.text.toString().trim().isNotEmpty()){
                val data = JsonObject()
                data.addProperty("login",binding.loginEditText.text.toString().trim())
                editUser(data)
                binding.loginEditText.text.clear()
            }
            if(binding.passwordEditText.text.toString().trim().isNotEmpty()){
                val data = JsonObject()
                data.addProperty("password",binding.passwordEditText.text.toString().trim())
                editUser(data)
                binding.passwordEditText.text.clear()
            }
            getUserById()
        }
        binding.deleteButton.setOnClickListener {
            val userId = intent.getStringExtra("USER_ID")!!
            deleteUser(userId)
        }

        binding.adminButton.setOnClickListener {
            val userId = intent.getStringExtra("USER_ID")
            val intent = Intent(this@UserActivity,AdminPanelActivity::class.java)
            intent.putExtra("USER_ID",userId)
            startActivity(intent)
        }




        getUserById()
    }

    private fun getUserById() {
        val userId = intent.getStringExtra("USER_ID")!!
        val api = ServiceBuilder.buildService(Api::class.java)
        api.getUserById(userId).enqueue(object:Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){

                    binding.loginEditText.hint = response.body()!!.login
                    binding.passwordEditText.hint = "Password"

                    if(response.body()!!.role == "2"){
                        binding.adminButton.visibility = View.VISIBLE
                    }else{
                        binding.adminButton.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
               Log.e("UserActivity",t.message.toString())
            }

        })
    }

    private fun deleteUser(userId: String) {
        val api = ServiceBuilder.buildService(Api::class.java)
        api.deleteUser(userId).enqueue(object:Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    val intent = Intent(this@UserActivity,AuthActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(applicationContext, "Аккаунт удалён", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("UserActivity",t.message.toString())
            }

        })
    }

    private fun editUser(data : JsonObject) {
        val userId = intent.getStringExtra("USER_ID")!!
        val api = ServiceBuilder.buildService(Api::class.java)
        api.updateUser(userId,data).enqueue(object:Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {

            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("UserActivity",t.message.toString())
            }

        })
    }
}