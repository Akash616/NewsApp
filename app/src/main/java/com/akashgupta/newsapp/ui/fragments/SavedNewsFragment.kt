package com.akashgupta.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akashgupta.newsapp.R
import com.akashgupta.newsapp.databinding.FragmentBreakingNewsBinding
import com.akashgupta.newsapp.databinding.FragmentSavedNewsBinding
import com.akashgupta.newsapp.ui.NewsActivity
import com.akashgupta.newsapp.ui.NewsViewModel

class SavedNewsFragment : Fragment() {

    private var _binding: FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //we have to access to the viewModel created in the NewsActivity
        viewModel = (activity as NewsActivity).viewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}