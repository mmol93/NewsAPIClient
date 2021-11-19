package com.example.newsapiclient.data.util

// 구글 공식 문서에 있는 네트워크 상태를 알 수 있는 코드 스니펫
// 네트워크 통신 결과에 대한 값을 반환한다
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    // 성공했을 경우 data만 반환한다
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    // 에러가 발생했을 경우 데이터와 함께 메시지를 반환한다
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}