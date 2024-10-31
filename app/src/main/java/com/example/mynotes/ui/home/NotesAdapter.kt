package com.example.mynotes.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.R
import com.example.mynotes.data.local.Note
import com.example.mynotes.databinding.NotesItemBinding
import java.time.format.DateTimeFormatter


class NotesAdapter(private val onItemClick: (Note) -> Unit, private val onLongItemClick: (Note) -> Unit, private val isSelected: (Note) -> Boolean
) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private val differ = AsyncListDiffer(this, NotesDiffCallback())
    fun submitList(list: List<Note>) {
        differ.submitList(list)
    }

    inner class NotesViewHolder(binding: NotesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val title: TextView = binding.title
        private val content: TextView = binding.content
        private val date: TextView = binding.date

        init {
            itemView.setOnClickListener{
                onItemClick(differ.currentList[adapterPosition])
            }
            itemView.setOnLongClickListener {
                onLongItemClick(differ.currentList[adapterPosition])
                true
            }


        }
        fun bind(note: Note) {
            title.text = note.title
            content.text = note.content
            date.text = note.lastModified.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
            setSelected(isSelected(note))
        }
        private fun setSelected(isSelected: Boolean) {
            if (isSelected) {
                itemView.background = ContextCompat.getDrawable(itemView.context, R.drawable.selected_shape)
                itemView.alpha = 0.73f

            } else {
                itemView.background = ContextCompat.getDrawable(itemView.context, R.drawable.unselected_shape)
                itemView.alpha = 1f
            }
        }
    }

    private inner class NotesDiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem && isSelected(oldItem) == isSelected(newItem)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NotesItemBinding.inflate(inflater, parent, false)
        return NotesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = differ.currentList[position]
        holder.bind(note)
    }
    fun getPosition(note: Note): Int {
        return differ.currentList.indexOf(note)
    }
}
