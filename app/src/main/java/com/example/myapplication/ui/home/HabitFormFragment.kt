package com.example.myapplication.ui.home

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R

class HabitFormFragment : Fragment(R.layout.fragment_add_habit) {

    private lateinit var viewModel: AddHabitViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = AddHabitViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory)[AddHabitViewModel::class.java]

        val name = view.findViewById<EditText>(R.id.etHabitName)
        val desc = view.findViewById<EditText>(R.id.etHabitDescription)
        val goal = view.findViewById<EditText>(R.id.etHabitGoal)
        val categoryId = view.findViewById<EditText>(R.id.etHabitCategoryId)

        val btn = view.findViewById<Button>(R.id.btnSaveHabit)
        val backBtn = view.findViewById<Button>(R.id.btnBack)

        btn.setOnClickListener {
            viewModel.addHabit(
                name.text.toString(),
                desc.text.toString(),
                categoryId.text.toString().toLong(),
                goal.text.toString()
            )
        }

        backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.success.observe(viewLifecycleOwner) {
            if (it == true) {
                Toast.makeText(requireContext(), "Habit created!", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            if (it != null)
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_LONG).show()
        }
    }
}
