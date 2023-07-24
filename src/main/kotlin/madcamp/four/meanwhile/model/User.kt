package madcamp.four.meanwhile.model

import lombok.AllArgsConstructor
import lombok.Data
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id



@Entity
@Data
@AllArgsConstructor
data class User (
    @Id
    @GeneratedValue
    var userId:Long,
    var username:String,
    var signupId:String,
    var password:String,
    var email:String,
)
