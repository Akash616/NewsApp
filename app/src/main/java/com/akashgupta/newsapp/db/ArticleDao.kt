package com.akashgupta.newsapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akashgupta.newsapp.model.Article

@Dao
interface ArticleDao {

    /*DAO - Data Access Object, it's interface we will defines the functions that access
    * our local database(save article in db, read articles, delete and so on)*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    // this fun won't be a suspend bec it will return live data object that
    // doesn't work with suspend function.
    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

}