package madcamp.four.meanwhile.service

import madcamp.four.meanwhile.model.Bookmark

interface BookmarkService {

    public fun addBookmark(bookmark: Bookmark)

    public fun getBookmarkList(signupId:String):List<Bookmark>

    public fun deleteBookmark(signupId: String, bookmarkId:Long)
}