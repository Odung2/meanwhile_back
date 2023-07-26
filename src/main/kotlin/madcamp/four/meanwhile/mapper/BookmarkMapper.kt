package madcamp.four.meanwhile.mapper

import madcamp.four.meanwhile.model.Bookmark
import org.apache.ibatis.annotations.Mapper
import org.mybatis.spring.annotation.MapperScan

@Mapper
interface BookmarkMapper {
    public fun addBookmark(bookmark: Bookmark)

    public fun getBookmarkList(signupId: String): List<Bookmark>

    public fun deleteBookmark(userId: Long, refLink:String)
}