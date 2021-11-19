package com.example.newsapiclient.domain.repository

import com.example.newsapiclient.data.model.APIResponse
import com.example.newsapiclient.data.model.Article
import com.example.newsapiclient.data.util.Resource
import kotlinx.coroutines.flow.Flow


interface NewsRepository {
    suspend fun getNewsHeadlines(): Resource<APIResponse>
    suspend fun getSearchedNews(searchQuery: String): Resource<APIResponse>
    suspend fun saveNews(article: Article)
    suspend fun deleteNews(article: Article)
    // 데이터 베이스에서 Flow 형태로 데이터를 꺼낼거임
    // Flow형태로 꺼낸 데이터를 liveData로 emit()할 수 있다
    fun getSavedNews(): Flow<List<Article>>
}