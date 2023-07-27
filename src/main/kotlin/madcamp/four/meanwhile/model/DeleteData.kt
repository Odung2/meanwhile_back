package madcamp.four.meanwhile.model

import lombok.AllArgsConstructor
import lombok.Data

@Data
@AllArgsConstructor
data class DeleteData(
    val bookmarkId: Long,
    val refTitle: String,
    val refLink: String
)
