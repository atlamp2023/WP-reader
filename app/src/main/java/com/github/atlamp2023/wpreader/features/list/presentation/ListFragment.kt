package com.github.atlamp2023.wpreader.features.list.presentation

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.atlamp2023.wpreader.R
import com.github.atlamp2023.wpreader.core.ShareViewModel
import com.github.atlamp2023.wpreader.core.util.Result
import com.github.atlamp2023.wpreader.core.util.State
import com.github.atlamp2023.wpreader.core.util.share
import com.github.atlamp2023.wpreader.databinding.FragmentListBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

enum class  ScrollDirection(val value: Int) {
    UP(-1), DOWN(1)
}

class ListFragment : Fragment(R.layout.fragment_list) {

    private lateinit var binding: FragmentListBinding

    private var listener = object: ListAdapter.Listener {
        override fun clickItem(data: ListItem, pos: Int) {
            viewModel.handleUserAction(
                UserActionOnList.ClickOnItem( pos )
            )
            var title = data.title ?: ""
            title = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT).toString()
            } else {
                Html.fromHtml(title).toString()
            }
            val args = Bundle().apply {
                putInt("id", data.id ?: -1)
                putString("title", title)
            }
            findNavController().navigate(R.id.action_listFragment_to_detailFragment, args)
        }
    }
    private var adapter = ListAdapter(listener)
    private lateinit var scrollListener: CustomScrollListener
    
    private lateinit var stateVM: ShareViewModel
    private lateinit var state: State

    private val viewModel: ListViewModel by lazy {
        getViewModel (parameters = { parametersOf(state) } )
    }

    inner class CustomScrollListener: RecyclerView.OnScrollListener(){
        private val UP = 0
        private val DOWN = 1
        private val handler = Handler(Looper.getMainLooper())
        private val lm = binding.rvList.layoutManager as LinearLayoutManager

        private var firstIP = 0
        private var lastIP = 0
        private var direction: Int = 0

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView!!, dx, dy)
            direction = if (dy > 0) { UP } else { DOWN }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (stateVM.state.value == State.REMOTE && newState == RecyclerView.SCROLL_STATE_IDLE){
                firstIP = lm.findFirstCompletelyVisibleItemPosition()
                lastIP = lm.findLastCompletelyVisibleItemPosition()
                if (adapter.list.size - lastIP <= 1 && direction == UP){
                    if (!viewModel.isCurrentPageEqualMaxPage()) {
                        showBtnForWhile(binding.downBtn)
                    }
                }

                if (firstIP < 1 && direction == DOWN){
                    if (!viewModel.isCurrentPageEqualStartPage()) {
                        showBtnForWhile(binding.upBtn)
                    }
                }
            }
            super.onScrollStateChanged(recyclerView, newState)
        }

        private fun showBtnForWhile(btn: Button, delay: Long = 1500L) {
            btn.visibility = View.VISIBLE
            handler.postDelayed({ btn.visibility = View.INVISIBLE }, delay)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stateVM = share().shareVM
        state = stateVM.state.value ?: State.LOCAL
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentListBinding.bind(view)

        binding.rvList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ListFragment.adapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        scrollListener = CustomScrollListener()
        binding.rvList.addOnScrollListener(scrollListener)

        binding.upBtn.setOnClickListener {
            viewModel.handleUserAction(
                UserActionOnList.ScrollList(ScrollDirection.UP)
            )
        }

        binding.downBtn.setOnClickListener {
            viewModel.handleUserAction(
                UserActionOnList.ScrollList(ScrollDirection.DOWN)
            )
        }

        stateVM.state.observe(viewLifecycleOwner) {
            viewModel.handleUserAction(
                UserActionOnList.SwitchState(it)
            )
        }

        viewModel.list.observe(viewLifecycleOwner) {
            updateUI( it )
        }

    }

    private fun updateUI(data: Result<ResultList>?) {
        if(data == null) return
        when(data){
            is Result.Pending -> {
                binding.rvList.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.VISIBLE
            }
            is Result.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.rvList.visibility = View.VISIBLE
                adapter.list = data.value as List<ListItem>
                binding.rvList.scrollToPosition(viewModel.currentPosition)
            }
            is Result.Empty -> {
                binding.progressBar.visibility = View.GONE
                binding.rvList.visibility = View.VISIBLE
                adapter.list = emptyList()
                val toast = Toast.makeText(context, "Empty", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
            is Result.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.rvList.visibility = View.VISIBLE
                val toast = Toast.makeText(context, "Error: ${data.error}", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        }
    }
}