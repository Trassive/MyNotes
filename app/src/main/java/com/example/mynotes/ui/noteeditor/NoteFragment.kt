package com.example.mynotes.ui.noteeditor

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mynotes.databinding.NoteFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class NoteFragment : Fragment() {
    private var _binding: NoteFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NoteViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NoteFragmentBinding.inflate(inflater, container, false)
        _binding?.lifecycleOwner = viewLifecycleOwner
        _binding!!.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.noteTitle.setOnClickListener {
            binding.noteTitle.apply{

                isFocusableInTouchMode = true
                isFocusable = true
                requestFocus()
            }
        }
        binding.noteContent.setOnClickListener {
            binding.noteContent.apply{

                isFocusableInTouchMode = true
                isFocusable = true
                requestFocus()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
