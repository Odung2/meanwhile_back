package madcamp.four.meanwhile.model

import lombok.AllArgsConstructor
import lombok.Data
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Data
@AllArgsConstructor
class Bookmark (

    @Id
    @GeneratedValue
    public var bookmarkId: Long,

    @Column
    public var userId: Long,

    @Column
    public var refLink: String,

)