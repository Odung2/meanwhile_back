package madcamp.four.meanwhile.model

import lombok.AllArgsConstructor
import lombok.Data

@Data
@AllArgsConstructor
data class LikeData(
//        val jwtUtilToken: String,
        val refTitle: String,
        val refLink: String
)
