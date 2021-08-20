package com.katyrin.testappache.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.katyrin.testappache.databinding.FragmentHomeBinding
import com.katyrin.testappache.view.adapter.RecyclerHomeAdapter
import com.katyrin.testappache.viewmodel.AppState
import com.katyrin.testappache.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var model: HomeViewModel
    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentHomeBinding.inflate(inflater, container, false)
            .also { binding = it }
            .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniViewModel()
        binding?.recyclerView?.adapter = RecyclerHomeAdapter {
            Toast.makeText(requireContext(), it.name, Toast.LENGTH_SHORT).show()
        }
        model.getSavedProjects()
    }

    private fun iniViewModel() {
        val viewModel: HomeViewModel by viewModel()
        model = viewModel
        model.subscribe().observe(viewLifecycleOwner) { renderData(it) }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success ->
                (binding?.recyclerView?.adapter as RecyclerHomeAdapter).updateData(appState.data)
            is AppState.Error ->
                Toast.makeText(requireContext(), appState.message, Toast.LENGTH_SHORT).show()
            is AppState.Loading ->
                Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}