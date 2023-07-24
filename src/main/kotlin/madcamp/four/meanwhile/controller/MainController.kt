package madcamp.four.meanwhile.controller

import madcamp.four.meanwhile.service.KakaoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

//import
@CrossOrigin(allowedHeaders = ["*"])
@Controller

class MainController {

    @Autowired
    lateinit var kakaoService: KakaoService

    @GetMapping("/kakao/sign_in")
//@GetMapping("/inhere")
    public fun kakaoSignIn(@RequestParam("code") code:String) :String
    {
        val token: String = kakaoService.execKakaoLogin(code)

        return "redirect:http://localhost:8080/kakao?data=$token"
    }

    @GetMapping("/kakao")
    public fun kakaoLoginDone(@RequestParam(value = "data", required = true) token:String):ResponseEntity<String>
    {
        return ResponseEntity.ok(token)
    }

}

