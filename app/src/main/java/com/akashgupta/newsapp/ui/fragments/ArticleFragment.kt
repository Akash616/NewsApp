package com.akashgupta.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.akashgupta.newsapp.databinding.FragmentArticleBinding
import com.akashgupta.newsapp.ui.NewsActivity
import com.akashgupta.newsapp.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: NewsViewModel
    private val TAG =  "ArticleFragment"

    /*This is a class that Navigation Components generated for us(navigation -> app:argType)*/
    private val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //we have to access to the viewModel created in the NewsActivity
        viewModel = (activity as NewsActivity).viewModel

        val article = args.article

        binding.webView.apply {
            //page will load inside this webview and don't load standard browser of the phone
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }

        binding.fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}