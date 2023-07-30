package space.keyme.keymeserverless

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KeymeServerlessApplication

fun main(args: Array<String>) {
	runApplication<KeymeServerlessApplication>(*args)
}
