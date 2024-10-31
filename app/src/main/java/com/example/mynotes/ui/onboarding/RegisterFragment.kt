package com.example.mynotes.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mynotes.databinding.RegisterFragmentBinding
import com.example.mynotes.ui.AuthViewModel
import kotlinx.coroutines.launch

class RegisterFragment: Fragment() {

    private val viewmodel by activityViewModels<AuthViewModel>()
    private var _binding: RegisterFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewmodel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewmodel.emailError.flowWithLifecycle(
                lifecycle = viewLifecycleOwner.lifecycle,
                minActiveState = androidx.lifecycle.Lifecycle.State.STARTED
            ).collect{
                binding.email.error = it
            }
        }
        lifecycleScope.launch {
            viewmodel.passwordError.flowWithLifecycle(
                lifecycle = viewLifecycleOwner.lifecycle,
                minActiveState = androidx.lifecycle.Lifecycle.State.STARTED
            ).collect{
                binding.password.error = it
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}