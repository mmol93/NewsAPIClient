package com.example.newsapiclient.domain.repository.dataSource

import com.example.newsapiclient.data.model.APIResponse
import retrofit2.Response

interface NewsRemoteDataSource {
    suspend fun getTopHeadlines(
        country:String,
        page:Int
    ):Response<APIResponse>
}