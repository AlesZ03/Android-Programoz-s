package com.example.myapplication.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R

class AddHabitFragment : Fragment() {

    private lateinit var viewModel: AddHabitViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_habit, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Létrehozod a factory-t, ami átadja a repository-t a ViewModelnek
        val factory = AddHabitViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory)[AddHabitViewModel::class.java]

        val name = view.findViewById<EditText>(R.id.etHabitName)
        val desc = view.findViewById<EditText>(R.id.etHabitDescription)
        val goal = view.findViewById<EditText>(R.id.etHabitGoal)
        val categoryId = view.findViewById<EditText>(R.id.etHabitCategoryId)
        val btn = view.findViewById<Button>(R.id.btnSaveHabit)

        btn.setOnClickListener {
            viewModel.addHabit(
                name.text.toString(),
                desc.text.toString(),
                categoryId.text.toString().toLong(),
                goal.text.toString()
            )
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.success.observe(viewLifecycleOwner) {
            if (it == true) {
                Toast.makeText(requireContext(), "Habit created!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            if (it != null)
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_LONG).show()
        }
    }
}
