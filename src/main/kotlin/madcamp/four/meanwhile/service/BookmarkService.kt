package madcamp.four.meanwhile.service

import madcamp.four.meanwhile.model.Bookmark

interface BookmarkService {

    public fun addBookmark(userId:Long, refLink:String)

    public fun getBookmarkList(userId:Long):List<Bookmark>

    public fun deleteBookmark(userId: Long, refLink:String)
}