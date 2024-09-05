package com.akashgupta.newsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.akashgupta.newsapp.model.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    /* Database classes for room always need to be abstract */

    abstract fun getArticleDao(): ArticleDao

    //to create our real database
    companion object {

        @Volatile //other threads can immediately see when a thread changes this instance
        private var instance: ArticleDatabase? = null
        //we really make sure that there is only a Single instance of our database at once
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db_db"
            ).build()

    }

}