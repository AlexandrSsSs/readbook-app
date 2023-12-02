package com.example.redbook.activities


import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.redbook.databinding.ActivityAddNewAnimalBinding
import com.example.redbook.datasource.ServiceBuilder
import com.example.redbook.interfaces.Api
import com.example.redbook.model.Animal
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class AddNewAnimalActivity : AppCompatActivity(){

    private lateinit var binding: ActivityAddNewAnimalBinding
    private var selectedImage: Uri? = null
    private lateinit var bitmap: Bitmap



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewAnimalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            val userId = intent.getStringExtra("USER_ID")!!
            val intent = Intent(this@AddNewAnimalActivity,AdminPanelActivity::class.java)
            intent.putExtra("USER_ID",userId)
            startActivity(intent)
        }

        binding.pickImageButton.setOnClickListener {
            pickImageGallery()
        }

        binding.createButton.setOnClickListener {
            val name = binding.animalNameEditText.text.toString().trim()
            val desc = binding.animalDescEditText.text.toString().trim()
          addNewAnimal(name,desc)
        }
    }

    private fun pickImageGallery() {
            Intent(Intent.ACTION_PICK).also {
                it.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg","image/png")
                it.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes)
                startActivityForResult(it, REQUEST_CODE_IMAGE_PICKER)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_CODE_IMAGE_PICKER -> {
                    selectedImage = data?.data
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedImage)
                    binding.animalImageImageView.setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun addNewAnimal(name: String, desc: String) {
        val api = ServiceBuilder.buildService(Api::class.java)
        val data = JsonObject()
        data.addProperty("name",name)
        data.addProperty("description",desc)
        api.createAnimalNoImage(data).enqueue(object : Callback<Animal>{
            override fun onResponse(call: Call<Animal>, response: Response<Animal>) {
              if(response.isSuccessful){
                  Toast.makeText(applicationContext, "Animal created", Toast.LENGTH_SHORT).show()
              }
            }
            override fun onFailure(call: Call<Animal>, t: Throwable) {
               Log.e("zxc123", t.message.toString())
            }

        })

    }

    private fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""
        val returnCursor = this.query(fileUri,
            null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }

    companion object{
        private const val REQUEST_CODE_IMAGE_PICKER = 100
    }

}


