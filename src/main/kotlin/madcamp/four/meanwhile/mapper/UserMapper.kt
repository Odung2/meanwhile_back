package madcamp.four.meanwhile.mapper

import madcamp.four.meanwhile.model.User
import org.apache.ibatis.annotations.Mapper

@Mapper
interface UserMapper {

    public fun getUserById(id:Long): User

    public fun getUserBySignUpId(signupId:String): User?

    public fun insertUser(user: User)

    public fun isUserIdAlreadyExist(signupId:String):Long //없으면 0 반환, 있으면 1 userId 반환.

    public fun testMapper(signupId:String):User

    public fun updateUser(user: User)

}