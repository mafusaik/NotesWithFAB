package by.homework.hlazarseni.noteswithfab.detail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import by.homework.hlazarseni.noteswithfab.R
import by.homework.hlazarseni.noteswithfab.database.Note
import by.homework.hlazarseni.noteswithfab.database.NoteDao
import by.homework.hlazarseni.noteswithfab.databinding.DetailFragmentBinding
import by.homework.hlazarseni.noteswithfab.utils.currentDateTime
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailFragment(private val noteDao: NoteDao) : Fragment() {

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val args by navArgs<DetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DetailFragmentBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            lifecycleScope.launch {
                val currentNode = currentNote(args.noteId)
                if (currentNode.title.isNotBlank() && currentNode.description.isNotBlank()) {
                    editTextTitle.setText(currentNode.title)
                    editTextDescription.setText(currentNode.description)
                    date.text = currentNode.date
                }
            }
            toolbar
                .setOnMenuItemClickListener {
                    val id = args.noteId
                    val title = containerTitle.getText()
                    val description = containerDescription.getText()

                    if (it.itemId == R.id.action_save) {
                        lifecycleScope.launch {
                            updateNote(id, title, description)
                        }
                        return@setOnMenuItemClickListener true
                    } else return@setOnMenuItemClickListener false
                }

            toolbar.setupWithNavController(findNavController())

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun TextInputLayout.getText(): String {
        return editText?.text.toString()
    }

    private suspend fun updateNote(noteId: Int, editTitle: String, editDescription: String) =
        withContext(
            Dispatchers.IO
        ) {
            val note = Note(
                id = noteId,
                title = editTitle,
                description = editDescription,
                date = currentDateTime()
            )
            runCatching {
                noteDao.update(note)
            }
        }

    private suspend fun currentNote(noteId: Int): Note = withContext(Dispatchers.IO) {
        return@withContext noteDao.getNote(noteId)
    }
}