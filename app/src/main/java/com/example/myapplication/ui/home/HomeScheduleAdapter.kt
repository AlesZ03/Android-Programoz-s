package com.example.myapplication.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemHomeScheduleBinding
import com.example.myapplication.model.ScheduleResponse
import android.util.Log
import android.view.View
import com.example.myapplication.R




class HomeScheduleAdapter :
ListAdapter<ScheduleResponse, HomeScheduleAdapter.ViewHolder>
(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val binding = ItemHomeScheduleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    class ViewHolder(private val binding: ItemHomeScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ScheduleResponse) {
            binding.tvTitle.text = item.habit?.name ?: "Unknown Habit"
            // Format time
            // Set notes visibility
            // set icon based on category
        }
    }
    class DiffCallback : DiffUtil.ItemCallback<ScheduleResponse>() {
        override fun areItemsTheSame(oldItem: ScheduleResponse, newItem:
        ScheduleResponse): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: ScheduleResponse,
                                        newItem: ScheduleResponse): Boolean {
            return oldItem == newItem
        }
    }
}