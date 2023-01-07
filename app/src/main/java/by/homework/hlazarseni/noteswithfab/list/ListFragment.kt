package by.homework.hlazarseni.noteswithfab.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.homework.hlazarseni.noteswithfab.NoteAdapter
import by.homework.hlazarseni.noteswithfab.R
import by.homework.hlazarseni.noteswithfab.addVerticalGaps
import by.homework.hlazarseni.noteswithfab.databinding.ListFragmentBinding
import org.koin.android.ext.android.inject

class ListFragment : Fragment() {

    private var _binding: ListFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by inject<ListViewModel>()

    private val adapter by lazy {
        NoteAdapter(
            onItemClicked = {
                editNote()
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
                Toast.makeText(requireContext(), "hello", Toast.LENGTH_LONG)
                    .show()
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

    private fun editNote() {
        view?.findNavController()?.navigate(R.id.to_detail_fragment)
    }

    private fun updateNotesList() {
        viewModel.allCats.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }
    }
}