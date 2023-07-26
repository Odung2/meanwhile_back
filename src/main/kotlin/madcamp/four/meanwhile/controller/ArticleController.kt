package madcamp.four.meanwhile.controller

import com.fasterxml.jackson.databind.ObjectMapper
import madcamp.four.meanwhile.model.Article
import madcamp.four.meanwhile.security.JwtTokenUtil
import madcamp.four.meanwhile.service.ArticleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.*


@CrossOrigin(allowedHeaders = ["*"])
@Controller
class ArticleController {

    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil

    @Autowired
    lateinit var articleService: ArticleService

    @Autowired
    lateinit var articleCacheManager: CacheManager

    private val objectMapper: ObjectMapper = ObjectMapper()



    @GetMapping("/search")
    fun searchArticles(@RequestParam(value = "query", required = true) query: String): ResponseEntity<String> {
        val queryKeywords: List<String> = articleService.getQueryKeywords(query)
        val cacheKey: String = queryKeywords.joinToString(":")

        val articleCache = articleCacheManager.getCache("articleCache")
        val cachedData: String? = articleCache?.get(cacheKey)?.get() as? String

        return if (cachedData != null) {
            ResponseEntity.ok(cachedData)
        } else {
            val articles: List<Article> = articleService.SearchArticles(queryKeywords)
            val json:String = objectMapper.writeValueAsString(articles)
            articleCache?.put(cacheKey, json)

            ResponseEntity.ok(json)
        }
    }

    @GetMapping("/trending")
    fun getTrend(): ResponseEntity<String> {
        val articleCache = articleCacheManager.getCache("articleCache")
        val cachedData: String? = articleCache?.get("trend")?.get() as? String

        return if (cachedData != null) {
            ResponseEntity.ok(cachedData)
        } else {
            val articles: List<Article> = articleService.getTrendNews()

            val json: String = objectMapper.writeValueAsString(articles)
            articleCache?.put("trend", json)

            ResponseEntity.ok(json)
        }
    }

    @GetMapping("/articles")
    fun dummyArticles(@RequestParam(value = "keywords", required = true) keywords: String): ResponseEntity<String>{
        val json:String = objectMapper.writeValueAsString(articleService.dummyArticles())
//        print(json)
        return  ResponseEntity.ok(json)
    }



}
