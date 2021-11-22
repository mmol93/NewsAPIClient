package com.example.newsapiclient.data.api

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsAPIServiceTest {
    private lateinit var service: NewsAPIService
    private lateinit var server: MockWebServer

    // @Before: @Test가 동작하기 전에 실시한다
    @Before
    fun setUp() {
        server = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsAPIService::class.java)
    }

    private fun enqueueMockResponse(fileName: String) {
        // stream 형태로 Json 파일 데이터를 가져온다
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        // 가져운 stream 데이터를 메모리 buffer에 넣는다
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        // stream 데이터를 사용하여 Reponse의 body를 생성한다
        mockResponse.setBody(source.readString(Charsets.UTF_8))
        // 서버에 Response에 대한 데이터를 넣는다
        server.enqueue(mockResponse)
    }

    @Test
    fun getTopHeadlines_sentRequest_receivedExpected() {
        runBlocking {
            enqueueMockResponse("newsresponse.json")
            val responseBody = service.getTopHeadlines("us", 1).body()
            val request = server.takeRequest()
            assertThat(responseBody).isNotNull()
            // request.path: json 파일이 있는 url과 같은 값
            assertThat(request.path)
                .isEqualTo("/v2/top-headlines?country=us&page=1&apikey=581a6c99767b415aba3ecaa99a27c6a6")
        }
    }

    @Test
    fun getTopHeadlines_receivedResponse_correctPageSize() {
        runBlocking {
            enqueueMockResponse("newsresponse.json")
            val responseBody = service.getTopHeadlines("us", 1).body()
            val articlesList = responseBody!!.articles
            // 하나의 page에는 20개의 기사가 있다
            assertThat(articlesList.size).isEqualTo(20)
        }
    }

    @Test
    fun getTopHeadlines_receivedResponse_correctContent() {
        runBlocking {
            enqueueMockResponse("newsresponse.json")
            val responseBody = service.getTopHeadlines("us", 1).body()
            val articlesList = responseBody!!.articles
            val article = articlesList[0]
            assertThat(article.author)
                .isEqualTo("Dakin Andone, Keith Allen and David Williams, CNN")
            assertThat(article.url)
                .isEqualTo("https://www.cnn.com/2021/11/20/us/atlanta-airport-scare/index.html")
            assertThat(article.publishedAt)
                .isEqualTo("2021-11-21T00:24:00Z")
        }
    }


    // @After: @Test가 동작한 이후에 실시한다
    @After
    fun tearDown() {
        server.shutdown()
    }
}