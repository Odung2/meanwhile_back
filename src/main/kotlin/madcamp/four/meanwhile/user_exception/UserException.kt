package madcamp.four.meanwhile.user_exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


class NotValidTokenException(message:String):RuntimeException(message){}

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NotValidTokenException::class)
    fun handleNotValidTokenException(ex: NotValidTokenException): ResponseEntity<String> {
        print("Exception occurred because of : ${ex.message}")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }
}