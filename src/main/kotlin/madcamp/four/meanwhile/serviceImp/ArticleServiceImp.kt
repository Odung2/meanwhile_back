package madcamp.four.meanwhile.serviceImp

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.nimbusds.jose.shaded.json.JSONArray
import com.nimbusds.jose.shaded.json.JSONObject
import madcamp.four.meanwhile.model.Article
import madcamp.four.meanwhile.model.ArticleTemp
import madcamp.four.meanwhile.model.Keywords
import madcamp.four.meanwhile.service.ArticleService
import org.objectweb.asm.TypeReference
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.RestTemplate

@Service
class ArticleServiceImp:ArticleService{
    private val djangoServerUrl = "http://172.10.9.45:443"
    private val restTemplate = RestTemplate()
    var dataArticle:String = "[{\"title\": [\"NewJeans Are Headed For The Best Anniversary Present Ever - Forbes\"]," +
//            " \"summary\": \"South Korean girl group NewJeans is set to celebrate their first anniversary in Japan with a pop-up store opening ceremony in Tokyo. The event will take place on April 6, 2023 in Tokyo, Japan. The group is on the brink of celebrating their first year in the music industry.\"," +
//            " \"keywords\": [\"tokyo\", \"2023\", \"girl\", \"april\", \"korean\"]," +
//            " \"refs\": [\"https://www.forbes.com/sites/hughmcintyre/2023/07/26/newjeans-are-headed-for-the-best-anniversary-present-ever/\"]," +
//            " \"date\": \"2023-07-26 22:00:16\"," +
//            " \"url\": \"\"," +
//            " \"lang\": 1}," +
//            " {\"title\": [\"\\u201cThey Need To Be Shamed Globally\\u201d \\u2014 Korean Netizens Furious ... - Koreaboo\"]," +
//            " \"summary\": \"A Korean gaming company, Project Moon, recently came under fire for firing a game illustrator. The issue started when male fans of the game Limbus Company took issue with the outfit on one of the characters. After discovering that the designer in charge of the character in question was male, they targeted a female story illustrator instead.\"," +
//            " \"keywords\": [\"korean\", \"moon\", \"fans\", \"illustrator\", \"male\"]," +
//            " \"refs\": [\"https://www.koreaboo.com/news/limbus-company-project-moon-canceled-fire-korean-netizens-reaction-fired-feminist-designer/\"]," +
//            " \"date\": \"2023-07-26 10:39:18\"" +
//            ", \"url\": \"\"," +
//            " \"lang\": 1}," +
//            " {\"title\": [\"NewJeans Beats One Of Blackpink\\u2019s Most Impressive Records - Forbes\"]," +
//            " \"summary\": \"Korean girl group NewJeans has emerged as one of the hottest and most exciting new acts. Despite debuting just a year ago, the group has already achieved a string of high-profile awards. The group will perform at the 2022 KBS Song Festival in Seoul.\"," +
//            " \"keywords\": [\"seoul\", \"2022\", \"girl\", \"hottest\", \"newjeans\"]," +
//            " \"refs\": [\"https://www.forbes.com/sites/hughmcintyre/2023/07/21/newjeans-beats-one-of-blackpinks-most-impressive-records/\"]," +
//            " \"date\": \"2023-07-21 19:39:08\"," +
//            " \"url\": \"\"," +
//            " \"lang\": 1}," +
//            "{\"title\": [\"\\ub274\\uc9c4\\uc2a4 '\\ub514\\ud1a0', \\uc2a4\\ud3ec\\ud2f0\\ud30c\\uc774 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\ub3cc\\ud30c - KBS\\ub274\\uc2a4\"]," +
//            " \"summary\": \"\\uc18c\\uc18d\\uc0ac \\uc5b4\\ub3c4\\uc5b4\\ub294 \\uadf8\\ub8f9 \\ud1b5\\uc0b0 \\ub450 \\ubc88\\uc9f8 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\uace1\\uc774 \\ub41c \\uadf8\\ub8f9 \\ud1b5\\uc0b0 \\ub450 \\ubc88\\uc9f8 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\uace1\\uc774 \\ub41c \\ub274\\uc9c4\\uc2a4\\uc758 '\\ub514\\ud1a0'\\uac00 \\uc74c\\uc6d0 \\uacf5\\uac1c \\ud6c4 218\\uc77c \\ub9cc\\uc758 \\uae30\\ub85d\\uc73c\\ub85c \\uc138\\uacc4 \\ucd5c\\ub300 \\uc74c\\uc6d0 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\ud50c\\ub7ab\\ud3fc \\uc2a4\\ud3ec\\ud2f0\\ud30c\\uc774\\uc5d0\\uc11c 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d\\uc744 \\ub3cc\\ud30c\\ud588\\ub2e4\\uace0 \\ubc1d\\ud614\\ub2e4. \\uc18c\\uc18d\\uc0ac \\uc5b4\\ub3c4\\uc5b4\\ub294 'OMG'\\uc5d0 \\uc774\\uc5b4 \\uadf8\\ub8f9 \\ud1b5\\uc0b0 \\ub450 \\ubc88\\uc9f8 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\uace1\\uc774 \\ub41c \\ub274\\uc9c4\\uc2a4\\uc758 '\\ub514\\ud1a0'\\uac00 \\uc74c\\uc6d0 \\uacf5\\uac1c \\ud6c4 218\\uc77c \\ub9cc\\uc758 \\uae30\\ub85d\\uc73c\\ub85c \\uc138\\uacc4 \\ucd5c\\ub300 \\uc74c\\uc6d0 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\ud50c\\ub7ab\\ud3fc \\uc2a4\\ud3ec\\ud2f0\\ud30c\\uc774\\uc5d0\\uc11c 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d\\uc744 \\ub3cc\\ud30c\\ud588\\ub2e4\\uace0 \\ubc1d\\ud614\\ub2e4.\"," +
//            " \"keywords\": [\"\\uae30\\ub85d\", \"\\ub274\\uc9c4\\uc2a4\", \"\\uc2a4\\ud2b8\\ub9ac\\ubc0d\", \"\\ud50c\\ub7ab\\ud3fc\", \"\\uc138\\uacc4\"]," +
//            " \"refs\": [\"https://news.kbs.co.kr/news/view.do?ncd=7734096\"]," +
//            " \"date\": \"2023-07-27 00:22:00\"," +
//            " \"url\": \"\"," +
//            " \"lang\": 0}," +
//            " {\"title\": [\"\\ub274\\uc9c4\\uc2a4 'ASAP', \\uae08\\ubc1c\\uc774 \\ub108\\ubb34\\ud574\\u2026\\ubbf8\\ub2c8 2\\uc9d1 '\\ube4c\\ubcf4\\ub4dc 200' 1\\uc704 \\uc720\\ub825 - \\ub274\\uc2dc\\uc2a4\", \"\\ub274\\uc9c4\\uc2a4, 'ASAP' \\ubba4\\ube44 \\uacf5\\uac1c\\u2026 \\uae08\\ubc1c \\uc694\\uc815+\\ubabd\\ud658\\uc801 \\ubd84\\uc704\\uae30 - \\uc544\\uc2dc\\uc544\\ud22c\\ub370\\uc774\", \"[\\uac00\\uc694\\uc18c\\uc2dd] \\ub274\\uc9c4\\uc2a4 'ASAP' \\ubba4\\uc9c1\\ube44\\ub514\\uc624 \\uacf5\\uac1c - \\uc5f0\\ud569\\ub274\\uc2a4\"]," +
//            " \"summary\": \"\\ub274\\ub4dc\\ub86c \\uac78\\uadf8\\ub8f9 '\\ub274\\uc9c4\\uc2a4(NewJeans)'\\uac00 \\ubbf8\\ub2c8 2\\uc9d1 '\\uac9f \\uc5c5(Get Up)' \\uc5d0\\uc11c \\uae08\\ubc1c \\uc694\\uc815\\uc73c\\ub85c \\ubcc0\\ud55c \\ub2e4\\uc12f \\uba64\\ubc84\\ub4e4\\uc758 \\ubaa8\\uc2b5\\uc774 \\ub2f4\\uae34 '\\uae08\\ubc1c \\uc694\\uc815'\\uc758 \\ubba4\\uc9c1\\ube44\\ub514\\uc624\\ub97c 26\\uc77c 0\\uc2dc \\ud558\\uc774\\ube0c \\ub808\\uc774\\ube14\\uc988 \\uc720\\ud29c\\ube0c \\ucc44\\ub110\\uc5d0 \\uacf5\\uac1c, \\uae08\\ubc1c \\uc694\\uc815\\uc73c\\ub85c \\ubcc0\\uc2e0\\ud558\\uc5ec \\uc2e0\\uc2a4 \\uc0ac\\uc6b4\\ub4dc\\uc640 \\ub098\\ub978\\ud55c \\uc74c\\uc0c9\\uc774 \\uc8fc\\ub294 \\ubabd\\ud658\\uc801\\uc778 \\uace1 \\ubd84\\uc704\\uae30\\uc640 \\uc870\\ud654\\ub97c \\uc774\\ub8e8\\uba70 \\uc2e0\\uc2a4 \\uc0ac\\uc6b4\\ub4dc\\uc640 \\ub098\\ub978\\ud55c \\uc74c\\uc0c9\\uc774 \\uc8fc\\ub294 \\ubabd\\ud658\\uc801\\uc778 \\uace1 \\ubd84\\uc704\\uae30\\uc640 \\uc870\\ud654\\ub97c \\uc774\\ub8e8\\uba70 \\uc2e0\\uc2a4 \\uc0ac\\uc6b4\\ub4dc\\uc640 \\ub098\\ub978\\ud55c \\uc74c\\uc0c9\\uc774 \\uc8fc\\ub294 \\ubabd\\ud658\\uc801\\uc778 \\uace1 \\ubd84\\uc704\\uae30\\uc640 \\uc870\\ud654\\ub97c \\uc774\\ub8ec\\ub2e4.\"," +
//            " \"keywords\": [\"\\uae08\\ubc1c\", \"\\ub274\\uc9c4\\uc2a4\"]," +
//            " \"refs\": [\"https://mobile.newsis.com/view.html?ar_id=NISX20230726_0002391063\", \"https://www.asiatoday.co.kr/view.php?key=20230726010014924&ref=view_topnews\", \"https://www.yna.co.kr/view/AKR20230726059500005\"]," +
//            " \"date\": \"2023-07-26 02:54:16\"," +
//            " \"url\": \"https://r.yna.co.kr/www/home_n/v02/img/bg_none_people01.jpg\"," +
//            " \"lang\": 0}," +
            " {\"title\": [\"NH\\ud22c\\uc790 \\u201c\\ub274\\uc9c4\\uc2a4 \\ube4c\\ubcf4\\ub4dc \\ud56b100 \\ucc28\\ud2b8 66\\uc704, \\ud558\\uc774\\ube0c \\ud558\\ubc18\\uae30 \\uc704\\ubc84\\uc2a4 \\uc218\\uc775\\ud654\\u201d - \\ube44\\uc988\\ub2c8\\uc2a4\\ud3ec\\uc2a4\\ud2b8\"]," +
            " \"summary\": \"\\ud558\\ubc18\\uae30\\ube0c\\ube0c \\uc18c\\uc18d \\uc5ec\\ub7ec \\uc544\\ud2f0\\uc2a4\\ud2b8\\uac00 \\ud558\\ubc18\\uae30 \\uc2e0\\uaddc\\uc568\\ubc94 \\ubc1c\\ub9e4\\uc640 \\uc6d4\\ub4dc\\ud22c\\uc5b4\\ub97c \\uacc4\\ud68d\\ud558\\uace0 \\uc788\\ub294 \\uac00\\uc6b4\\ub370 \\uac78\\uadf8\\ub8f9 \\ub274\\uc9c4\\uc2a4\\uac00 \\uc2e0\\uace1\\uc73c\\ub85c \\ube4c\\ubcf4\\ub4dc \\ud56b100 \\ucc28\\ud2b8\\uc5d0\\uc11c 66\\uc704\\uc5d0 \\uc774\\ub984\\uc744 \\uc62c\\ub824 \\uc790\\uccb4 \\ucd5c\\uace0\\uc21c\\uc704\\ub97c \\uae30\\ub85d\\ud558\\uace0 BTS \\uc815\\uad6d\\uc758 \\uc194\\ub85c\\uace1\\ub3c4 \\uc9c4\\uc785\\uc774 \\uc608\\uc0c1\\ub418\\uc5b4 \\ud558\\uc774\\ube0c \\uc8fc\\uc2dd \\ub9e4\\uc218\\uc758\\uacac\\uc774 \\uc720\\uc9c0\\ub410\\uace0 \\ub274\\uc9c4\\uc2a4 \\uc2e0\\uace1\\uc73c\\ub85c \\ube4c\\ubcf4\\ub4dc \\ud56b100 \\ucc28\\ud2b8\\uc5d0\\uc11c 66\\uc704\\uc5d0 \\uc774\\ub984\\uc744 \\uc62c\\ub824 \\uc790\\uccb4 \\ucd5c\\uace0\\uc21c\\uc704\\ub97c \\uae30\\ub85d\\ud558\\uace0 \\uc790\\uccb4 \\ucd5c\\uace0\\uc21c\\uc704\\ub97c \\uae30\\ub85d\\ud558\\uace0 BTS \\uc815\\uad6d\\uc758 \\uc194\\ub85c\\uace1\\ub3c4 \\uc9c4\\uc785\\uc774 \\uc608\\uc0c1\\ub418\\uc5b4 \\ud558\\uc774\\ube0c \\uc8fc\\uc2dd \\ub9e4\\uc218\\uc758\\uacac\\uc774 \\uc720\\uc9c0\\ub410\\ub2e4.\"," +
            " \"keywords\": [\"\\ud22c\\uc5b4\", \"\\uc8fc\\uc2dd\", \"\\ub274\\uc9c4\\uc2a4\", \"\\uacc4\\ud68d\", \"\\uae30\\ub85d\"]," +
            " \"refs\": [\"https://www.businesspost.co.kr/BP?command=article_view&num=321623\"]," +
            " \"date\": \"2023-07-18 23:50:45\"," +
            " \"url\": \"https://www.businesspost.co.kr/news/photo/202307/20230719084805_45801.jpg\"," +
            " \"lang\": 0}," +
            " {\"title\": [\"\\ub124\\uc774\\ubc84 \\ub098\\uc6b0(NOW) \\uacbd\\uc7c1\\ub825 \\ud0a4\\uc6b0\\uae30 \\ud3ec\\uae30 \\ubabb \\ud574, \\ucd5c\\uc218\\uc5f0 K\\ud31d \\uc778\\uae30 \\uc801\\uadf9 \\ud65c\\uc6a9 - \\ube44\\uc988\\ub2c8\\uc2a4\\ud3ec\\uc2a4\\ud2b8\"]," +
            " \"summary\": \"25\\uc77c \\ub124\\uc774\\ubc84\\uc5d0 \\ub530\\ub974\\uba74 8\\uc6d4 \\ud55c \\ub2ec \\ub3d9\\uc548 \\ub098\\uc6b0\\uc5d0 \\ub274\\uc9c4\\uc2a4 \\ud2b9\\uc9d1\\uc73c\\ub85c K\\ud31d \\ucc28\\ud2b8\\uc1fc '\\uc5d4\\ud31d(NPOP)'\\uc758 \\ud504\\ub9ac\\ubdf0\\ub97c \\uc120\\ubcf4\\uc774\\uac8c \\ub418\\uba70, \\ucd5c\\uc218\\uc5f0 \\ub124\\uc774\\ubc84 \\ub300\\ud45c\\uc774\\uc0ac\\uac00 \\uc80a\\uc740 \\uc774\\uc6a9\\uc790 \\uacf5\\ub7b5\\uc5d0 \\ud798\\uc744 \\uc4f0\\uace0 \\uc788\\uace0, \\ucd5c\\uc218\\uc5f0 \\ub124\\uc774\\ubc84 \\ub300\\ud45c\\uc774\\uc0ac\\uac00 \\uc80a\\uc740 \\uc774\\uc6a9\\uc790 \\uacf5\\ub7b5\\uc5d0 \\ud798\\uc744 \\uc4f0\\uace0 \\uc788\\ub2e4.\"," +
            " \"keywords\": [\"\\ud504\\ub9ac\\ubdf0\", \"\\ubaa8\\ubc14\\uc77c\", \"\\ub274\\uc9c4\\uc2a4\", \"\\ud2b9\\uc9d1\", \"\\ub300\\ud45c\\uc774\\uc0ac\"]," +
            " \"refs\": [\"https://www.businesspost.co.kr/BP?command=article_view&num=322447\"]," +
            " \"date\": \"2023-07-26 07:53:04\"," +
            " \"url\": \"https://www.businesspost.co.kr/news/photo/202301/20230105182650_117756.jpg\"," +
            " \"lang\": 0}," +
            " {\"title\": [\"[\\uac00\\uc694\\uc18c\\uc2dd] \\uc704\\ubc84\\uc2a4, \\ub274\\uc9c4\\uc2a4\\u00b7TXT \\u7f8e \\ub864\\ub77c\\ud314\\ub8e8\\uc790 \\ubb34\\ub300 \\uc0dd\\uc911\\uacc4 - \\uc5f0\\ud569\\ub274\\uc2a4\"]," +
            " \"summary\": \"25\\ubc84\\uc2a4\\uac00 \\ubbf8\\uad6d \\uc678 \\uc804 \\uc138\\uacc4 \\uc9c0\\uc5ed\\uc5d0 \\uadf8\\ub8f9 \\ub274\\uc9c4\\uc2a4\\uc640 \\ud22c\\ubaa8\\ub85c\\uc6b0\\ubc14\\uc774\\ud22c\\uac8c\\ub354\\uc758 '\\ub864\\ub77c\\ud314\\ub8e8\\uc790' \\ucd95\\uc81c \\ubb34\\ub300\\ub97c \\uc0dd\\uc911\\uacc4\\ud55c\\ub2e4\\uace0 25\\uc77c \\ubc1d\\ud614\\uc73c\\uba70 \\uc774\\ubc88 \\uc0dd\\uc911\\uacc4\\ub294 \\uc704\\ubc84\\uc2a4 \\uac00\\uc785\\uc790\\ub77c\\uba74 \\ub204\\uad6c\\ub098 \\ubb34\\ub8cc\\ub85c \\uc2dc\\uccad\\ud560 \\uc218 \\uc788\\uace0 \\uc2e4\\uc2dc\\uac04 \\ucc44\\ud305 \\uae30\\ub2a5\\ub3c4 \\uc81c\\uacf5\\ub41c\\ub2e4.\"," +
            " \"keywords\": [\"\\uc0dd\\uc911\\uacc4\", \"\\ub274\\uc9c4\\uc2a4\", \"\\ucd95\\uc81c\", \"\\ubc84\\uc2a4\", \"\\uc81c\\uacf5\"]," +
            " \"refs\": [\"https://www.yna.co.kr/view/AKR20230725124100005\"]," +
            " \"date\": \"2023-07-25 07:01:47\"," +
            " \"url\": \"//r.yna.co.kr/www/home_n/v02/img/bg_none_people01.jpg\"," +
            " \"lang\": 0}]"
    val newjeans1:List<ArticleTemp> = listOf(
            ArticleTemp(
                    articleId = 1,
                    summary = "South Korean girl group NewJeans is set to celebrate their first anniversary in Japan with a pop-up store opening ceremony in Tokyo. The event will take place on April 6, 2023 in Tokyo, Japan. The group is on the brink of celebrating their first year in the music industry.",
                    keywords = listOf("tokyo", "2023", "girl", "april","korean" ),
                    refTitles = listOf("NewJeans Are Headed For The Best Anniversary Present Ever - Forbes"),
                    references = listOf("https://www.forbes.com/sites/hughmcintyre/2023/07/26/newjeans-are-headed-for-the-best-anniversary-present-ever/"),
                    publishTime = "2023-07-26 22:00:16",
                    imageLink = "",
                    language = 1
            ),
            ArticleTemp(
                    articleId = 2,
                    summary = "A Korean gaming company, Project Moon, recently came under fire for firing a game illustrator. The issue started when male fans of the game Limbus Company took issue with the outfit on one of the characters. After discovering that the designer in charge of the character in question was male, they targeted a female story illustrator instead.",
                    keywords = listOf("korean", "moon", "fans", "illustrator", "male"),
                    refTitles = listOf("\"日 현지 사정 때문에\"…'농담곰' 28일 한국 전개 종료", "카톡 이모티콘 '농담곰'의 쓸쓸한 퇴장", "농담곰, 실시간 검색어 등장…28일 이후로 영영 안녕?"),
                    references = listOf("https://www.koreaboo.com/news/limbus-company-project-moon-canceled-fire-korean-netizens-reaction-fired-feminist-designer/"),
                    publishTime = "2023-07-26 10:39:18",
                    imageLink = "",
                    language = 0

            ),
            ArticleTemp(
                    articleId = 3,
                    summary = "Korean girl group NewJeans has emerged as one of the hottest and most exciting new acts. Despite debuting just a year ago, the group has already achieved a string of high-profile awards. The group will perform at the 2022 KBS Song Festival in Seoul.",
                    keywords = listOf("seoul", "2022", "girl", "hottest", "newjeans"),
                    references = listOf("https://www.forbes.com/sites/hughmcintyre/2023/07/21/newjeans-beats-one-of-blackpinks-most-impressive-records/"),
                    refTitles = listOf("NewJeans Beats One Of Blackpink\\u2019s Most Impressive Records - Forbes"),
                    publishTime = "2023-07-21 19:39:08",
                    imageLink = "",
                    language = 1

            ),
            ArticleTemp(
                    articleId = 4,
                    summary= "\\uc18c\\uc18d\\uc0ac \\uc5b4\\ub3c4\\uc5b4\\ub294 \\uadf8\\ub8f9 \\ud1b5\\uc0b0 \\ub450 \\ubc88\\uc9f8 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\uace1\\uc774 \\ub41c \\uadf8\\ub8f9 \\ud1b5\\uc0b0 \\ub450 \\ubc88\\uc9f8 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\uace1\\uc774 \\ub41c \\ub274\\uc9c4\\uc2a4\\uc758 '\\ub514\\ud1a0'\\uac00 \\uc74c\\uc6d0 \\uacf5\\uac1c \\ud6c4 218\\uc77c \\ub9cc\\uc758 \\uae30\\ub85d\\uc73c\\ub85c \\uc138\\uacc4 \\ucd5c\\ub300 \\uc74c\\uc6d0 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\ud50c\\ub7ab\\ud3fc \\uc2a4\\ud3ec\\ud2f0\\ud30c\\uc774\\uc5d0\\uc11c 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d\\uc744 \\ub3cc\\ud30c\\ud588\\ub2e4\\uace0 \\ubc1d\\ud614\\ub2e4. \\uc18c\\uc18d\\uc0ac \\uc5b4\\ub3c4\\uc5b4\\ub294 'OMG'\\uc5d0 \\uc774\\uc5b4 \\uadf8\\ub8f9 \\ud1b5\\uc0b0 \\ub450 \\ubc88\\uc9f8 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\uace1\\uc774 \\ub41c \\ub274\\uc9c4\\uc2a4\\uc758 '\\ub514\\ud1a0'\\uac00 \\uc74c\\uc6d0 \\uacf5\\uac1c \\ud6c4 218\\uc77c \\ub9cc\\uc758 \\uae30\\ub85d\\uc73c\\ub85c \\uc138\\uacc4 \\ucd5c\\ub300 \\uc74c\\uc6d0 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\ud50c\\ub7ab\\ud3fc \\uc2a4\\ud3ec\\ud2f0\\ud30c\\uc774\\uc5d0\\uc11c 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d\\uc744 \\ub3cc\\ud30c\\ud588\\ub2e4\\uace0 \\ubc1d\\ud614\\ub2e4.",
                    keywords = listOf("\\uae08\\ubc1c", "2022", "girl", "hottest", "newjeans"),
                    references = listOf("https://news.kbs.co.kr/news/view.do?ncd=7734096"),
                    refTitles = listOf("\\ub274\\uc9c4\\uc2a4 '\\ub514\\ud1a0', \\uc2a4\\ud3ec\\ud2f0\\ud30c\\uc774 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\ub3cc\\ud30c - KBS\\ub274\\uc2a4\\"),
                    publishTime = "2023-07-27 00:22:00",
                    imageLink = "",
                    language = 1

            ),
            ArticleTemp(
                    articleId = 5,
                    summary= "\\ub274\\ub4dc\\ub86c \\uac78\\uadf8\\ub8f9 '\\ub274\\uc9c4\\uc2a4(NewJeans)'\\uac00 \\ubbf8\\ub2c8 2\\uc9d1 '\\uac9f \\uc5c5(Get Up)' \\uc5d0\\uc11c \\uae08\\ubc1c \\uc694\\uc815\\uc73c\\ub85c \\ubcc0\\ud55c \\ub2e4\\uc12f \\uba64\\ubc84\\ub4e4\\uc758 \\ubaa8\\uc2b5\\uc774 \\ub2f4\\uae34 '\\uae08\\ubc1c \\uc694\\uc815'\\uc758 \\ubba4\\uc9c1\\ube44\\ub514\\uc624\\ub97c 26\\uc77c 0\\uc2dc \\ud558\\uc774\\ube0c \\ub808\\uc774\\ube14\\uc988 \\uc720\\ud29c\\ube0c \\ucc44\\ub110\\uc5d0 \\uacf5\\uac1c, \\uae08\\ubc1c \\uc694\\uc815\\uc73c\\ub85c \\ubcc0\\uc2e0\\ud558\\uc5ec \\uc2e0\\uc2a4 \\uc0ac\\uc6b4\\ub4dc\\uc640 \\ub098\\ub978\\ud55c \\uc74c\\uc0c9\\uc774 \\uc8fc\\ub294 \\ubabd\\ud658\\uc801\\uc778 \\uace1 \\ubd84\\uc704\\uae30\\uc640 \\uc870\\ud654\\ub97c \\uc774\\ub8e8\\uba70 \\uc2e0\\uc2a4 \\uc0ac\\uc6b4\\ub4dc\\uc640 \\ub098\\ub978\\ud55c \\uc74c\\uc0c9\\uc774 \\uc8fc\\ub294 \\ubabd\\ud658\\uc801\\uc778 \\uace1 \\ubd84\\uc704\\uae30\\uc640 \\uc870\\ud654\\ub97c \\uc774\\ub8e8\\uba70 \\uc2e0\\uc2a4 \\uc0ac\\uc6b4\\ub4dc\\uc640 \\ub098\\ub978\\ud55c \\uc74c\\uc0c9\\uc774 \\uc8fc\\ub294 \\ubabd\\ud658\\uc801\\uc778 \\uace1 \\ubd84\\uc704\\uae30\\uc640 \\uc870\\ud654\\ub97c \\uc774\\ub8ec\\ub2e4.",
                    keywords = listOf("\\uae08\\ubc1c", "2022", "girl", "hottest", "newjeans"),
                    references = listOf("https://news.kbs.co.kr/news/view.do?ncd=7734096", "https://www.asiatoday.co.kr/view.php?key=20230726010014924&ref=view_topnews", "https://www.yna.co.kr/view/AKR20230726059500005"),
                    refTitles = listOf("\\ub274\\uc9c4\\uc2a4 'ASAP', \\uae08\\ubc1c\\uc774 \\ub108\\ubb34\\ud574\\u2026\\ubbf8\\ub2c8 2\\uc9d1 '\\ube4c\\ubcf4\\ub4dc 200' 1\\uc704 \\uc720\\ub825 - \\ub274\\uc2dc\\uc2a4\\",
                           "\\ub274\\uc9c4\\uc2a4, 'ASAP' \\ubba4\\ube44 \\uacf5\\uac1c\\u2026 \\uae08\\ubc1c \\uc694\\uc815+\\ubabd\\ud658\\uc801 \\ubd84\\uc704\\uae30 - \\uc544\\uc2dc\\uc544\\ud22c\\ub370\\uc774",
                            "\\uac00\\uc694\\uc18c\\uc2dd] \\ub274\\uc9c4\\uc2a4 'ASAP' \\ubba4\\uc9c1\\ube44\\ub514\\uc624 \\uacf5\\uac1c - \\uc5f0\\ud569\\ub274\\uc2a4"),
                    publishTime = "2023-07-26 02:54:16",
                    imageLink = "https://r.yna.co.kr/www/home_n/v02/img/bg_none_people01.jpg",
                    language = 0

            ),
            ArticleTemp(
                    articleId = 6,
                    summary= "\\ub274\\ub4dc\\ub86c \\uac78\\uadf8\\ub8f9 '\\ub274\\uc9c4\\uc2a4(NewJeans)'\\uac00 \\ubbf8\\ub2c8 2\\uc9d1 '\\uac9f \\uc5c5(Get Up)' \\uc5d0\\uc11c \\uae08\\ubc1c \\uc694\\uc815\\uc73c\\ub85c \\ubcc0\\ud55c \\ub2e4\\uc12f \\uba64\\ubc84\\ub4e4\\uc758 \\ubaa8\\uc2b5\\uc774 \\ub2f4\\uae34 '\\uae08\\ubc1c \\uc694\\uc815'\\uc758 \\ubba4\\uc9c1\\ube44\\ub514\\uc624\\ub97c 26\\uc77c 0\\uc2dc \\ud558\\uc774\\ube0c \\ub808\\uc774\\ube14\\uc988 \\uc720\\ud29c\\ube0c \\ucc44\\ub110\\uc5d0 \\uacf5\\uac1c, \\uae08\\ubc1c \\uc694\\uc815\\uc73c\\ub85c \\ubcc0\\uc2e0\\ud558\\uc5ec \\uc2e0\\uc2a4 \\uc0ac\\uc6b4\\ub4dc\\uc640 \\ub098\\ub978\\ud55c \\uc74c\\uc0c9\\uc774 \\uc8fc\\ub294 \\ubabd\\ud658\\uc801\\uc778 \\uace1 \\ubd84\\uc704\\uae30\\uc640 \\uc870\\ud654\\ub97c \\uc774\\ub8e8\\uba70 \\uc2e0\\uc2a4 \\uc0ac\\uc6b4\\ub4dc\\uc640 \\ub098\\ub978\\ud55c \\uc74c\\uc0c9\\uc774 \\uc8fc\\ub294 \\ubabd\\ud658\\uc801\\uc778 \\uace1 \\ubd84\\uc704\\uae30\\uc640 \\uc870\\ud654\\ub97c \\uc774\\ub8e8\\uba70 \\uc2e0\\uc2a4 \\uc0ac\\uc6b4\\ub4dc\\uc640 \\ub098\\ub978\\ud55c \\uc74c\\uc0c9\\uc774 \\uc8fc\\ub294 \\ubabd\\ud658\\uc801\\uc778 \\uace1 \\ubd84\\uc704\\uae30\\uc640 \\uc870\\ud654\\ub97c \\uc774\\ub8ec\\ub2e4.",
                    keywords = listOf("\\uae08\\ubc1c", "2022", "girl", "hottest", "newjeans"),
                    references = listOf("https://news.kbs.co.kr/news/view.do?ncd=7734096", "https://www.asiatoday.co.kr/view.php?key=20230726010014924&ref=view_topnews", "https://www.yna.co.kr/view/AKR20230726059500005"),
                    refTitles = listOf("NH\\ud22c\\uc790 \\u201c\\ub274\\uc9c4\\uc2a4 \\ube4c\\ubcf4\\ub4dc \\ud56b100 \\ucc28\\ud2b8 66\\uc704, \\ud558\\uc774\\ube0c \\ud558\\ubc18\\uae30 \\uc704\\ubc84\\uc2a4 \\uc218\\uc775\\ud654\\u201d - \\ube44\\uc988\\ub2c8\\uc2a4\\ud3ec\\uc2a4\\ud2b8",
                            "\\ub274\\uc9c4\\uc2a4, 'ASAP' \\ubba4\\ube44 \\uacf5\\uac1c\\u2026 \\uae08\\ubc1c \\uc694\\uc815+\\ubabd\\ud658\\uc801 \\ubd84\\uc704\\uae30 - \\uc544\\uc2dc\\uc544\\ud22c\\ub370\\uc774",
                            "\\uac00\\uc694\\uc18c\\uc2dd] \\ub274\\uc9c4\\uc2a4 'ASAP' \\ubba4\\uc9c1\\ube44\\ub514\\uc624 \\uacf5\\uac1c - \\uc5f0\\ud569\\ub274\\uc2a4"),
                    publishTime = "2023-07-26 02:54:16",
                    imageLink = "https://r.yna.co.kr/www/home_n/v02/img/bg_none_people01.jpg",
                    language = 0

            ),


    )

    val dummyArticles2:List<ArticleTemp> = listOf(
            ArticleTemp(
                    articleId = 1,
                    summary = "This is the first article.",
                    keywords = listOf("keyword1", "keyword2", "keyword3"),
                    refTitles = listOf("일본 캐릭터 '농담곰' 한국 서비스 종료 발표하자 '막판 인기'", "[댓글에답하다] 농담처럼 '농담곰'이 사라진다 - 쿠키뉴스", "“한국선 ‘농담곰’ 더이상 안 팔겠다” 카톡 1위 이모티콘 일본의 변심? [IT선빵!]"),
                    references = listOf("https://www.hani.co.kr/arti/society/society_general/1101598.html", "http://www.civicnews.com/news/articleView.html?idxno=30983", "https://www.kukinews.com/newsView/kuk202102150286"),
                    publishTime = "2023-07-26 00:04",
                    imageLink = "https://image.mediapen.com/news/202103/news_605667_1614652226_m.jpg",
                    language = 0
            ),
            ArticleTemp(
                    articleId = 2,
                    summary = "This is the second article.",
                    keywords = listOf("keyword4", "keyword5", "keyword6"),
                    refTitles = listOf("\"日 현지 사정 때문에\"…'농담곰' 28일 한국 전개 종료", "카톡 이모티콘 '농담곰'의 쓸쓸한 퇴장", "농담곰, 실시간 검색어 등장…28일 이후로 영영 안녕?"),
                    references = listOf("http://news.heraldcorp.com/view.php?ud=20210216001090", "http://www.goodkyung.com/news/articleView.html?idxno=137797", "https://www.pinpointnews.co.kr/news/articleView.html?idxno=33218"),
                    publishTime = "2023-07-27 00:04",
                    imageLink = "https://pbs.twimg.com/media/FACQ9-hUcAcA_11?format=jpg&name=large",
                    language = 1

            ),
            ArticleTemp(
                    articleId = 3,
                    summary = "This is the third article.",
                    keywords = listOf("keyword7", "keyword8", "keyword9"),
                    references = listOf("https://www.yna.co.kr/view/AKR20230329087600005", "https://m.hankookilbo.com/News/Read/A2022080408150002726", "https://www.ize.co.kr/news/articleView.html?idxno=55773"),
                    refTitles = listOf("뉴진스 \"이리 큰 사랑 예상 못 해…우리 매력은 무대 즐기는 것\"", "뉴진스, 또 신기록...걸그룹 시장 뒤집었다", "뉴진스가 온세상에 새겨넣은 놀라온 기록"),
                    publishTime = "2023-07-28 00:04",
                    imageLink = "https://blog.kakaocdn.net/dn/w4C2v/btrVqJWa4Pt/Ba0Dw0MU5gAlfQ7aKHZksk/img.jpg",
                    language = 0

            )
    )

    val dummyArticles3:List<ArticleTemp> = listOf(
            ArticleTemp(
                    articleId = 1,
                    summary = "This is the first article.",
                    keywords = listOf("keyword1", "keyword2", "keyword3"),
                    refTitles = listOf("일본 캐릭터 '농담곰' 한국 서비스 종료 발표하자 '막판 인기'", "[댓글에답하다] 농담처럼 '농담곰'이 사라진다 - 쿠키뉴스", "“한국선 ‘농담곰’ 더이상 안 팔겠다” 카톡 1위 이모티콘 일본의 변심? [IT선빵!]"),
                    references = listOf("https://www.hani.co.kr/arti/society/society_general/1101598.html", "http://www.civicnews.com/news/articleView.html?idxno=30983", "https://www.kukinews.com/newsView/kuk202102150286"),
                    publishTime = "2023-07-26 00:04",
                    imageLink = "https://image.mediapen.com/news/202103/news_605667_1614652226_m.jpg",
                    language = 0
            ),
            ArticleTemp(
                    articleId = 2,
                    summary = "This is the second article.",
                    keywords = listOf("keyword4", "keyword5", "keyword6"),
                    refTitles = listOf("\"日 현지 사정 때문에\"…'농담곰' 28일 한국 전개 종료", "카톡 이모티콘 '농담곰'의 쓸쓸한 퇴장", "농담곰, 실시간 검색어 등장…28일 이후로 영영 안녕?"),
                    references = listOf("http://news.heraldcorp.com/view.php?ud=20210216001090", "http://www.goodkyung.com/news/articleView.html?idxno=137797", "https://www.pinpointnews.co.kr/news/articleView.html?idxno=33218"),
                    publishTime = "2023-07-27 00:04",
                    imageLink = "https://pbs.twimg.com/media/FACQ9-hUcAcA_11?format=jpg&name=large",
                    language = 1

            ),
            ArticleTemp(
                    articleId = 3,
                    summary = "This is the third article.",
                    keywords = listOf("keyword7", "keyword8", "keyword9"),
                    references = listOf("https://www.yna.co.kr/view/AKR20230329087600005", "https://m.hankookilbo.com/News/Read/A2022080408150002726", "https://www.ize.co.kr/news/articleView.html?idxno=55773"),
                    refTitles = listOf("뉴진스 \"이리 큰 사랑 예상 못 해…우리 매력은 무대 즐기는 것\"", "뉴진스, 또 신기록...걸그룹 시장 뒤집었다", "뉴진스가 온세상에 새겨넣은 놀라온 기록"),
                    publishTime = "2023-07-28 00:04",
                    imageLink = "https://blog.kakaocdn.net/dn/w4C2v/btrVqJWa4Pt/Ba0Dw0MU5gAlfQ7aKHZksk/img.jpg",
                    language = 0

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

    override fun search(query:String):String
    {
        var dataArticle:String = "[{\"title\": [\"NewJeans Are Headed For The Best Anniversary Present Ever - Forbes\"], \"summary\": \"South Korean girl group NewJeans is set to celebrate their first anniversary in Japan with a pop-up store opening ceremony in Tokyo. The event will take place on April 6, 2023 in Tokyo, Japan. The group is on the brink of celebrating their first year in the music industry.\"," +
                " \"keywords\": [\"tokyo\", \"2023\", \"girl\", \"april\", \"korean\"], \"refs\": [\"https://www.forbes.com/sites/hughmcintyre/2023/07/26/newjeans-are-headed-for-the-best-anniversary-present-ever/\"]," +
                " \"date\": \"2023-07-26 22:00:16\"," +
                " \"url\": \"\"," +
                " \"lang\": 1}," +
                " {\"title\": [\"\\u201cThey Need To Be Shamed Globally\\u201d \\u2014 Korean Netizens Furious ... - Koreaboo\"]," +
                " \"summary\": \"A Korean gaming company, Project Moon, recently came under fire for firing a game illustrator. The issue started when male fans of the game Limbus Company took issue with the outfit on one of the characters. After discovering that the designer in charge of the character in question was male, they targeted a female story illustrator instead.\"," +
                " \"keywords\": [\"korean\", \"moon\", \"fans\", \"illustrator\", \"male\"]," +
                " \"refs\": [\"https://www.koreaboo.com/news/limbus-company-project-moon-canceled-fire-korean-netizens-reaction-fired-feminist-designer/\"]," +
                " \"date\": \"2023-07-26 10:39:18\"" +
                ", \"url\": \"\"," +
                " \"lang\": 1}," +
                " {\"title\": [\"NewJeans Beats One Of Blackpink\\u2019s Most Impressive Records - Forbes\"]," +
                " \"summary\": \"Korean girl group NewJeans has emerged as one of the hottest and most exciting new acts. Despite debuting just a year ago, the group has already achieved a string of high-profile awards. The group will perform at the 2022 KBS Song Festival in Seoul.\"," +
                " \"keywords\": [\"seoul\", \"2022\", \"girl\", \"hottest\", \"newjeans\"]," +
                " \"refs\": [\"https://www.forbes.com/sites/hughmcintyre/2023/07/21/newjeans-beats-one-of-blackpinks-most-impressive-records/\"]," +
                " \"date\": \"2023-07-21 19:39:08\"," +
                " \"url\": \"\"," +
                " \"lang\": 1}," +
                "{\"title\": [\"\\ub274\\uc9c4\\uc2a4 '\\ub514\\ud1a0', \\uc2a4\\ud3ec\\ud2f0\\ud30c\\uc774 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\ub3cc\\ud30c - KBS\\ub274\\uc2a4\"]," +
                " \"summary\": \"\\uc18c\\uc18d\\uc0ac \\uc5b4\\ub3c4\\uc5b4\\ub294 \\uadf8\\ub8f9 \\ud1b5\\uc0b0 \\ub450 \\ubc88\\uc9f8 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\uace1\\uc774 \\ub41c \\uadf8\\ub8f9 \\ud1b5\\uc0b0 \\ub450 \\ubc88\\uc9f8 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\uace1\\uc774 \\ub41c \\ub274\\uc9c4\\uc2a4\\uc758 '\\ub514\\ud1a0'\\uac00 \\uc74c\\uc6d0 \\uacf5\\uac1c \\ud6c4 218\\uc77c \\ub9cc\\uc758 \\uae30\\ub85d\\uc73c\\ub85c \\uc138\\uacc4 \\ucd5c\\ub300 \\uc74c\\uc6d0 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\ud50c\\ub7ab\\ud3fc \\uc2a4\\ud3ec\\ud2f0\\ud30c\\uc774\\uc5d0\\uc11c 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d\\uc744 \\ub3cc\\ud30c\\ud588\\ub2e4\\uace0 \\ubc1d\\ud614\\ub2e4. \\uc18c\\uc18d\\uc0ac \\uc5b4\\ub3c4\\uc5b4\\ub294 'OMG'\\uc5d0 \\uc774\\uc5b4 \\uadf8\\ub8f9 \\ud1b5\\uc0b0 \\ub450 \\ubc88\\uc9f8 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\uace1\\uc774 \\ub41c \\ub274\\uc9c4\\uc2a4\\uc758 '\\ub514\\ud1a0'\\uac00 \\uc74c\\uc6d0 \\uacf5\\uac1c \\ud6c4 218\\uc77c \\ub9cc\\uc758 \\uae30\\ub85d\\uc73c\\ub85c \\uc138\\uacc4 \\ucd5c\\ub300 \\uc74c\\uc6d0 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\ud50c\\ub7ab\\ud3fc \\uc2a4\\ud3ec\\ud2f0\\ud30c\\uc774\\uc5d0\\uc11c 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d\\uc744 \\ub3cc\\ud30c\\ud588\\ub2e4\\uace0 \\ubc1d\\ud614\\ub2e4.\"," +
                " \"keywords\": [\"\\uae30\\ub85d\", \"\\ub274\\uc9c4\\uc2a4\", \"\\uc2a4\\ud2b8\\ub9ac\\ubc0d\", \"\\ud50c\\ub7ab\\ud3fc\", \"\\uc138\\uacc4\"], \"refs\": [\"https://news.kbs.co.kr/news/view.do?ncd=7734096\"]," +
                " \"date\": \"2023-07-27 00:22:00\"," +
                " \"url\": \"\"," +
                " \"lang\": 0}," +
                " {\"title\": [\"\\ub274\\uc9c4\\uc2a4 'ASAP', \\uae08\\ubc1c\\uc774 \\ub108\\ubb34\\ud574\\u2026\\ubbf8\\ub2c8 2\\uc9d1 '\\ube4c\\ubcf4\\ub4dc 200' 1\\uc704 \\uc720\\ub825 - \\ub274\\uc2dc\\uc2a4\", \"\\ub274\\uc9c4\\uc2a4, 'ASAP' \\ubba4\\ube44 \\uacf5\\uac1c\\u2026 \\uae08\\ubc1c \\uc694\\uc815+\\ubabd\\ud658\\uc801 \\ubd84\\uc704\\uae30 - \\uc544\\uc2dc\\uc544\\ud22c\\ub370\\uc774\", \"[\\uac00\\uc694\\uc18c\\uc2dd] \\ub274\\uc9c4\\uc2a4 'ASAP' \\ubba4\\uc9c1\\ube44\\ub514\\uc624 \\uacf5\\uac1c - \\uc5f0\\ud569\\ub274\\uc2a4\"]," +
                " \"summary\": \"\\ub274\\ub4dc\\ub86c \\uac78\\uadf8\\ub8f9 '\\ub274\\uc9c4\\uc2a4(NewJeans)'\\uac00 \\ubbf8\\ub2c8 2\\uc9d1 '\\uac9f \\uc5c5(Get Up)' \\uc5d0\\uc11c \\uae08\\ubc1c \\uc694\\uc815\\uc73c\\ub85c \\ubcc0\\ud55c \\ub2e4\\uc12f \\uba64\\ubc84\\ub4e4\\uc758 \\ubaa8\\uc2b5\\uc774 \\ub2f4\\uae34 '\\uae08\\ubc1c \\uc694\\uc815'\\uc758 \\ubba4\\uc9c1\\ube44\\ub514\\uc624\\ub97c 26\\uc77c 0\\uc2dc \\ud558\\uc774\\ube0c \\ub808\\uc774\\ube14\\uc988 \\uc720\\ud29c\\ube0c \\ucc44\\ub110\\uc5d0 \\uacf5\\uac1c, \\uae08\\ubc1c \\uc694\\uc815\\uc73c\\ub85c \\ubcc0\\uc2e0\\ud558\\uc5ec \\uc2e0\\uc2a4 \\uc0ac\\uc6b4\\ub4dc\\uc640 \\ub098\\ub978\\ud55c \\uc74c\\uc0c9\\uc774 \\uc8fc\\ub294 \\ubabd\\ud658\\uc801\\uc778 \\uace1 \\ubd84\\uc704\\uae30\\uc640 \\uc870\\ud654\\ub97c \\uc774\\ub8e8\\uba70 \\uc2e0\\uc2a4 \\uc0ac\\uc6b4\\ub4dc\\uc640 \\ub098\\ub978\\ud55c \\uc74c\\uc0c9\\uc774 \\uc8fc\\ub294 \\ubabd\\ud658\\uc801\\uc778 \\uace1 \\ubd84\\uc704\\uae30\\uc640 \\uc870\\ud654\\ub97c \\uc774\\ub8e8\\uba70 \\uc2e0\\uc2a4 \\uc0ac\\uc6b4\\ub4dc\\uc640 \\ub098\\ub978\\ud55c \\uc74c\\uc0c9\\uc774 \\uc8fc\\ub294 \\ubabd\\ud658\\uc801\\uc778 \\uace1 \\ubd84\\uc704\\uae30\\uc640 \\uc870\\ud654\\ub97c \\uc774\\ub8ec\\ub2e4.\"," +
                " \"keywords\": [\"\\uae08\\ubc1c\", \"\\ub274\\uc9c4\\uc2a4\"]," +
                " \"refs\": [\"https://mobile.newsis.com/view.html?ar_id=NISX20230726_0002391063\", \"https://www.asiatoday.co.kr/view.php?key=20230726010014924&ref=view_topnews\", \"https://www.yna.co.kr/view/AKR20230726059500005\"]," +
                " \"date\": \"2023-07-26 02:54:16\"," +
                " \"url\": \"//r.yna.co.kr/www/home_n/v02/img/bg_none_people01.jpg\"," +
                " \"lang\": 0}," +
                " {\"title\": [\"NH\\ud22c\\uc790 \\u201c\\ub274\\uc9c4\\uc2a4 \\ube4c\\ubcf4\\ub4dc \\ud56b100 \\ucc28\\ud2b8 66\\uc704, \\ud558\\uc774\\ube0c \\ud558\\ubc18\\uae30 \\uc704\\ubc84\\uc2a4 \\uc218\\uc775\\ud654\\u201d - \\ube44\\uc988\\ub2c8\\uc2a4\\ud3ec\\uc2a4\\ud2b8\"]," +
                " \"summary\": \"\\ud558\\ubc18\\uae30\\ube0c\\ube0c \\uc18c\\uc18d \\uc5ec\\ub7ec \\uc544\\ud2f0\\uc2a4\\ud2b8\\uac00 \\ud558\\ubc18\\uae30 \\uc2e0\\uaddc\\uc568\\ubc94 \\ubc1c\\ub9e4\\uc640 \\uc6d4\\ub4dc\\ud22c\\uc5b4\\ub97c \\uacc4\\ud68d\\ud558\\uace0 \\uc788\\ub294 \\uac00\\uc6b4\\ub370 \\uac78\\uadf8\\ub8f9 \\ub274\\uc9c4\\uc2a4\\uac00 \\uc2e0\\uace1\\uc73c\\ub85c \\ube4c\\ubcf4\\ub4dc \\ud56b100 \\ucc28\\ud2b8\\uc5d0\\uc11c 66\\uc704\\uc5d0 \\uc774\\ub984\\uc744 \\uc62c\\ub824 \\uc790\\uccb4 \\ucd5c\\uace0\\uc21c\\uc704\\ub97c \\uae30\\ub85d\\ud558\\uace0 BTS \\uc815\\uad6d\\uc758 \\uc194\\ub85c\\uace1\\ub3c4 \\uc9c4\\uc785\\uc774 \\uc608\\uc0c1\\ub418\\uc5b4 \\ud558\\uc774\\ube0c \\uc8fc\\uc2dd \\ub9e4\\uc218\\uc758\\uacac\\uc774 \\uc720\\uc9c0\\ub410\\uace0 \\ub274\\uc9c4\\uc2a4 \\uc2e0\\uace1\\uc73c\\ub85c \\ube4c\\ubcf4\\ub4dc \\ud56b100 \\ucc28\\ud2b8\\uc5d0\\uc11c 66\\uc704\\uc5d0 \\uc774\\ub984\\uc744 \\uc62c\\ub824 \\uc790\\uccb4 \\ucd5c\\uace0\\uc21c\\uc704\\ub97c \\uae30\\ub85d\\ud558\\uace0 \\uc790\\uccb4 \\ucd5c\\uace0\\uc21c\\uc704\\ub97c \\uae30\\ub85d\\ud558\\uace0 BTS \\uc815\\uad6d\\uc758 \\uc194\\ub85c\\uace1\\ub3c4 \\uc9c4\\uc785\\uc774 \\uc608\\uc0c1\\ub418\\uc5b4 \\ud558\\uc774\\ube0c \\uc8fc\\uc2dd \\ub9e4\\uc218\\uc758\\uacac\\uc774 \\uc720\\uc9c0\\ub410\\ub2e4.\"," +
                " \"keywords\": [\"\\ud22c\\uc5b4\", \"\\uc8fc\\uc2dd\", \"\\ub274\\uc9c4\\uc2a4\", \"\\uacc4\\ud68d\", \"\\uae30\\ub85d\"]," +
                " \"refs\": [\"https://www.businesspost.co.kr/BP?command=article_view&num=321623\"]," +
                " \"date\": \"2023-07-18 23:50:45\"," +
                " \"url\": \"https://www.businesspost.co.kr/news/photo/202307/20230719084805_45801.jpg\"," +
                " \"lang\": 0}," +
                " {\"title\": [\"\\ub124\\uc774\\ubc84 \\ub098\\uc6b0(NOW) \\uacbd\\uc7c1\\ub825 \\ud0a4\\uc6b0\\uae30 \\ud3ec\\uae30 \\ubabb \\ud574, \\ucd5c\\uc218\\uc5f0 K\\ud31d \\uc778\\uae30 \\uc801\\uadf9 \\ud65c\\uc6a9 - \\ube44\\uc988\\ub2c8\\uc2a4\\ud3ec\\uc2a4\\ud2b8\"]," +
                " \"summary\": \"25\\uc77c \\ub124\\uc774\\ubc84\\uc5d0 \\ub530\\ub974\\uba74 8\\uc6d4 \\ud55c \\ub2ec \\ub3d9\\uc548 \\ub098\\uc6b0\\uc5d0 \\ub274\\uc9c4\\uc2a4 \\ud2b9\\uc9d1\\uc73c\\ub85c K\\ud31d \\ucc28\\ud2b8\\uc1fc '\\uc5d4\\ud31d(NPOP)'\\uc758 \\ud504\\ub9ac\\ubdf0\\ub97c \\uc120\\ubcf4\\uc774\\uac8c \\ub418\\uba70, \\ucd5c\\uc218\\uc5f0 \\ub124\\uc774\\ubc84 \\ub300\\ud45c\\uc774\\uc0ac\\uac00 \\uc80a\\uc740 \\uc774\\uc6a9\\uc790 \\uacf5\\ub7b5\\uc5d0 \\ud798\\uc744 \\uc4f0\\uace0 \\uc788\\uace0, \\ucd5c\\uc218\\uc5f0 \\ub124\\uc774\\ubc84 \\ub300\\ud45c\\uc774\\uc0ac\\uac00 \\uc80a\\uc740 \\uc774\\uc6a9\\uc790 \\uacf5\\ub7b5\\uc5d0 \\ud798\\uc744 \\uc4f0\\uace0 \\uc788\\ub2e4.\"," +
                " \"keywords\": [\"\\ud504\\ub9ac\\ubdf0\", \"\\ubaa8\\ubc14\\uc77c\", \"\\ub274\\uc9c4\\uc2a4\", \"\\ud2b9\\uc9d1\", \"\\ub300\\ud45c\\uc774\\uc0ac\"], \"refs\": [\"https://www.businesspost.co.kr/BP?command=article_view&num=322447\"]," +
                " \"date\": \"2023-07-26 07:53:04\"," +
                " \"url\": \"https://www.businesspost.co.kr/news/photo/202301/20230105182650_117756.jpg\"," +
                " \"lang\": 0}," +
                " {\"title\": [\"[\\uac00\\uc694\\uc18c\\uc2dd] \\uc704\\ubc84\\uc2a4, \\ub274\\uc9c4\\uc2a4\\u00b7TXT \\u7f8e \\ub864\\ub77c\\ud314\\ub8e8\\uc790 \\ubb34\\ub300 \\uc0dd\\uc911\\uacc4 - \\uc5f0\\ud569\\ub274\\uc2a4\"]," +
                " \"summary\": \"25\\ubc84\\uc2a4\\uac00 \\ubbf8\\uad6d \\uc678 \\uc804 \\uc138\\uacc4 \\uc9c0\\uc5ed\\uc5d0 \\uadf8\\ub8f9 \\ub274\\uc9c4\\uc2a4\\uc640 \\ud22c\\ubaa8\\ub85c\\uc6b0\\ubc14\\uc774\\ud22c\\uac8c\\ub354\\uc758 '\\ub864\\ub77c\\ud314\\ub8e8\\uc790' \\ucd95\\uc81c \\ubb34\\ub300\\ub97c \\uc0dd\\uc911\\uacc4\\ud55c\\ub2e4\\uace0 25\\uc77c \\ubc1d\\ud614\\uc73c\\uba70 \\uc774\\ubc88 \\uc0dd\\uc911\\uacc4\\ub294 \\uc704\\ubc84\\uc2a4 \\uac00\\uc785\\uc790\\ub77c\\uba74 \\ub204\\uad6c\\ub098 \\ubb34\\ub8cc\\ub85c \\uc2dc\\uccad\\ud560 \\uc218 \\uc788\\uace0 \\uc2e4\\uc2dc\\uac04 \\ucc44\\ud305 \\uae30\\ub2a5\\ub3c4 \\uc81c\\uacf5\\ub41c\\ub2e4.\"," +
                " \"keywords\": [\"\\uc0dd\\uc911\\uacc4\", \"\\ub274\\uc9c4\\uc2a4\", \"\\ucd95\\uc81c\", \"\\ubc84\\uc2a4\", \"\\uc81c\\uacf5\"], \"refs\": [\"https://www.yna.co.kr/view/AKR20230725124100005\"]," +
                " \"date\": \"2023-07-25 07:01:47\"," +
                " \"url\": \"//r.yna.co.kr/www/home_n/v02/img/bg_none_people01.jpg\"," +
                " \"lang\": 0}]"

        return dataArticle

//        val keywordUrl:String = "$djangoServerUrl/api/keyword?query=$query"
//
//        val (_, response, result) = keywordUrl.httpGet().responseString()
//
//        when (result) {
//            is Result.Success -> {
//                val data = result.get()
//                if (data.isNotEmpty()) {
//                    val gson = Gson()
//                    val keywords = gson.fromJson(data, Keywords::class.java)
//
//                    val articleUrl:String = "$djangoServerUrl/api/articles?korean=${keywords.korean_keywords}&english=${keywords.english_keywords}"
//
//                    val (_, response, result2) = articleUrl.httpGet().responseString()
//
//                    val articleList = gson.fromJson(dataArticle, Array<Article>::class.java)
//
//                    return articleList?.toList() ?: emptyList()
//
//                    when (result) {
//                        is Result.Success -> {
//                            // val dataArticle = result2.get()
//                            if (dataArticle.isNotEmpty()) {
//                                var objectMapper:ObjectMapper = ObjectMapper()
//                                var articles: List<Article> = objectMapper.readValue(dataArticle, object: TypeReference<List<Article>>(){})
//                            } else {
//                                throw Exception("list is weird")
//                            }
//                        }
//                        is Result.Failure -> {
//                            print("error : ${result.getException()}")
//                            // Handle failure case (if needed)
//                        }
//                    }
//                } else {
//                    throw Exception("list is weird")
//                }
//            }
//            is Result.Failure -> {
//                print("error : ${result.getException()}")
//                // Handle failure case (if needed)
//            }
//        }
//        return listOf()
    }

    override fun dummyArticles(query: String): String {
        var newjeans:String = "[" +
                "{\"title\": [\"\\ub274\\uc9c4\\uc2a4 '\\ub514\\ud1a0', \\uc2a4\\ud3ec\\ud2f0\\ud30c\\uc774 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\ub3cc\\ud30c - KBS\\ub274\\uc2a4\"]," +
                " \"summary\": \"\\uc18c\\uc18d\\uc0ac \\uc5b4\\ub3c4\\uc5b4\\ub294 \\uadf8\\ub8f9 \\ud1b5\\uc0b0 \\ub450 \\ubc88\\uc9f8 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\uace1\\uc774 \\ub41c \\uadf8\\ub8f9 \\ud1b5\\uc0b0 \\ub450 \\ubc88\\uc9f8 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\uace1\\uc774 \\ub41c \\ub274\\uc9c4\\uc2a4\\uc758 '\\ub514\\ud1a0'\\uac00 \\uc74c\\uc6d0 \\uacf5\\uac1c \\ud6c4 218\\uc77c \\ub9cc\\uc758 \\uae30\\ub85d\\uc73c\\ub85c \\uc138\\uacc4 \\ucd5c\\ub300 \\uc74c\\uc6d0 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\ud50c\\ub7ab\\ud3fc \\uc2a4\\ud3ec\\ud2f0\\ud30c\\uc774\\uc5d0\\uc11c 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d\\uc744 \\ub3cc\\ud30c\\ud588\\ub2e4\\uace0 \\ubc1d\\ud614\\ub2e4. \\uc18c\\uc18d\\uc0ac \\uc5b4\\ub3c4\\uc5b4\\ub294 'OMG'\\uc5d0 \\uc774\\uc5b4 \\uadf8\\ub8f9 \\ud1b5\\uc0b0 \\ub450 \\ubc88\\uc9f8 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\uace1\\uc774 \\ub41c \\ub274\\uc9c4\\uc2a4\\uc758 '\\ub514\\ud1a0'\\uac00 \\uc74c\\uc6d0 \\uacf5\\uac1c \\ud6c4 218\\uc77c \\ub9cc\\uc758 \\uae30\\ub85d\\uc73c\\ub85c \\uc138\\uacc4 \\ucd5c\\ub300 \\uc74c\\uc6d0 \\uc2a4\\ud2b8\\ub9ac\\ubc0d \\ud50c\\ub7ab\\ud3fc \\uc2a4\\ud3ec\\ud2f0\\ud30c\\uc774\\uc5d0\\uc11c 4\\uc5b5 \\uc2a4\\ud2b8\\ub9ac\\ubc0d\\uc744 \\ub3cc\\ud30c\\ud588\\ub2e4\\uace0 \\ubc1d\\ud614\\ub2e4.\"," +
                " \"keywords\": [\"\\uae30\\ub85d\", \"\\ub274\\uc9c4\\uc2a4\", \"\\uc2a4\\ud2b8\\ub9ac\\ubc0d\", \"\\ud50c\\ub7ab\\ud3fc\", \"\\uc138\\uacc4\"], \"refs\": [\"https://news.kbs.co.kr/news/view.do?ncd=7734096\"]," +
                " \"date\": \"2023-07-27 00:22:00\"," +
                " \"url\": \"\"," +
                " \"lang\": 0}," +
                "{\"title\": [\"NewJeans Are Headed For The Best Anniversary Present Ever - Forbes\"], \"summary\": \"South Korean girl group NewJeans is set to celebrate their first anniversary in Japan with a pop-up store opening ceremony in Tokyo. The event will take place on April 6, 2023 in Tokyo, Japan. The group is on the brink of celebrating their first year in the music industry.\"," +
                " \"keywords\": [\"tokyo\", \"2023\", \"girl\", \"april\", \"korean\"], \"refs\": [\"https://www.forbes.com/sites/hughmcintyre/2023/07/26/newjeans-are-headed-for-the-best-anniversary-present-ever/\"]," +
                " \"date\": \"2023-07-26 22:00:16\"," +
                " \"url\": \"\"," +
                " \"lang\": 1}," +
                " {\"title\": [\"\\u201cThey Need To Be Shamed Globally\\u201d \\u2014 Korean Netizens Furious ... - Koreaboo\"]," +
                " \"summary\": \"A Korean gaming company, Project Moon, recently came under fire for firing a game illustrator. The issue started when male fans of the game Limbus Company took issue with the outfit on one of the characters. After discovering that the designer in charge of the character in question was male, they targeted a female story illustrator instead.\"," +
                " \"keywords\": [\"korean\", \"moon\", \"fans\", \"illustrator\", \"male\"]," +
                " \"refs\": [\"https://www.koreaboo.com/news/limbus-company-project-moon-canceled-fire-korean-netizens-reaction-fired-feminist-designer/\"]," +
                " \"date\": \"2023-07-26 10:39:18\"" +
                ", \"url\": \"\"," +
                " \"lang\": 1}," +
                " {\"title\": [\"\\ub124\\uc774\\ubc84 \\ub098\\uc6b0(NOW) \\uacbd\\uc7c1\\ub825 \\ud0a4\\uc6b0\\uae30 \\ud3ec\\uae30 \\ubabb \\ud574, \\ucd5c\\uc218\\uc5f0 K\\ud31d \\uc778\\uae30 \\uc801\\uadf9 \\ud65c\\uc6a9 - \\ube44\\uc988\\ub2c8\\uc2a4\\ud3ec\\uc2a4\\ud2b8\"]," +
                " \"summary\": \"25\\uc77c \\ub124\\uc774\\ubc84\\uc5d0 \\ub530\\ub974\\uba74 8\\uc6d4 \\ud55c \\ub2ec \\ub3d9\\uc548 \\ub098\\uc6b0\\uc5d0 \\ub274\\uc9c4\\uc2a4 \\ud2b9\\uc9d1\\uc73c\\ub85c K\\ud31d \\ucc28\\ud2b8\\uc1fc '\\uc5d4\\ud31d(NPOP)'\\uc758 \\ud504\\ub9ac\\ubdf0\\ub97c \\uc120\\ubcf4\\uc774\\uac8c \\ub418\\uba70, \\ucd5c\\uc218\\uc5f0 \\ub124\\uc774\\ubc84 \\ub300\\ud45c\\uc774\\uc0ac\\uac00 \\uc80a\\uc740 \\uc774\\uc6a9\\uc790 \\uacf5\\ub7b5\\uc5d0 \\ud798\\uc744 \\uc4f0\\uace0 \\uc788\\uace0, \\ucd5c\\uc218\\uc5f0 \\ub124\\uc774\\ubc84 \\ub300\\ud45c\\uc774\\uc0ac\\uac00 \\uc80a\\uc740 \\uc774\\uc6a9\\uc790 \\uacf5\\ub7b5\\uc5d0 \\ud798\\uc744 \\uc4f0\\uace0 \\uc788\\ub2e4.\"," +
                " \"keywords\": [\"\\ud504\\ub9ac\\ubdf0\", \"\\ubaa8\\ubc14\\uc77c\", \"\\ub274\\uc9c4\\uc2a4\", \"\\ud2b9\\uc9d1\", \"\\ub300\\ud45c\\uc774\\uc0ac\"], \"refs\": [\"https://www.businesspost.co.kr/BP?command=article_view&num=322447\"]," +
                " \"date\": \"2023-07-26 07:53:04\"," +
                " \"url\": \"https://www.businesspost.co.kr/news/photo/202301/20230105182650_117756.jpg\"," +
                " \"lang\": 0}," +
                " {\"title\": [\"\\ub274\\uc9c4\\uc2a4 'ASAP', \\uae08\\ubc1c\\uc774 \\ub108\\ubb34\\ud574\\u2026\\ubbf8\\ub2c8 2\\uc9d1 '\\ube4c\\ubcf4\\ub4dc 200' 1\\uc704 \\uc720\\ub825 - \\ub274\\uc2dc\\uc2a4\", \"\\ub274\\uc9c4\\uc2a4, 'ASAP' \\ubba4\\ube44 \\uacf5\\uac1c\\u2026 \\uae08\\ubc1c \\uc694\\uc815+\\ubabd\\ud658\\uc801 \\ubd84\\uc704\\uae30 - \\uc544\\uc2dc\\uc544\\ud22c\\ub370\\uc774\", \"[\\uac00\\uc694\\uc18c\\uc2dd] \\ub274\\uc9c4\\uc2a4 'ASAP' \\ubba4\\uc9c1\\ube44\\ub514\\uc624 \\uacf5\\uac1c - \\uc5f0\\ud569\\ub274\\uc2a4\"]," +
                " \"summary\": \"\\ub274\\ub4dc\\ub86c \\uac78\\uadf8\\ub8f9 '\\ub274\\uc9c4\\uc2a4(NewJeans)'\\uac00 \\ubbf8\\ub2c8 2\\uc9d1 '\\uac9f \\uc5c5(Get Up)' \\uc5d0\\uc11c \\uae08\\ubc1c \\uc694\\uc815\\uc73c\\ub85c \\ubcc0\\ud55c \\ub2e4\\uc12f \\uba64\\ubc84\\ub4e4\\uc758 \\ubaa8\\uc2b5\\uc774 \\ub2f4\\uae34 '\\uae08\\ubc1c \\uc694\\uc815'\\uc758 \\ubba4\\uc9c1\\ube44\\ub514\\uc624\\ub97c 26\\uc77c 0\\uc2dc \\ud558\\uc774\\ube0c \\ub808\\uc774\\ube14\\uc988 \\uc720\\ud29c\\ube0c \\ucc44\\ub110\\uc5d0 \\uacf5\\uac1c, \\uae08\\ubc1c \\uc694\\uc815\\uc73c\\ub85c \\ubcc0\\uc2e0\\ud558\\uc5ec \\uc2e0\\uc2a4 \\uc0ac\\uc6b4\\ub4dc\\uc640 \\ub098\\ub978\\ud55c \\uc74c\\uc0c9\\uc774 \\uc8fc\\ub294 \\ubabd\\ud658\\uc801\\uc778 \\uace1 \\ubd84\\uc704\\uae30\\uc640 \\uc870\\ud654\\ub97c \\uc774\\ub8e8\\uba70 \\uc2e0\\uc2a4 \\uc0ac\\uc6b4\\ub4dc\\uc640 \\ub098\\ub978\\ud55c \\uc74c\\uc0c9\\uc774 \\uc8fc\\ub294 \\ubabd\\ud658\\uc801\\uc778 \\uace1 \\ubd84\\uc704\\uae30\\uc640 \\uc870\\ud654\\ub97c \\uc774\\ub8e8\\uba70 \\uc2e0\\uc2a4 \\uc0ac\\uc6b4\\ub4dc\\uc640 \\ub098\\ub978\\ud55c \\uc74c\\uc0c9\\uc774 \\uc8fc\\ub294 \\ubabd\\ud658\\uc801\\uc778 \\uace1 \\ubd84\\uc704\\uae30\\uc640 \\uc870\\ud654\\ub97c \\uc774\\ub8ec\\ub2e4.\"," +
                " \"keywords\": [\"\\uae08\\ubc1c\", \"\\ub274\\uc9c4\\uc2a4\"]," +
                " \"refs\": [\"https://mobile.newsis.com/view.html?ar_id=NISX20230726_0002391063\", \"https://www.asiatoday.co.kr/view.php?key=20230726010014924&ref=view_topnews\", \"https://www.yna.co.kr/view/AKR20230726059500005\"]," +
                " \"date\": \"2023-07-26 02:54:16\"," +
                " \"url\": \"//r.yna.co.kr/www/home_n/v02/img/bg_none_people01.jpg\"," +
                " \"lang\": 0}," +
                " {\"title\": [\"[\\uac00\\uc694\\uc18c\\uc2dd] \\uc704\\ubc84\\uc2a4, \\ub274\\uc9c4\\uc2a4\\u00b7TXT \\u7f8e \\ub864\\ub77c\\ud314\\ub8e8\\uc790 \\ubb34\\ub300 \\uc0dd\\uc911\\uacc4 - \\uc5f0\\ud569\\ub274\\uc2a4\"]," +
                " \"summary\": \"25\\ubc84\\uc2a4\\uac00 \\ubbf8\\uad6d \\uc678 \\uc804 \\uc138\\uacc4 \\uc9c0\\uc5ed\\uc5d0 \\uadf8\\ub8f9 \\ub274\\uc9c4\\uc2a4\\uc640 \\ud22c\\ubaa8\\ub85c\\uc6b0\\ubc14\\uc774\\ud22c\\uac8c\\ub354\\uc758 '\\ub864\\ub77c\\ud314\\ub8e8\\uc790' \\ucd95\\uc81c \\ubb34\\ub300\\ub97c \\uc0dd\\uc911\\uacc4\\ud55c\\ub2e4\\uace0 25\\uc77c \\ubc1d\\ud614\\uc73c\\uba70 \\uc774\\ubc88 \\uc0dd\\uc911\\uacc4\\ub294 \\uc704\\ubc84\\uc2a4 \\uac00\\uc785\\uc790\\ub77c\\uba74 \\ub204\\uad6c\\ub098 \\ubb34\\ub8cc\\ub85c \\uc2dc\\uccad\\ud560 \\uc218 \\uc788\\uace0 \\uc2e4\\uc2dc\\uac04 \\ucc44\\ud305 \\uae30\\ub2a5\\ub3c4 \\uc81c\\uacf5\\ub41c\\ub2e4.\"," +
                " \"keywords\": [\"\\uc0dd\\uc911\\uacc4\", \"\\ub274\\uc9c4\\uc2a4\", \"\\ucd95\\uc81c\", \"\\ubc84\\uc2a4\", \"\\uc81c\\uacf5\"], \"refs\": [\"https://www.yna.co.kr/view/AKR20230725124100005\"]," +
                " \"date\": \"2023-07-25 07:01:47\"," +
                " \"url\": \"//r.yna.co.kr/www/home_n/v02/img/bg_none_people01.jpg\"," +
                " \"lang\": 0}," +
                " {\"title\": [\"NewJeans Beats One Of Blackpink\\u2019s Most Impressive Records - Forbes\"]," +
                " \"summary\": \"Korean girl group NewJeans has emerged as one of the hottest and most exciting new acts. Despite debuting just a year ago, the group has already achieved a string of high-profile awards. The group will perform at the 2022 KBS Song Festival in Seoul.\"," +
                " \"keywords\": [\"seoul\", \"2022\", \"girl\", \"hottest\", \"newjeans\"]," +
                " \"refs\": [\"https://www.forbes.com/sites/hughmcintyre/2023/07/21/newjeans-beats-one-of-blackpinks-most-impressive-records/\"]," +
                " \"date\": \"2023-07-21 19:39:08\"," +
                " \"url\": \"\"," +
                " \"lang\": 1}," +
                " {\"title\": [\"NH\\ud22c\\uc790 \\u201c\\ub274\\uc9c4\\uc2a4 \\ube4c\\ubcf4\\ub4dc \\ud56b100 \\ucc28\\ud2b8 66\\uc704, \\ud558\\uc774\\ube0c \\ud558\\ubc18\\uae30 \\uc704\\ubc84\\uc2a4 \\uc218\\uc775\\ud654\\u201d - \\ube44\\uc988\\ub2c8\\uc2a4\\ud3ec\\uc2a4\\ud2b8\"]," +
                " \"summary\": \"\\ud558\\ubc18\\uae30\\ube0c\\ube0c \\uc18c\\uc18d \\uc5ec\\ub7ec \\uc544\\ud2f0\\uc2a4\\ud2b8\\uac00 \\ud558\\ubc18\\uae30 \\uc2e0\\uaddc\\uc568\\ubc94 \\ubc1c\\ub9e4\\uc640 \\uc6d4\\ub4dc\\ud22c\\uc5b4\\ub97c \\uacc4\\ud68d\\ud558\\uace0 \\uc788\\ub294 \\uac00\\uc6b4\\ub370 \\uac78\\uadf8\\ub8f9 \\ub274\\uc9c4\\uc2a4\\uac00 \\uc2e0\\uace1\\uc73c\\ub85c \\ube4c\\ubcf4\\ub4dc \\ud56b100 \\ucc28\\ud2b8\\uc5d0\\uc11c 66\\uc704\\uc5d0 \\uc774\\ub984\\uc744 \\uc62c\\ub824 \\uc790\\uccb4 \\ucd5c\\uace0\\uc21c\\uc704\\ub97c \\uae30\\ub85d\\ud558\\uace0 BTS \\uc815\\uad6d\\uc758 \\uc194\\ub85c\\uace1\\ub3c4 \\uc9c4\\uc785\\uc774 \\uc608\\uc0c1\\ub418\\uc5b4 \\ud558\\uc774\\ube0c \\uc8fc\\uc2dd \\ub9e4\\uc218\\uc758\\uacac\\uc774 \\uc720\\uc9c0\\ub410\\uace0 \\ub274\\uc9c4\\uc2a4 \\uc2e0\\uace1\\uc73c\\ub85c \\ube4c\\ubcf4\\ub4dc \\ud56b100 \\ucc28\\ud2b8\\uc5d0\\uc11c 66\\uc704\\uc5d0 \\uc774\\ub984\\uc744 \\uc62c\\ub824 \\uc790\\uccb4 \\ucd5c\\uace0\\uc21c\\uc704\\ub97c \\uae30\\ub85d\\ud558\\uace0 \\uc790\\uccb4 \\ucd5c\\uace0\\uc21c\\uc704\\ub97c \\uae30\\ub85d\\ud558\\uace0 BTS \\uc815\\uad6d\\uc758 \\uc194\\ub85c\\uace1\\ub3c4 \\uc9c4\\uc785\\uc774 \\uc608\\uc0c1\\ub418\\uc5b4 \\ud558\\uc774\\ube0c \\uc8fc\\uc2dd \\ub9e4\\uc218\\uc758\\uacac\\uc774 \\uc720\\uc9c0\\ub410\\ub2e4.\"," +
                " \"keywords\": [\"\\ud22c\\uc5b4\", \"\\uc8fc\\uc2dd\", \"\\ub274\\uc9c4\\uc2a4\", \"\\uacc4\\ud68d\", \"\\uae30\\ub85d\"]," +
                " \"refs\": [\"https://www.businesspost.co.kr/BP?command=article_view&num=321623\"]," +
                " \"date\": \"2023-07-18 23:50:45\"," +
                " \"url\": \"https://www.businesspost.co.kr/news/photo/202307/20230719084805_45801.jpg\"," +
                " \"lang\": 0}" +
                "]"
        if (query == "뉴진스"){
            return newjeans
        }else if(query == "미군 탈영"){

        }else if(query == "일론 머스크"){

        }
        print("\n\n\nTlqkf")
        return newjeans
    }
}