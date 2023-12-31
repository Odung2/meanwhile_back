package madcamp.four.meanwhile.model

import com.fasterxml.jackson.annotation.JsonFormat
import lombok.Data
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Id

@Data
class Article(
        @Id
        val title:String,
        val summary: String,
        val keywords: String,
        val refs: String,
        val url: String,
        val date: String,
        val lang: Int,

)