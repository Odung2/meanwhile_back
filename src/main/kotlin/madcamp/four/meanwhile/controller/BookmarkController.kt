package madcamp.four.meanwhile.controller

import com.fasterxml.jackson.databind.ObjectMapper
import madcamp.four.meanwhile.model.Bookmark
import madcamp.four.meanwhile.model.LikeData
import madcamp.four.meanwhile.security.JwtTokenUtil
import madcamp.four.meanwhile.service.BookmarkService
import madcamp.four.meanwhile.user_exception.NotValidTokenException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@CrossOrigin(allowedHeaders = ["*"])
@Controller
class BookmarkController {

    @Autowired
    lateinit var bookmarkService: BookmarkService

    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil

    var objectMapper: ObjectMapper = ObjectMapper()

    @PostMapping("/add_bookmark")
    public fun addBookmark(
            @RequestHeader("Authorization") token:String,
            @RequestBody likeData: LikeData
//            @RequestParam(value="refLink", required=true) refLink:String
    ): ResponseEntity<String>
    {
        try {
            if(!jwtTokenUtil.validateToken(token.substring(7)))  throw NotValidTokenException("token is not valid, cannot get account list")

            var userId:Long = jwtTokenUtil.extractUserId(token.substring(7))
            var refLink = likeData.references
            var refTitle = likeData.refTitle
//            print(refTitle)
//            print(userId)
            bookmarkService.addBookmark(userId, refLink, refTitle)

            return ResponseEntity.ok("Successfully add the bookmark")
        }
        catch (e:Exception)
        {
            return ResponseEntity.badRequest().body(e.message)
        }
    }

    @GetMapping("/get_bookmark_list")
    public fun getBookmarkList(
            @RequestHeader("Authorization") token:String,
    ): ResponseEntity<String>
    {
        try {
            if(!jwtTokenUtil.validateToken(token.substring(7)))  throw NotValidTokenException("token is not valid, cannot get account list")

            var userId:Long = jwtTokenUtil.extractUserId(token.substring(7))
            var bookmarks:List<Bookmark> = bookmarkService.getBookmarkList(userId)
            var json:String = objectMapper.writeValueAsString(bookmarks)
            return ResponseEntity.ok(json)
        }
        catch (e:Exception)
        {
            return ResponseEntity.badRequest().body(e.message)
        }
    }

//    @PostMapping("/likeArticle")
//    fun likeArticles(@RequestBody likeData: LikeData): ResponseEntity<String>{
//        try{
//            // LikeData에서 받은 정보를 Like 엔티티로 변환하여 저장
//            val like = LikeData(
//                jwtUtilToken = likeData.jwtUtilToken,
//                refTitle = likeData.refTitle,
//                references = likeData.references
//            )
//            like.jwtUtilToken
//
//            return ResponseEntity.ok("Like data saved successfully!")
//        } catch (e:Exception){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to save like data.")
//        }
//    }

    @GetMapping("/delete_bookmark")
    public fun deleteBookmark(
            @RequestHeader("Authorization") token:String,
            @RequestParam(value="refLink", required=true) refLink:String
    ):ResponseEntity<String>
    {
        try {
            if(!jwtTokenUtil.validateToken(token.substring(7)))  throw NotValidTokenException("token is not valid, cannot get account list")

            var userId:Long = jwtTokenUtil.extractUserId(token.substring(7))
            bookmarkService.deleteBookmark(userId, refLink)
            return ResponseEntity.ok("Successfully delete the bookmark")
        }
        catch (e:Exception)
        {
            return ResponseEntity.badRequest().body(e.message)
        }
    }
}