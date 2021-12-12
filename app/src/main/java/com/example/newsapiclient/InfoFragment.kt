package com.example.newsapiclient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.newsapiclient.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {
    private lateinit var fragmentInfoBinding : FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentInfoBinding = FragmentInfoBinding.bind(view)
        // nav_graph에서 InfoFragment에 Arguments를 설정했기 때문에
        // InfoFragmentArgs를 사용할 수 있음
        val args : InfoFragmentArgs by navArgs()
        // selectedArticle: nav_graph에서 argument로 설정한 id
        val article = args.selectedArticle
        fragmentInfoBinding.webViewInfo.apply {
            webViewClient = WebViewClient()
            if (article.url != ""){
                loadUrl(article.url)
            }
        }
    }
}