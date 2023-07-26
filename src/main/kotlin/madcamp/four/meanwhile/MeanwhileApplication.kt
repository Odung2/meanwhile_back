package madcamp.four.meanwhile

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.ComponentScan

@EnableCaching
@SpringBootApplication
@MapperScan(basePackages = ["madcamp.four.meanwhile.mapper"])
@ComponentScan(basePackages = ["madcamp.four.meanwhile.config", "madcamp.four.meanwhile.controller", "madcamp.four.meanwhile.mapper",
        "madcamp.four.meanwhile.model", "madcamp.four.meanwhile.security", "madcamp.four.meanwhile.service", "madcamp.four.meanwhile.serviceImp",
    "madcamp.four.meanwhile.model", "madcamp.four.meanwhile.user_exception"])
class MeanwhileApplication

fun main(args: Array<String>) {
    runApplication<MeanwhileApplication>(*args)
}
