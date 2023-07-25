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

    val dummyArticles = listOf(
            Article(
                articleId = 1,
                summary = "This is the first article.",
                keywords = listOf("keyword1", "keyword2", "keyword3"),
                references = listOf("ref1", "ref2", "ref3"),
                refTitles = listOf("ref1article1", "ref2article1", "ref3article1"),
                publishTime = "2023-07-26 00:04",
                imageLink = "https://image.mediapen.com/news/202103/news_605667_1614652226_m.jpg"
            ),
            Article(
                articleId = 2,
                summary = "This is the second article.",
                keywords = listOf("keyword4", "keyword5", "keyword6"),
                references = listOf("ref4", "ref5", "ref6"),
                refTitles = listOf("ref1article2", "ref2article2", "ref3article2"),
                publishTime = "2023-07-27 00:04",
                imageLink = "https://pbs.twimg.com/media/FACQ9-hUcAcA_11?format=jpg&name=large"
            ),
            Article(
                articleId = 3,
                summary = "This is the third article.",
                keywords = listOf("keyword7", "keyword8", "keyword9"),
                references = listOf("ref7", "ref8", "ref9"),
                refTitles = listOf("ref1article3", "ref2article3", "ref3article3"),
                publishTime = "2023-07-28 00:04",
                imageLink = "https://blog.kakaocdn.net/dn/w4C2v/btrVqJWa4Pt/Ba0Dw0MU5gAlfQ7aKHZksk/img.jpg"
            )
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

    override fun dummyArticles(): List<Article> {
        return dummyArticles
    }
}