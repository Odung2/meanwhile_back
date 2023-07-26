package madcamp.four.meanwhile.service

import madcamp.four.meanwhile.model.User


interface UserService {
    public fun getUserById(id:Long): User

    public fun getUserBySignupId(signupId:String):User?

    public fun insertUser(user:User)

    public fun getUserIdBySignupId(signupId: String):Long

}