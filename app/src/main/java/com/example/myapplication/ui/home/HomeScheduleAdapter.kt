package com.example.myapplication.ui.home

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemHomeScheduleBinding
import com.example.myapplication.model.ScheduleResponse

import android.util.Log
import android.view.View
import com.example.myapplication.R
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class HomeScheduleAdapter(
    private val onItemClicked: (ScheduleResponse) -> Unit
) :
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
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
        holder.bind(item)
    }
    class ViewHolder(private val binding: ItemHomeScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: ScheduleResponse) {
            binding.tvTitle.text = item.habit?.name ?: "Unknown Habit"

            val startTime = formatTime(item.start_time)
            val endTime = formatTime(item.end_time)
            binding.tvTime.text = "$startTime - $endTime"

            binding.tvNotes.visibility = View.GONE
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun formatTime(isoString: String?): String {
            if (isoString.isNullOrEmpty()) {
                return "N/A"
            }
            return try {
                val odt = OffsetDateTime.parse(isoString)
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                odt.format(timeFormatter)
            } catch (e: Exception) {
                if (isoString.contains("T")) {
                    val parts = isoString.split("T")
                    if (parts[1].length >= 5) parts[1].substring(0, 5) else "N/A"
                } else {
                    isoString
                }
            }
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