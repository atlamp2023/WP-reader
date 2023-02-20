package com.github.atlamp2023.wpreader.features.detail.presentation

import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.atlamp2023.wpreader.R
import com.github.atlamp2023.wpreader.core.ShareViewModel
import com.github.atlamp2023.wpreader.TAG
import com.github.atlamp2023.wpreader.core.sources.BASE_URL
import com.github.atlamp2023.wpreader.databinding.FragmentDetailBinding
import com.github.atlamp2023.wpreader.core.util.Result
import com.github.atlamp2023.wpreader.core.util.State
import com.github.atlamp2023.wpreader.core.util.share
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private lateinit var binding: FragmentDetailBinding

    private lateinit var stateVM: ShareViewModel
    private var state: State = State.LOCAL

    private val viewModel: DetailViewModel by lazy {
        getViewModel (parameters = { parametersOf(state) } )
    }

    inner class MyClient : WebViewClient() {
        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, er: SslError) {
            handler.proceed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        stateVM = share().shareVM
        stateVM.state.value?.let { state = it }
        arguments?.getInt("id")?.let { viewModel.update(state, it) }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.getItem(0).isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailBinding.bind(view)
        binding.webView.setWebViewClient(MyClient())
        val webSettings = binding.webView.getSettings()
        webSettings.javaScriptEnabled = true
        webSettings.loadWithOverviewMode = true

        viewModel.detail.observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })

    }

    private fun updateUI(data: Detail){
        when(data){
            is Result.Pending -> {
                binding.mainGroup.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.VISIBLE
            }
            is Result.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.mainGroup.visibility = View.VISIBLE
                try {
                    //binding.title.text = data.value.title ?: ""
                    binding.webView.loadDataWithBaseURL(BASE_URL, data.value.content ?: "", "text/html; charset=utf-8", "UTF-8", null);
                } catch (e: Exception){
                    Log.d(TAG, "$e, ${e.stackTrace}")
                }
            }
            is Result.Empty -> {
                binding.progressBar.visibility = View.GONE
                binding.mainGroup.visibility = View.VISIBLE
                val toast = Toast.makeText(context, "Empty", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
            is Result.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.mainGroup.visibility = View.VISIBLE
                val toast = Toast.makeText(context, "Error: ${data.error}", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        }
    }
}