package com.example.newsapiclient.presentation.di

import com.example.newsapiclient.data.api.NewsAPIService
import com.example.newsapiclient.domain.repository.dataSource.NewsRemoteDataSource
import com.example.newsapiclient.domain.repository.dataSource.dataSourceImpl.NewsRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteDataSourceModule {
    @Singleton
    @Provides
    // 이 함수는 NewsAPIService를 매개변수로 받음
    // 그 이유는 NewsRemoteDataSourceImpl 클래스에서 매개변수로 받기 때문
    // 매개변수로 NewAPIService를 받으면 provideNewsAPIService에서 반환한 값이
    // 여기의 매개변수로 들어간다
    fun provideNewsRemoteDataSource(newsAPIService: NewsAPIService)
    :NewsRemoteDataSource{
        return NewsRemoteDataSourceImpl(newsAPIService)
    }
}