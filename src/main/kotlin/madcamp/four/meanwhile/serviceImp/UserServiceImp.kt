package madcamp.four.meanwhile.serviceImp

import madcamp.four.meanwhile.mapper.UserMapper
import madcamp.four.meanwhile.model.User
import madcamp.four.meanwhile.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@Service
class UserServiceImp: UserService {
    @Autowired
    lateinit var userMapper: UserMapper

    private final var passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()


    override fun getUserById(id: Long):User {
        return userMapper.getUserById(id)
    }

    override fun getUserBySignupId(signupId: String): User? {
        return userMapper.getUserBySignUpId(signupId)
    }

    override fun insertUser(user: User) {
        if(!user.signupId.equals("") && !user.password.equals(""))
        {
            user.password = passwordEncoder.encode(user.password)
            userMapper.insertUser(user)
        }
    }

}