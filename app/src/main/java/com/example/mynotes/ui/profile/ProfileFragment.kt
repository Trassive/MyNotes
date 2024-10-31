package com.example.mynotes.ui.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mynotes.R
import com.example.mynotes.databinding.ProfileFragmentBinding
import kotlinx.coroutines.launch

class ProfileFragment: Fragment(){
    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel>()

    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {image->
            viewModel.updateProfile(image)
        }
    }
    private val getPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            getImage.launch("image/*")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().addMenuProvider(
            object:MenuProvider{
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.profile_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    when(menuItem.itemId){
                        R.id.edit_profile -> {
                            menuItem.isVisible = false
                            val newItem = requireActivity().findViewById<View>(R.id.reset) as MenuItem
                            newItem.isVisible = true
                        }
                        R.id.reset -> {
                            viewModel.reset()
                            menuItem.isVisible = false
                            val newItem = requireActivity().findViewById<View>(R.id.edit_profile) as MenuItem
                            newItem.isVisible = true
                        }
                    }
                    viewModel.toggleEditing()
                    return true
                }
            }
        )
        lifecycleScope.launch {
            viewModel.emailError.flowWithLifecycle(
                lifecycle = lifecycle,
                minActiveState = Lifecycle.State.STARTED
            ).collect{
                binding.email.error = it
            }
        }
        binding.cameraIcon.setOnClickListener{

        }
    }

    private fun imagePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(this.requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                getImage.launch("image/*")
            } else{
                getPermission.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            if (checkSelfPermission(this.requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                getImage.launch("image/*")
            } else {
                getPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }



    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}