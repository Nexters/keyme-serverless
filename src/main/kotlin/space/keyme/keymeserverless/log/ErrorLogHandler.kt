package space.keyme.keymeserverless.log

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.function.Function

@Component
class ErrorLogHandler: Function<String, String> {
    override fun apply(t: String): String {
        return t.reversed()
    }
}