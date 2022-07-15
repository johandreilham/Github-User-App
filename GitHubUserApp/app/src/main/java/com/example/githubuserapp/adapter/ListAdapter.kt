package com.example.githubuserapp.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubuserapp.data.GitHub
import com.example.githubuserapp.databinding.ListItemBinding


class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private val list = ArrayList<GitHub>()

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setList(users: ArrayList<GitHub>){
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: GitHub){
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(user)
            }

            binding.apply {
                Glide.with(itemView)
                    .load(user.avatar_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(imgPhoto)
                txtUsername.text = "@${user.login}"

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
       val view = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder((view))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickCallback {
        fun onItemClicked(data: GitHub)
    }
}