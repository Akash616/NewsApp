package com.akashgupta.newsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akashgupta.newsapp.NewsApplication
import com.akashgupta.newsapp.model.Article
import com.akashgupta.newsapp.model.NewsResponse
import com.akashgupta.newsapp.repository.NewsRepository
import com.akashgupta.newsapp.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class NewsViewModel(
    app: Application,
    val newsRepository: NewsRepository
) : AndroidViewModel(app) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null //for pagination

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null //for pagination

    init {
        getBreakingNews("us")
    }

    //viewModelScope -> make sure that this coroutine is stays only alive as long as our viewModel is alive.
    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    private fun handleBreakingNewsResource(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null){
                    breakingNewsResponse = resultResponse
                } else {
                    //List<Article> -> MutableList<Article> to add articles
                    val oldArticles = breakingNewsResponse?.articles //null check
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
                //if breakingNewsResponse is null then resultResponse
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResource(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchNewsResponse == null){
                    searchNewsResponse = resultResponse
                } else {
                    //List<Article> -> MutableList<Article> to add articles
                    val oldArticles = searchNewsResponse?.articles //null check
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
                //if breakingNewsResponse is null then resultResponse
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResource(response))
            } else {
                breakingNews.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable){
            //If getBreakingNews() fun throws an exception that can happen
            when(t){
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion ERROR")) //MEANS CONVERSION FROM JSON TO KOTLIN CLASS OBJECT FAILED
            }
        }
    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResource(response))
            } else {
                searchNews.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable){
            //If getBreakingNews() fun throws an exception that can happen
            when(t){
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion ERROR")) //MEANS CONVERSION FROM JSON TO KOTLIN CLASS OBJECT FAILED
            }
        }
    }

    //ViewModel -> AndroidViewModel why? is just the same as ViewModel but inside AndroidViewModel we
    //use the application context. AndroidViewModel(application) -> NewsApplication
    private fun hasInternetConnection(): Boolean {
        //getApplication<>() -> that fun is only available in AndroidViewModel not in the normal ViewModel
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}