package by.homework.hlazarseni.noteswithfab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.homework.hlazarseni.noteswithfab.model.Note
import by.homework.hlazarseni.noteswithfab.databinding.ItemNoteBinding

class NoteAdapter(private val onItemClicked: (Note) -> Unit) :
    ListAdapter<Note, RecyclerView.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NoteViewHolder(
            ItemNoteBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        checkNotNull(holder as NoteViewHolder) { "incorrect viewholder" }
        holder.bind(current)
    }

    companion object {

        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(
                oldItem: Note,
                newItem: Note
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Note,
                newItem: Note
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}