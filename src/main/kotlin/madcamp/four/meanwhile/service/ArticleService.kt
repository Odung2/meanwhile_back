package madcamp.four.meanwhile.service

import madcamp.four.meanwhile.model.Article

interface ArticleService {
    public fun getQueryKeywords(keyword:String):List<String>

    public fun SearchArticles(keywords:List<String>):List<Article>

    public fun getTrendNews():List<Article>

    public fun dummyArticles():List<Article>
}