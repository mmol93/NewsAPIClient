package com.example.newsapiclient.presentation.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapiclient.domain.usecase.GetNewsHeadLinesUseCase

// NewsViewModelFactory에서 UI에 보여주기 위한 데이터를 초기화한다
// 즉, DI를 사용하여 News 데이터를 가져와서 여기에 초기화한다
class NewsViewModelFactory(
    private val app: Application,
    private val getNewsHeadLinesUseCase: GetNewsHeadLinesUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app, getNewsHeadLinesUseCase) as T
    }
}