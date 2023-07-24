package madcamp.four.meanwhile.service


interface KakaoService {
    public fun signupOrLogin(userInfo: MutableMap<String, Any>): String

    public fun execKakaoLogin(authorize_code:String): String

    public fun getUserinfo(accessToken: String):MutableMap<String, Any>

    public fun getAccessToken(authorize_code: String):String
}