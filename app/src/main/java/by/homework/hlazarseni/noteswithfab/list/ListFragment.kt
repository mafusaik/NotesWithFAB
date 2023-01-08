package by.homework.hlazarseni.noteswithfab.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.homework.hlazarseni.noteswithfab.adapter.NoteAdapter
import by.homework.hlazarseni.noteswithfab.addVerticalGaps
import by.homework.hlazarseni.noteswithfab.database.Note
import by.homework.hlazarseni.noteswithfab.databinding.ListFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : Fragment() {

    private var _binding: ListFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModel<ListViewModel>()

    private val adapter by lazy {
        NoteAdapter(
            onItemClicked = {
                editNote(it)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ListFragmentBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            fab.setOnClickListener {
                val newNote = viewModel.createNote()
                viewModel.insertNote(newNote)
                updateNotesList()
            }

            val linearLayoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false
            )

            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = false
            }

            recyclerView.adapter = adapter
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.addVerticalGaps()

            val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val note = adapter.currentList[position]
                    viewModel.deleteNote(note)

                }
            }
            ItemTouchHelper(itemTouchHelperCallback).apply {
                attachToRecyclerView(binding.recyclerView)
            }
        }
        updateNotesList()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun editNote(currentNote: Note) {
        findNavController().navigate(ListFragmentDirections.toDetailFragment(currentNote.id))
    }

    private fun updateNotesList() {
        viewModel.allNotes.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }
    }
}