package com.katyrin.testappache.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.katyrin.testappache.R
import com.katyrin.testappache.databinding.FragmentHomeBinding
import com.katyrin.testappache.utils.toast
import com.katyrin.testappache.view.adapter.RecyclerHomeAdapter
import com.katyrin.testappache.viewmodel.AppState
import com.katyrin.testappache.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var model: HomeViewModel
    private var binding: FragmentHomeBinding? = null
    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentHomeBinding.inflate(inflater, container, false)
            .also { binding = it }
            .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.main_container)
        iniViewModel()
        initAdapter()
        model.getSavedProjects()
    }

    private fun initAdapter() {
        binding?.recyclerView?.adapter = RecyclerHomeAdapter { contentData ->
            val direction = HomeFragmentDirections
                .actionHomeFragmentToDrawingFragment(contentData.id)
            navController?.navigate(direction)
        }
    }

    private fun iniViewModel() {
        val viewModel: HomeViewModel by viewModel()
        model = viewModel
        model.subscribe().observe(viewLifecycleOwner) { renderData(it) }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success ->
                (binding?.recyclerView?.adapter as RecyclerHomeAdapter).submitList(appState.data)
            is AppState.Error -> toast(appState.message)
            is AppState.Loading -> toast(getString(R.string.loading))
        }
    }

    override fun onDestroy() {
        binding = null
        navController = null
        super.onDestroy()
    }
}