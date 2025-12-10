package com.example.myapplication.ui.auth

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.utils.SessionManager

class LoginFragment : Fragment() {
    // Binding reference (cleared when view is destroyed)
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate binding
        _binding = FragmentLoginBinding.inflate(inflater, container,
            false)
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPass.text.toString()
            Log.d("LoginFragment", "Email: $email, Password: $password")
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Email and Password are required",
                    Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(email, password)
            }
        }

        viewModel.authResult.observe(viewLifecycleOwner) { event ->
            // 🔹 JAVÍTÁS: Kezeld le az eseményt!
            event.getContentIfNotHandled()?.let { result ->
                // A kódod többi része változatlan, de most már csak egyszer fog lefutni!
                Log.d("LoginFragment", "authResult: $result")
                result.onSuccess { authResponse ->
                    Toast.makeText(requireContext(), "Welcome " + (authResponse.user.email ?: ""), Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }.onFailure { error ->
                    Toast.makeText(requireContext(), "Login failed: ${error.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Avoid memory leaks
        _binding = null
    }
}