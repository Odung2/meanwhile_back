package madcamp.four.meanwhile.mapper

import madcamp.four.meanwhile.model.Bookmark
import org.apache.ibatis.annotations.Mapper

@Mapper
interface BookmarkMapper {
    public fun addBookmark(userId: Long, refTitle: String, refLink: String)

    public fun getBookmarkList(userId: Long): List<Bookmark>

    public fun deleteBookmark(userId: Long, refLink:String)
}