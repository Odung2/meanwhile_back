package madcamp.four.meanwhile.serviceImp

import madcamp.four.meanwhile.model.Article
import madcamp.four.meanwhile.service.ArticleService
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ArticleServiceImp:ArticleService{
    private val djangoServerUrl = "http://localhost:10000/api/articles" //jiyeon setting
    private val restTemplate = RestTemplate()

    private val dummyArticles: List<Article> = listOf(
            Article(
                    1,
                    "Lorem ipsum dolor sit amet.",
                    listOf("lorem", "ipsum", "dolor"),
                    listOf("ref1", "ref2"),
                    "https://example.com/image1.jpg"
            ),
            Article(
                    2,
                    "Sed ut perspiciatis unde omnis iste natus error sit.",
                    listOf("sed", "perspiciatis", "error"),
                    listOf("ref3", "ref4"),
                    "https://example.com/image2.jpg"
            ),
            Article(
                    3,
                    "Neque porro quisquam est qui dolorem ipsum quia dolor.",
                    listOf("neque", "porro", "quia"),
                    listOf("ref5", "ref6"),
                    "https://example.com/image3.jpg"
            ),
            // Add more articles here if needed
    )

    override fun getQueryKeywords(keyword: String): List<String> {
        // Spring Boot에서 받은 문자열을 Django 서버로 전송
        val response = restTemplate.exchange(djangoServerUrl, HttpMethod.POST, HttpEntity(keyword), object : ParameterizedTypeReference<List<String>>() {})
        return response.body!!
//        return listOf("keyword1", "keyword2", "keyword3")
    }

    override fun SearchArticles(keywords: List<String>): List<Article> {
        val requestEntity: HttpEntity<List<String>> = HttpEntity(keywords)
        val response = restTemplate.exchange(djangoServerUrl, HttpMethod.POST, requestEntity, object : ParameterizedTypeReference<List<Article>>() {})
        return response.body ?: emptyList()
//        return dummyArticles
    }

    override fun getTrendNews(): List<Article> { //그냥 가져오기.
//        val restTemplate = RestTemplate()
        val response = restTemplate.getForObject(djangoServerUrl, Array<Article>::class.java)
        return response?.toList() ?: emptyList()
//        return dummyArticles
    }
}