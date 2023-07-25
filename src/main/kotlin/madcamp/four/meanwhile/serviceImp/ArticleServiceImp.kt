package madcamp.four.meanwhile.serviceImp

import madcamp.four.meanwhile.model.Article
import madcamp.four.meanwhile.service.ArticleService
import org.springframework.stereotype.Service

@Service
class ArticleServiceImp:ArticleService{
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
        return listOf("keyword1", "keyword2", "keyword3")
    }

    override fun SearchArticles(keywords: List<String>): List<Article> {
        return dummyArticles
    }

    override fun getTrendNews(): List<Article> {
        return dummyArticles
    }
}