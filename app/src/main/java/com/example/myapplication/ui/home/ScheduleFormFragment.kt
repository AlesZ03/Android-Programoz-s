package com.example.myapplication.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentAddScheduleBinding
import com.example.myapplication.repository.ScheduleRepository
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class ScheduleFormFragment : Fragment() {

    private lateinit var viewModel: ScheduleViewModel

    private var _binding: FragmentAddScheduleBinding? = null
    private val binding get() = _binding!!

    // Változók a formázott dátum és idő tárolására
    private var selectedDateFormatted: String? = null
    private var selectedStartTimeFormatted: String? = null
    private var selectedEndTimeFormatted: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = ScheduleRepository(requireContext())
        val factory = ScheduleViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ScheduleViewModel::class.java]

        // Kattintásfigyelők beállítása
        setupClickListeners()
        // ViewModel figyelése
        observeViewModel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupClickListeners() {
        binding.dateEditText.setOnClickListener {
            showDatePicker()
        }

        binding.startTimeEditText.setOnClickListener {
            showTimePicker(isStartTime = true)
        }

        binding.endTimeEditText.setOnClickListener {
            showTimePicker(isStartTime = false)
        }

        binding.btnSaveSchedule.setOnClickListener {
            val habitIdText = binding.etHabitId.text.toString()
            val durationText = binding.etDuration.text.toString()

            if (selectedDateFormatted == null || selectedStartTimeFormatted == null || selectedEndTimeFormatted == null || habitIdText.isBlank()) {
                Toast.makeText(requireContext(), "A dátum, idő és 'Habit ID' mezők kitöltése kötelező!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.addSchedule(
                habitId = habitIdText.toLong(),
                date = selectedDateFormatted!!,
                startTime = selectedStartTimeFormatted!!,
                endTime = selectedEndTimeFormatted!!,
                durationMinutes = durationText.toIntOrNull() ?: 0,
                participantIds = binding.etParticipants.text.toString(),
                notes = binding.etNotes.text.toString()
            )
        }
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Válassz dátumot")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val backendFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            selectedDateFormatted = backendFormatter.format(Date(selection))

            val displayFormatter = SimpleDateFormat("yyyy. MM. dd.", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            binding.dateEditText.setText(displayFormatter.format(Date(selection)))
        }

        datePicker.show(childFragmentManager, "DATE_PICKER_TAG")
    }

    private fun showTimePicker(isStartTime: Boolean) {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(currentHour)
            .setMinute(currentMinute)
            .setTitleText(if (isStartTime) "Kezdés időpontja" else "Befejezés időpontja")
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute

            val backendTime = String.format(Locale.getDefault(), "%02d:%02d:00", hour, minute)
            val displayTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)

            if (isStartTime) {
                selectedStartTimeFormatted = backendTime
                binding.startTimeEditText.setText(displayTime)
            } else {
                selectedEndTimeFormatted = backendTime
                binding.endTimeEditText.setText(displayTime)
            }
        }

        timePicker.show(childFragmentManager, if (isStartTime) "START_TIME_PICKER_TAG" else "END_TIME_PICKER_TAG")
    }

    private fun observeViewModel() {
        viewModel.success.observe(viewLifecycleOwner) { success ->
            if (success == true) {
                Toast.makeText(requireContext(), "Sikeres mentés!", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(requireContext(), "Hiba: $error", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
