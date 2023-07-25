package madcamp.four.meanwhile.serviceImp


import com.google.gson.JsonElement
import com.google.gson.JsonParser
import madcamp.four.meanwhile.mapper.UserMapper
import madcamp.four.meanwhile.model.User
import madcamp.four.meanwhile.security.JwtTokenUtil
import madcamp.four.meanwhile.service.KakaoService
import madcamp.four.meanwhile.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

@Service
class KakaoServiceImp:KakaoService {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userMapper: UserMapper
    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil

    override fun signupOrLogin(userInfo: MutableMap<String, Any>): String {
        try {
            val signupId = userInfo["id"] as Long
            val username = userInfo["nickname"] as String
            val accessToken = userInfo["access_token"] as String
            val signupIdstr = signupId.toString()?: ""
            println("signupid : $signupId")
            println("username : $username")
            println("access token : $accessToken")
            var user: Long? = userMapper.getUserBySignUpId(signupIdstr)?.userId
            val token: UsernamePasswordAuthenticationToken
            if (user == null) {
                val newUser = User(
                    userId = 1,
                    signupId = signupIdstr,
                    email = accessToken.substring(3),
                    username= username,
                    password = accessToken,
                )

                userService.insertUser(newUser)
            }
            val roles: MutableList<GrantedAuthority> = ArrayList()
            roles.add(SimpleGrantedAuthority("USER"))
            token = UsernamePasswordAuthenticationToken(signupId, null, roles)
            return jwtTokenUtil.generateToken(token)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    override fun execKakaoLogin(authorize_code: String): String {
        var accessToken: String  = getAccessToken(authorize_code)
        var userInfo: MutableMap<String, Any>  = getUserinfo(accessToken)

        return signupOrLogin(userInfo)
    }

    override fun getAccessToken(authorize_code: String): String {
        var accessToken = ""
        var refreshToken = ""

        val reqUrl = "https://kauth.kakao.com/oauth/token"

        try {
            val url = URL(reqUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            val bw = BufferedWriter(OutputStreamWriter(connection.outputStream))
            val sb = StringBuilder()
            sb.append("grant_type=authorization_code")
            sb.append("&client_id=54747942f208486425c7e37cb211a42f")
            sb.append("&redirect_uri=http://172.10.5.81:80/kakao/sign_in")
            sb.append("&code=$authorize_code")
            bw.write(sb.toString())
            bw.flush()
            val responseCode = connection.responseCode
            val br = BufferedReader(InputStreamReader(connection.inputStream))
            var line: String? = ""
            var result: String? = ""
            while (br.readLine().also { line = it } != null) {
                result += line
            }
            val element: JsonElement = JsonParser.parseString(result)
            accessToken = element.getAsJsonObject().get("access_token").getAsString()
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString()
            br.close()
            bw.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return accessToken
    }

    override fun getUserinfo(accessToken: String): MutableMap<String, Any> {
        val userInfo: MutableMap<String, Any> = HashMap()

        userInfo["access_token"] = accessToken
        val reqURL = "https://kapi.kakao.com/v2/user/me"

        try {
            val url = URL(reqURL)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", "Bearer $accessToken")
            val responseCode = connection.responseCode
            val br = BufferedReader(InputStreamReader(connection.inputStream))
            var line: String? = ""
            var result: String? = ""
            while (br.readLine().also { line = it } != null) {
                result += line
            }
            val element = JsonParser.parseString(result)
            val properties = element.asJsonObject["properties"].asJsonObject
            val kakao_account = element.asJsonObject["kakao_account"].asJsonObject
            val nickname = properties.asJsonObject["nickname"].asString
            val email = kakao_account.asJsonObject["email"].asString
            val id = element.asJsonObject["id"].asLong
            userInfo["nickname"] = nickname
            userInfo["email"] = email
            userInfo["id"] = id
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        return userInfo
    }

}