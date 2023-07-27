package madcamp.four.meanwhile.model

import com.google.gson.Gson

data class Keywords(
        val korean_keywords: String,
        val english_keywords: String
){
    companion object {
        fun fromJson(jsonString: String): Keywords? {
            return try {
                val gson = Gson()
                gson.fromJson(jsonString, Keywords::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
