// .../ui/profile/ProfileFragment.kt
package com.example.myapplication.ui.profile
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // ViewModel inicializálása a Factory-val
    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // LiveData figyelése a profiladatokhoz
        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            binding.usernameText.text = profile.username
            binding.emailText.text = profile.email
            binding.descriptionText.text = profile.description ?: "Nincs leírás megadva."

            // Base64 string dekódolása és beállítása képként
            profile.profileImageBase64?.let { base64String ->
                try {
                    val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
                    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    binding.profileImage.setImageBitmap(decodedImage)
                } catch (e: IllegalArgumentException) {
                    Log.e("ProfileFragment", "Hibás Base64 string: ", e)
                    // Itt beállíthatsz egy alapértelmezett képet hiba esetén
                    binding.profileImage.setImageResource(R.drawable.ic_default_profile)
                }
            }
        }

        // LiveData figyelése a hibaüzenetekhez
        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show()
        }

        // LiveData figyelése a kijelentkezés utáni navigációhoz
        viewModel.navigateToLogin.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                // Navigálás a LoginFragment-re és a back stack törlése
                findNavController().navigate(R.id.action_global_to_loginFragment)
            }
        }

        // Kijelentkezés gomb eseménykezelője
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
