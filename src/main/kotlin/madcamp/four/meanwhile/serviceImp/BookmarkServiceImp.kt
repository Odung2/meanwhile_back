package madcamp.four.meanwhile.serviceImp

import madcamp.four.meanwhile.mapper.BookmarkMapper
import madcamp.four.meanwhile.model.Bookmark
import madcamp.four.meanwhile.service.BookmarkService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import java.awt.print.Book

@Service
class BookmarkServiceImp: BookmarkService {

    @Autowired
    lateinit var bookmarkMapper: BookmarkMapper

    //구현 기능
    //1. like button 눌렀을 때 -> userid 별로 추가

    override fun addBookmark(bookmark: Bookmark) {
//        print("in Service IMPP\n")
        var bookmark1:Bookmark = Bookmark(0, 1, "refLink", "refTitle")
        bookmarkMapper.addBookmark(bookmark1)
        print("in Service IMPP1\n")

        print(bookmark.refTitle)
        print(bookmark.refLink)
        print(bookmark.userId)
        print(bookmark.bookmarkId)
        bookmarkMapper.addBookmark(bookmark)
//        bookmarkMapper.addBookmark()
        print("in Service IMPP2\n")

    }

    //2. like list userid로 불러오기

    override fun getBookmarkList(userId: Long): List<Bookmark> {
        return bookmarkMapper.getBookmarkList(userId)
    }

    override fun deleteBookmark(userId: Long, refLink: String) {
        bookmarkMapper.deleteBookmark(userId, refLink)
    }
}