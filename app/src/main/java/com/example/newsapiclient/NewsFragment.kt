package com.example.newsapiclient

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapiclient.data.util.Resource
import com.example.newsapiclient.databinding.FragmentNewsBinding
import com.example.newsapiclient.presentation.adapter.NewsAdapter
import com.example.newsapiclient.presentation.viewModel.NewsViewModel

class NewsFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var fragmentNewsBinding: FragmentNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    private var country = "kr"
    private var page = 1
    private var isScrolling = false
    private var isLoading = false
    private var isLastPage = false
    private var lastPages = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNewsBinding = FragmentNewsBinding.bind(view)
        // MainActivity에서 사용중인 객체를 그대로 들고온다
        viewModel= (activity as MainActivity).viewModel
        newsAdapter= (activity as MainActivity).newsAdapter
        initRecyclerView()
        viewNewsList()
    }

    private fun initRecyclerView() {
//        newsAdapter = NewsAdapter()
        fragmentNewsBinding.rvNews.apply {
            this.adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@NewsFragment.onScrollListener)
        }
    }

    private fun viewNewsList() {
        viewModel.getNewsHeadLines(country, page)
        viewModel.newsHeadlines.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                        // 검색결과 나온 기사의 갯수가 20개인지 아닌지 확인
                        // totalResults: 모든 기사의 갯수가 나와있음
                        // totalResults가 20으로 나눠진다? = 마지막 페이지임
                        if (it.totalResults%20 == 0){
                            lastPages = it.totalResults/20
                        }
                        // 20개 아님 20으로 나눈거에 +1한게 마지막 페이지임
                        else{
                            lastPages = it.totalResults/20 + 1
                        }
                        isLastPage = page == lastPages
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(activity, "An error occurred: $it", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun showProgressBar() {
        isLoading = true
        fragmentNewsBinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        isLoading = false
        fragmentNewsBinding.progressBar.isGone = true
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            // 현재 recyclerView에서 사용되고 있는 layoutManager를 가져온다
            // layoutManager를 이용하여 다음과 같은 3개의 요소를 가져올 수 있음
            val layoutManager = fragmentNewsBinding.rvNews.layoutManager as LinearLayoutManager
            // 1. 현재 리스트의 size
            val sizeOfTheCurrentList = layoutManager.itemCount
            // 2. 보여지고 있는 item size
            val visibleItemsCount = layoutManager.childCount
            // 3. 보여지고 있는 item 중 첫 번째 item의 위치
            val visibleTopPosition = layoutManager.findFirstVisibleItemPosition()

            // 마지막 페이지에 닿았는지 확인
            val hasReachedToEnd = visibleTopPosition + visibleItemsCount >= sizeOfTheCurrentList

//            Log.d("page", "sizeOfTheCurrentList: $sizeOfTheCurrentList")
//            Log.d("page", "visibleItemsCount: $visibleItemsCount")
//            Log.d("page", "visibleTopPosition: $visibleTopPosition")
//            Log.d("page", "hasReachedToEnd: $hasReachedToEnd")

            // 로딩중이 아님 & 아직 로딩할 수 있는 페이지 남음 & 스크롤이 제일 밑에 부딫힘
            val shouldPaginate = !isLoading && !isLastPage && hasReachedToEnd
            Log.d("page", "shouldPaginate: $shouldPaginate")
            if (shouldPaginate){
                page++
                viewModel.getNewsHeadLines(country, page)
                isScrolling = false
            }
        }
    }
}