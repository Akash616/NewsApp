package com.akashgupta.newsapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.akashgupta.newsapp.R
import com.akashgupta.newsapp.databinding.ActivityNewsBinding
import com.akashgupta.newsapp.db.ArticleDatabase
import com.akashgupta.newsapp.repository.NewsRepository

class NewsActivity : AppCompatActivity() {

    private var _binding: ActivityNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        //connect bottomNavigationView with Navigation components
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navigationHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        /* Whenever we request with retrofit we will get the answer as a JSON string,
        * JSON is just a way to convert complex objects in a simple string that you can
        * send over the network and then extract the complex object again.*/

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}