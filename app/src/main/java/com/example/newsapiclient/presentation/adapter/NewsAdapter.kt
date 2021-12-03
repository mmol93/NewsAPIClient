package com.example.newsapiclient.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapiclient.data.model.Article
import com.example.newsapiclient.databinding.NewsListItemBinding

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(){

    // DiffUtil을 사용하여 해당 객체가 어떤 부분이 다른지 확인할 수 있다
    private val callback = object : DiffUtil.ItemCallback<Article>(){
        // 두 객체가 같은 항목인지 확인 -> 고유한 값을 비교해야 알 수 있음
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }
        // 두 항목의 데이터 내용이 동일한지 확인
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
        // -> 즉, 같은 인덱스일 때 값이 같은지 확인하는 것이다
    }

    // AsyncListDiffer: 자체적으로 멀티 스레드 처리가 되어있음
    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class NewsViewHolder(val binding:NewsListItemBinding)
        :RecyclerView.ViewHolder(binding.root){
            fun bind(article: Article){
                binding.tvTitle.text = article.title
                binding.tvDescription.text = article.description
                binding.tvPublishedAt.text = article.publishedAt
                binding.tvSource.text = article.source.name
                Glide.with(binding.ivArticleImage.context)
                    .load(article.urlToImage)
                    .into(binding.ivArticleImage)
            }
        }
}