package com.example.myapplication.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.model.ScheduleResponse
import com.example.myapplication.repository.ScheduleRepository
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


class HomeViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val repository = ScheduleRepository(context)
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class:${modelClass.name}")
    }
}
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: HomeScheduleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the ViewModel using the custom factory
        val factory = HomeViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupObservers()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupUi() {
        // Setup RecyclerView and adapter
        adapter = HomeScheduleAdapter { schedule ->
            showScheduleDetailsDialog(schedule)
        }
        binding.rvSchedules.layoutManager =
            LinearLayoutManager(requireContext())
        binding.rvSchedules.adapter = adapter
        // Add a divider between list items

        binding.rvSchedules.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        // Fetch today's schedules (format YYYY-MM-DD)
        binding.btnAddHabit.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addHabitFragment)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatIsoDateTime(isoString: String?): Pair<String, String> {
        if (isoString.isNullOrEmpty()) {
            return "N/A" to "N/A"
        }
        return try {
            val odt = OffsetDateTime.parse(isoString)
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy MM dd")
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            odt.format(dateFormatter) to odt.format(timeFormatter)
        } catch (e: Exception) {
            if (isoString.contains("T")) {
                val parts = isoString.split("T")
                val datePart = parts[0].replace("-", " ")
                val timePart = if (parts[1].length >= 5) parts[1].substring(0, 5) else "N/A"
                datePart to timePart
            } else {
                isoString to "N/A"
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showScheduleDetailsDialog(schedule: ScheduleResponse) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_schedule_details, null)

        val tvHabitName = dialogView.findViewById<TextView>(R.id.tv_habit_name)
        val tvDate = dialogView.findViewById<TextView>(R.id.tv_date)
        val tvTime = dialogView.findViewById<TextView>(R.id.tv_time)

        tvHabitName.text = schedule.habit?.name ?: "Unknown Habit"

        val (startDate, startTime) = formatIsoDateTime(schedule.start_time)
        val (_, endTime) = formatIsoDateTime(schedule.end_time)

        tvDate.text = startDate
        tvTime.text = if (startTime != "N/A" && endTime != "N/A") "$startTime - $endTime" else startTime

        val mainDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Schedule Details")
            .setPositiveButton("Vissza", null)
            .setNegativeButton("Törlés", null)
            .create()

        mainDialog.setOnShowListener {
            val deleteButton = mainDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            deleteButton.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle("Törlés megerősítése")
                    .setMessage("Biztosan törölni szeretné ezt a teendőt?")
                    .setPositiveButton("Igen") { _, _ ->
                        viewModel.deleteSchedule(schedule.id)
                        mainDialog.dismiss()
                    }
                    .setNegativeButton("Nem", null)
                    .show()
            }

            val backButton = mainDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            backButton.setOnClickListener {
                mainDialog.dismiss()
            }
        }

        mainDialog.show()
    }

    private fun setupObservers() {
        viewModel.schedules.observe(viewLifecycleOwner) { schedules ->
            if (!schedules.isNullOrEmpty()) {
                adapter.submitList(schedules)
                binding.tvEmpty.visibility = View.GONE
            } else {
                adapter.submitList(emptyList())
                binding.tvEmpty.visibility = View.VISIBLE
            }
        }
        //viewModel.isLoading.observe
        //viewModel.errorMessage.observe
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
