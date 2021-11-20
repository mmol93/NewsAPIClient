package com.example.newsapiclient.domain.repository

import com.example.newsapiclient.data.model.APIResponse
import com.example.newsapiclient.data.model.Article
import com.example.newsapiclient.data.util.Resource
import com.example.newsapiclient.domain.repository.dataSource.NewsRemoteDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class NewsRepositoryImpl(private val newsRemoteDataSource: NewsRemoteDataSource) : NewsRepository {
    override suspend fun getNewsHeadlines(): Resource<APIResponse> {
        // responseToResource()로 통신 결과를 확인한다
        return responseToResource(newsRemoteDataSource.getTopHeadlines())
    }

    // 우리가 만든 Resource 클래스를 사용하여 통신 결과를 반환하도록 만든다
    // Response: retrofit 클래스로 통신 결과를 반환해주는 클래스다
    private fun responseToResource(response: Response<APIResponse>):Resource<APIResponse>{
        // 통신결과가 성공적이라면
        if (response.isSuccessful){
            // response.body(): 성공한 내용
            response.body()?.let {result ->
                // 즉, 성공한 내용을 Resource의 Success에 넣어서 반환하게 한다
                return Resource.Success(result)
            }
        }
        // response.message(): 통신 결과 에러일 떄 에러 메시지를 포함함
        // 즉, 이를 Error로써 반환함
        return Resource.Error(response.message())
    }

    override suspend fun getSearchedNews(searchQuery: String): Resource<APIResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun saveNews(article: Article) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNews(article: Article) {
        TODO("Not yet implemented")
    }

    override fun getSavedNews(): Flow<List<Article>> {
        TODO("Not yet implemented")
    }
}