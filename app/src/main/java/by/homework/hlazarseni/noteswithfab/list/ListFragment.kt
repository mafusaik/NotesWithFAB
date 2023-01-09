package by.homework.hlazarseni.noteswithfab.list


import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.homework.hlazarseni.noteswithfab.model.Lce
import by.homework.hlazarseni.noteswithfab.R
import by.homework.hlazarseni.noteswithfab.adapter.NoteAdapter
import by.homework.hlazarseni.noteswithfab.addVerticalGaps
import by.homework.hlazarseni.noteswithfab.model.Note
import by.homework.hlazarseni.noteswithfab.databinding.ListFragmentBinding
import by.homework.hlazarseni.noteswithfab.utils.currentDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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

            viewLifecycleOwner.lifecycleScope.launch {
                progressHorizontal.isVisible = true
                progress.isVisible = true
                progressLoading()

            }
            viewLifecycleOwner.lifecycleScope.launch {
                updateTimeNotes()
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {

                    updateNoteListTest()
                }
            }

            fab.setOnClickListener {
                val newNote = viewModel.createNote()
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.insertNote(newNote)
                }
                updateNotesList()
            }

            val linearLayoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false
            )

            swipeRefresh.setOnRefreshListener {
                updateNotesList()
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
                    lifecycleScope.launch {
                        viewModel.deleteNote(note)
                    }
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

    private suspend fun progressLoading() {
        for (i in 1..10) {
            delay(500)
            binding.progressHorizontal.progress += 10
        }
    }

    private fun updateTimeNotes() {
        viewModel.allNotes.observe(this.viewLifecycleOwner) { notes ->
            notes.forEach() {
                if (it.date != currentDate()) {
                    lifecycleScope.launch {
                        viewModel.updateNote(it.id, it.title, it.description, it.date, it.date)
                    }
                }
            }
        }
    }

    private fun updateNotesList() {
        viewModel.allNotes.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }
    }

    private fun updateNoteListTest() {
        with(binding) {
            viewModel.apiFlow
                .onEach {
                    when (it) {
                        is Lce.Loading -> {}
                        is Lce.Content -> {
                            progress.isVisible = false
                            progressHorizontal.isVisible = false
                            adapter.submitList(it.data)
                        }
                        is Lce.Error -> {
                            val toast = Toast.makeText(
                                requireContext(),
                                getString(R.string.error_data),
                                Toast.LENGTH_SHORT
                            )
                            toast.setGravity(Gravity.CENTER, 0, 0)
                            toast.show()
                            progress.isVisible = false
                            progressHorizontal.isVisible = false
                        }
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycle.coroutineScope)
        }
    }
}