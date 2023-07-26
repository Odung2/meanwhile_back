package madcamp.four.meanwhile.serviceImp

import madcamp.four.meanwhile.mapper.BookmarkMapper
import madcamp.four.meanwhile.model.Bookmark
import madcamp.four.meanwhile.service.BookmarkService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping

@Service
class BookmarkServiceImp: BookmarkService {

    @Autowired
    lateinit var bookmarkMapper: BookmarkMapper

    //구현 기능
    //1. like button 눌렀을 때 -> userid 별로 추가

    override fun addBookmark(bookmark: Bookmark) {
        print(bookmark.userId)
        print(bookmark.refTitle)
        print(bookmark.refLink)
//        bookmarkMapper.addBookmark(userId, refLink, refTitle)
        bookmarkMapper.addBookmark(bookmark)


    }

    //2. like list userid로 불러오기

    override fun getBookmarkList(userId: Long): List<Bookmark> {
        return bookmarkMapper.getBookmarkList(userId)
    }

    override fun deleteBookmark(userId: Long, refLink: String) {
        bookmarkMapper.deleteBookmark(userId, refLink)
    }
}