package madcamp.four.meanwhile.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import javax.annotation.PostConstruct
import javax.crypto.spec.SecretKeySpec


@Component
class JwtTokenUtil {
    @Value("\${app.jwtSecret}")
    private lateinit var jwtSecret:String

    @Value("\${app.jwtExpirationsMs}")
    private lateinit var jwtExpirationMs : String
    private lateinit var key:Key
    @PostConstruct
    public fun init() {
        val decodedKey = Base64.getDecoder().decode(jwtSecret)
        key = SecretKeySpec(decodedKey, "HmacSHA512")
    }

    public fun validateToken(token: String): Boolean {
        return try {
            // Parse the token and verify the signature using the secret key
            val claimsJws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret!!.toByteArray()))
                    .build()
                    .parseClaimsJws(token)

            // The token signature is valid, and the token is successfully parsed
            true
        } catch (e: Exception) {
            // Token validation failed
            false
        }
    }

    fun generateToken(authentication: Authentication?): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationMs.toInt())
        val principal = Optional.ofNullable(authentication)
                .map { obj: Authentication -> obj.principal }
                .map { obj: Any -> obj.toString() }
                .orElseThrow { IllegalArgumentException("Authentication or Principal is null") }
        return Jwts.builder()
                .setSubject(principal)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret!!.toByteArray())
                .compact()
    }

    fun extractUserId(token: String?): Long {
        val subject = Optional.ofNullable(token)
                .map { t: String? -> Jwts.parser().setSigningKey(jwtSecret!!.toByteArray()).parseClaimsJws(t).body.subject }
                .orElseThrow { IllegalArgumentException("Token is null") }
        return subject.toLong()
    }

    companion object {
        fun isExpired(token: String?, secretKey: String): Boolean {
            return Jwts.parser().setSigningKey(secretKey.toByteArray()).parseClaimsJws(token)
                    .body.expiration.before(Date())
        }
    }
}

