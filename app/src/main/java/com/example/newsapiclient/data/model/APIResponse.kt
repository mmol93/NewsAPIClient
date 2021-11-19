package com.example.newsapiclient.data.model


import com.google.gson.annotations.SerializedName

// API 통신 결과에 대한 클래스
data class APIResponse(
    @SerializedName("articles")
    val articles: List<Article>,
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int
)