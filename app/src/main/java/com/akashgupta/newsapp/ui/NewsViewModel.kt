package com.akashgupta.newsapp.ui

import androidx.lifecycle.ViewModel
import com.akashgupta.newsapp.repository.NewsRepository

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {

}