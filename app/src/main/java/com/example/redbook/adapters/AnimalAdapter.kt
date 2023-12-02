package com.example.redbook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.redbook.databinding.RecyclerItemAnimalBinding
import com.example.redbook.datasource.ServiceBuilder.baseUrl
import com.example.redbook.model.Animal


class AnimalAdapter(): RecyclerView.Adapter<AnimalAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RecyclerItemAnimalBinding): RecyclerView.ViewHolder(binding.root)


    private var animalList = ArrayList<Animal>()
    var onItemClick : ((Animal) -> Unit)? = null


    fun setAnimalList(animalList: List<Animal>){
        this.animalList = animalList as ArrayList<Animal>
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalAdapter.ViewHolder {
        return ViewHolder(
            RecyclerItemAnimalBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: AnimalAdapter.ViewHolder, position: Int) {

        holder.binding.animalName.text = animalList[position].name

        Glide.with(holder.itemView).load("$baseUrl"+animalList[position].animalImage).into(holder.binding.animalImage)


       holder.binding.animalName.setOnClickListener {
           onItemClick!!.invoke(animalList[position])
       }
       holder.binding.animalImage.setOnClickListener {
           onItemClick!!.invoke(animalList[position])
       }
    }

    override fun getItemCount(): Int {
       return animalList.size
    }

}