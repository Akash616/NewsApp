package com.akashgupta.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.akashgupta.newsapp.databinding.ItemArticlePreviewBinding
import com.akashgupta.newsapp.model.Article
import com.bumptech.glide.Glide

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    /* Normally if you had a recyclerview adapter -
    * class NewsAdapter(
        list: List<Article>
    ) -> then you add it to the list and you call adapter.notifyDataSetChange
    * But that is very INEFFICIENT Because by using notifyDataSetChanges recyclerview
    * adapter always update its whole items even the items didn't change and to
    * Solve that problem we can use -> DiffUtil(Calculates differences b/w 2 lists and update
    * those items that different. It will actually happen in background so we don't block
    * our Main Thread)*/

    inner class ArticleViewHolder(val binding: ItemArticlePreviewBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            //return oldItem.id == newItem.id bec articles from our API and they don't have id by default
            //we only use that id for our local database.
            return oldItem.url == newItem.url //bec url unique to each article
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    //Tool that will take our 2 lists and compares and calculate the differences and run in background.
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.binding.apply {
            Glide.with(this.root).load(article.urlToImage).into(ivArticleImage)
            tvSource.text = article.source?.name
            tvTitle.text = article.title
            tvDescription.text = article.description
            tvPublishedAt.text = article.publishedAt
            root.setOnClickListener {
                onItemClickListener?.let { //refer to itemView
                    it(article)
                }
            }
        }
    }

    //click on article - fun under fun and return Unit(return nothing)
    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

}