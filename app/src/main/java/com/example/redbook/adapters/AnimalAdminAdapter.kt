package com.example.redbook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.redbook.databinding.RecyclerItemAnimalAdminBinding
import com.example.redbook.databinding.RecyclerItemAnimalBinding
import com.example.redbook.datasource.ServiceBuilder.baseUrl
import com.example.redbook.model.Animal


class AnimalAdminAdapter(): RecyclerView.Adapter<AnimalAdminAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RecyclerItemAnimalAdminBinding): RecyclerView.ViewHolder(binding.root)


    private var animalList = ArrayList<Animal>()
    var onUpdateClick : ((Animal) -> Unit)? = null
    var onDeleteClick : ((Animal) -> Unit)? = null


    fun setAnimalList(animalList: List<Animal>){
        this.animalList = animalList as ArrayList<Animal>
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalAdminAdapter.ViewHolder {
        return ViewHolder(
            RecyclerItemAnimalAdminBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: AnimalAdminAdapter.ViewHolder, position: Int) {

        holder.binding.animalName.text = animalList[position].name

        Glide.with(holder.itemView).load("$baseUrl"+animalList[position].animalImage).into(holder.binding.animalImage)


      holder.binding.editButton.setOnClickListener {
          onUpdateClick!!.invoke(animalList[position])
           }
      holder.binding.deleteButton.setOnClickListener {
           onDeleteClick!!.invoke(animalList[position])
       }
    }

    override fun getItemCount(): Int {
       return animalList.size
    }

}