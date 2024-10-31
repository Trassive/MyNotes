package com.example.mynotes

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.mynotes.databinding.ActivityMainBinding
import com.example.mynotes.ui.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private val authViewModel by viewModels<AuthViewModel>()

    private lateinit var menubar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navController = findNavController(R.id.nav_host_fragment_content_main)
        setUpToolbar()
        observeAuthState()

    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)

        supportActionBar?.title = "My Notes"

        binding.fab.setOnClickListener {
            navController.navigate(R.id.action_notesGridFragment_to_noteFragment)
        }
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.onboarding_nav_graph, R.id.notes_nav_graph
            )
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.hierarchy.any { it.id == R.id.notes_nav_graph }) binding.appBarLayout.setExpanded(true)
        }
        binding.toolbar.setupWithNavController(
            navController = navController,
            configuration = appBarConfiguration
        )
    }

    private fun observeAuthState() {
        lifecycleScope.launch{
            authViewModel.authState.flowWithLifecycle(
                    lifecycle = lifecycle,
                    minActiveState = androidx.lifecycle.Lifecycle.State.STARTED
                )
                .collect { user ->
                if (user == null) {
                    binding.fab.visibility = View.GONE
                    binding.toolbar.visibility = View.GONE
                    if(navController.currentDestination?.id!= R.id.registerFragment){
                        navController.navigate(R.id.registerFragment) {
                            popUpTo(R.id.notes_nav_graph) {
                                inclusive = true
                            }
                        }
                    }
                } else {
                    navController.navigate(R.id.action_onboarding_to_main)
                    binding.fab.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE
                }
            }
        }
    }

}