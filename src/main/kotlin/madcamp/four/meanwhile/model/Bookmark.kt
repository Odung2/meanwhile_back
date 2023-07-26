package madcamp.four.meanwhile.model

import lombok.AllArgsConstructor
import lombok.Data
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
@Entity
@Data
@AllArgsConstructor
class Bookmark (

    @Id
    @GeneratedValue
    var bookmarkId: Long,
    var userId: Long,
    var refLink: String,
    var refTitle: String,

)

