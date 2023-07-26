package madcamp.four.meanwhile.serviceImp

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import madcamp.four.meanwhile.model.Article
import madcamp.four.meanwhile.service.ArticleService
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ArticleServiceImp:ArticleService{
    private val djangoServerUrl = "http://localhost:8000"
    private val restTemplate = RestTemplate()

    override fun getQueryKeywords(keyword: String): List<String> {
        // Spring Boot에서 받은 문자열을 Django 서버로 전송
        val response = restTemplate.exchange(djangoServerUrl, HttpMethod.POST, HttpEntity(keyword), object : ParameterizedTypeReference<List<String>>() {})
        return response.body!!
    }

    override fun SearchArticles(keywords: List<String>): List<Article> {
        val requestEntity: HttpEntity<List<String>> = HttpEntity(keywords)
        val response = restTemplate.exchange(djangoServerUrl, HttpMethod.POST, requestEntity, object : ParameterizedTypeReference<List<Article>>() {})
        return response.body ?: emptyList()
    }

    override fun getTrendNews(): List<Article> { //그냥 가져오기.
        val trendUrl:String = "$djangoServerUrl/api/trend"

        val (_, response, result) = trendUrl.httpGet().responseString()

        return when (result)
        {
            is Result.Success -> {
                val data = result.get()
                if (data.isNotEmpty()) {
                    val articleArray = Gson().fromJson(data, Array<Article>::class.java)
                    // Check if the articleArray is not null before converting it to a list
                    articleArray?.toList() ?: emptyList()
                } else {
                    emptyList()
                }
            }
            is Result.Failure -> {
                print("error : ${result.getException()}")
                 emptyList()
            }
        }
    }

    override fun dummyArticles(): List<Article> {
        return listOf()
    }
}