package madcamp.four.meanwhile.model

import com.fasterxml.jackson.annotation.JsonFormat
import lombok.Data
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Id

@Data
class ArticleTemp(
        @Id
        val articleId: Long,
        val refTitles:List<String>,
        val summary: String,
        val keywords: List<String>,
        val references: List<String>,
        val imageLink: String,
        val publishTime: String

)