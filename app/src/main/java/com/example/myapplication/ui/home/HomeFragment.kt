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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.repository.ScheduleRepository
import java.time.LocalDate

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: HomeScheduleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the ViewModel using the custom factory
        val factory = HomeViewModelFactory(requireContext())
        viewModel = ViewModelProvider(owner = this, factory = factory)
        [HomeViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupObservers()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupUi() {
        // Setup RecyclerView and adapter
        adapter = HomeScheduleAdapter()
        binding.rvSchedules.layoutManager =
            LinearLayoutManager(requireContext())
        binding.rvSchedules.adapter = adapter
        // Add a divider between list items

        binding.rvSchedules.addItemDecoration(DividerItemDecoration(requireContext
            (), LinearLayoutManager.VERTICAL))
        // Fetch today's schedules (format YYYY-MM-DD)
        val today = try { LocalDate.now().toString() } catch (_:Exception) { "2025-10-26" }
        viewModel.getScheduleByDay(today)

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
}}