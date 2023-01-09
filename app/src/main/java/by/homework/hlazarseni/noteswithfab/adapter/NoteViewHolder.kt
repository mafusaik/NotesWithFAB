package by.homework.hlazarseni.noteswithfab.adapter

import androidx.recyclerview.widget.RecyclerView
import by.homework.hlazarseni.noteswithfab.model.Note
import by.homework.hlazarseni.noteswithfab.databinding.ItemNoteBinding

class NoteViewHolder(
    private var binding: ItemNoteBinding,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(note: Note) {
        with(binding) {
            title.text = note.title
            description.text = note.description
            date.text = note.time
        }
    }
}