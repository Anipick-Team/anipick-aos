package jpark.bro.data.remote

class AuthInterceptor {
}
//class AuthInterceptor @Inject constructor(
//    private val tokenManager: TokenManager
//) : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val originalRequest = chain.request()
//
//        // 인증이 필요 없는 요청이면 그대로 진행
//        if (originalRequest.url.encodedPath.contains("/auth/google") ||
//            originalRequest.url.encodedPath.contains("/auth/refresh")) {
//            return chain.proceed(originalRequest)
//        }
//
//        // 액세스 토큰 가져오기
//        val accessToken = runBlocking {
//            tokenManager.accessToken.first()
//        }
//
//        // 헤더에 토큰 추가
//        val newRequest = originalRequest.newBuilder()
//            .apply {
//                if (!accessToken.isNullOrEmpty()) {
//                    header("Authorization", "Bearer $accessToken")
//                }
//            }
//            .build()
//
//        return chain.proceed(newRequest)
//    }
//}