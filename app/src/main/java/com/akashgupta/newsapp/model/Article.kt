package com.akashgupta.newsapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "articles"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
) : Serializable

/* Need to click our article, we clicked on to our article fragment so we are able to save it later on
* from the article fragment and for that we will need the SafeArgs(from the Navigation Component library)
* which will enable us to pass arguments to our transitions from ONE FRAGMENT TO ANOTHER because our
* Article class is not a primitive data type like int or float we need to mark this class as Serializable.
* Which tells Kotlin we will to be able to pass this class b/w several fragments with the navigation component.*/