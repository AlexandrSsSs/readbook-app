package com.example.redbook.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.redbook.R
import com.example.redbook.databinding.ActivityRegBinding
import com.example.redbook.datasource.ServiceBuilder
import com.example.redbook.interfaces.Api
import com.example.redbook.model.User
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            val intent = Intent(this@RegActivity,AuthActivity::class.java)
            startActivity(intent)
        }

        binding.regButton.setOnClickListener {
            val login = binding.loginEditText.text.toString().trim()
            val password = binding.PasswordEditText.text.toString().trim()

            if(login.isNotEmpty() && password.isNotEmpty()){
                registration(login, password)
            }
        }

    }

    private fun registration(login: String, password: String) {
        val data = JsonObject()
        data.addProperty("login",login)
        data.addProperty("password",password)
        val api = ServiceBuilder.buildService(Api::class.java)
        api.registration(data).enqueue(object :Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    val intent = Intent(this@RegActivity,AuthActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(applicationContext, "Пользователь успешно создан", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
               Log.e("RegActivity",t.message.toString())
            }

        })
    }
}