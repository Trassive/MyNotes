package com.example.mynotes.ui.home

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mynotes.R
import com.example.mynotes.data.local.Note
import com.example.mynotes.databinding.NotesGridFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesGridFragment: Fragment() {

    private var _binding: NotesGridFragmentBinding? = null
    private val binding get() = _binding!!

    private var actionMode: ActionMode? = null
    private lateinit var notesAdapter: NotesAdapter


    private val actionModeCallback = object: ActionMode.Callback{
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.selected_menu, menu)
            mode?.title = "1 selected"
            return true
        }

        override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
            return true
        }


        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
           viewModel.deleteSelectedItems()
            actionMode?.finish()
            return true

        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
            viewModel.toggleSelectedState()
            removeAllSelection()
        }
    }

    private val viewModel: NotesGridViewModel by viewModels<NotesGridViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NotesGridFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        observeSelectionState()
    }

    private fun observeSelectionState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isSelectionModeActive.flowWithLifecycle(
                lifecycle = viewLifecycleOwner.lifecycle,
                minActiveState = Lifecycle.State.STARTED
            ).collect { isSelectionModeActive ->
                if (isSelectionModeActive) {
                    actionMode = requireActivity().startActionMode(actionModeCallback)
                } else {
                    actionMode?.finish()
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        notesAdapter = NotesAdapter (
            onItemClick = { note ->
                if(!viewModel.isSelectionModeActive.value){
                    findNavController().navigate(
                        R.id.action_notesGridFragment_to_noteFragment,
                        Bundle().apply {
                            putInt("noteId", note.id)
                        }
                    )
                } else{
                    viewModel.onItemClick(note)
                    updateItemState(note)
                }
            },
            onLongItemClick = { note ->
                if(!viewModel.isSelectionModeActive.value)  viewModel.toggleSelectedState()

                viewModel.onItemClick(note)
                updateItemState(note)

            },
            isSelected = { note ->
                viewModel.isSelected(note)
            }
        )


        binding.notesGrid.addItemDecoration(MarginItemDecoration(8.toPx(), spanCount = 2))

        binding.notesGrid.adapter = notesAdapter
        binding.notesGrid.layoutManager = GridLayoutManager(requireContext(), 2)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.flowWithLifecycle(
                lifecycle = viewLifecycleOwner.lifecycle,
                minActiveState = Lifecycle.State.STARTED
            ).collect { notes ->
                notesAdapter.submitList(notes)
            }
        }
    }
    private fun updateItemState(note: Note) {
        val position = notesAdapter.getPosition(note)
        if(position!= -1){
            notesAdapter.notifyItemChanged(position)
        }
    }
    private fun removeAllSelection() {
        viewModel.selectedItems.value.forEach {
            updateItemState(it)
        }
        viewModel.clearSelection()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
fun Int.toPx():Int{
    return (this* Resources.getSystem().displayMetrics.density).toInt()
}
