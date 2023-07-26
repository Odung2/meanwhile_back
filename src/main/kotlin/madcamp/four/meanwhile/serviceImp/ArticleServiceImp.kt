package madcamp.four.meanwhile.serviceImp

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import madcamp.four.meanwhile.model.Article
import madcamp.four.meanwhile.model.ArticleTemp
import madcamp.four.meanwhile.service.ArticleService
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ArticleServiceImp:ArticleService{
    private val djangoServerUrl = "http://172.10.9.45:443"
    private val restTemplate = RestTemplate()

    val dummyArticles:List<ArticleTemp> = listOf(
            ArticleTemp(
                    articleId = 1,
                    summary = "This is the first article.",
                    keywords = listOf("keyword1", "keyword2", "keyword3"),
                    refTitles = listOf("일본 캐릭터 '농담곰' 한국 서비스 종료 발표하자 '막판 인기'", "[댓글에답하다] 농담처럼 '농담곰'이 사라진다 - 쿠키뉴스", "“한국선 ‘농담곰’ 더이상 안 팔겠다” 카톡 1위 이모티콘 일본의 변심? [IT선빵!]"),
                    references = listOf("https://www.hani.co.kr/arti/society/society_general/1101598.html", "http://www.civicnews.com/news/articleView.html?idxno=30983", "https://www.kukinews.com/newsView/kuk202102150286"),
                    publishTime = "2023-07-26 00:04",
                    imageLink = "https://image.mediapen.com/news/202103/news_605667_1614652226_m.jpg"
            ),
            ArticleTemp(
                    articleId = 2,
                    summary = "This is the second article.",
                    keywords = listOf("keyword4", "keyword5", "keyword6"),
                    refTitles = listOf("\"日 현지 사정 때문에\"…'농담곰' 28일 한국 전개 종료", "카톡 이모티콘 '농담곰'의 쓸쓸한 퇴장", "농담곰, 실시간 검색어 등장…28일 이후로 영영 안녕?"),
                    references = listOf("http://news.heraldcorp.com/view.php?ud=20210216001090", "http://www.goodkyung.com/news/articleView.html?idxno=137797", "https://www.pinpointnews.co.kr/news/articleView.html?idxno=33218"),
                    publishTime = "2023-07-27 00:04",
                    imageLink = "https://pbs.twimg.com/media/FACQ9-hUcAcA_11?format=jpg&name=large"
            ),
            ArticleTemp(
                    articleId = 3,
                    summary = "This is the third article.",
                    keywords = listOf("keyword7", "keyword8", "keyword9"),
                    references = listOf("https://www.yna.co.kr/view/AKR20230329087600005", "https://m.hankookilbo.com/News/Read/A2022080408150002726", "https://www.ize.co.kr/news/articleView.html?idxno=55773"),
                    refTitles = listOf("뉴진스 \"이리 큰 사랑 예상 못 해…우리 매력은 무대 즐기는 것\"", "뉴진스, 또 신기록...걸그룹 시장 뒤집었다", "뉴진스가 온세상에 새겨넣은 놀라온 기록"),
                    publishTime = "2023-07-28 00:04",
                    imageLink = "https://blog.kakaocdn.net/dn/w4C2v/btrVqJWa4Pt/Ba0Dw0MU5gAlfQ7aKHZksk/img.jpg"
            )
    )

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
                print(data)
                if (data.isNotEmpty()) {
                    val articleArray = Gson().fromJson(data, Array<Article>::class.java)
                    print(articleArray)
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

    override fun dummyArticles(): List<ArticleTemp> {
        return dummyArticles
    }
}