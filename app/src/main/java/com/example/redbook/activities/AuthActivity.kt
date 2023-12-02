package com.example.redbook.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.redbook.R
import com.example.redbook.databinding.ActivityAuthBinding
import com.example.redbook.datasource.ServiceBuilder
import com.example.redbook.interfaces.Api
import com.example.redbook.model.User
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.RegButton.setOnClickListener {
            val intent = Intent(this@AuthActivity, RegActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val login = binding.loginEditText.text.toString().trim()
            val password = binding.PasswordEditText.text.toString().trim()

            if(login.isNotEmpty() && password.isNotEmpty()){
             auth(login,password)
            }
        }


    }

    private fun auth(login: String, password: String) {
        val data = JsonObject()
        data.addProperty("login",login)
        data.addProperty("password",password)
        val api = ServiceBuilder.buildService(Api::class.java)
        api.login(data).enqueue(object: Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
               if(response.isSuccessful){
                   val intent = Intent(this@AuthActivity, MainActivity::class.java)
                   intent.putExtra("USER_ID",response.body()!!.userId)
                   startActivity(intent)
                   Toast.makeText(applicationContext, "Вход выполнен успешно!", Toast.LENGTH_SHORT).show()
               }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
               Log.e("AuthActivity", t.message.toString())
            }

        })
    }
}