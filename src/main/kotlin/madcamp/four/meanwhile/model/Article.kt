package madcamp.four.meanwhile.model

import lombok.Data
import javax.persistence.Entity
import javax.persistence.Id

@Data
class Article(
        @Id
        val articleId: Long,
        val summary: String,
        val keywords: List<String>,
        val references: List<String>,
        val publishTime: String,
        val imageLink: String
)