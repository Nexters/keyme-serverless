package space.keyme.keymeserverless

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class KeymeServerlessApplication

fun main(args: Array<String>) {
	runApplication<KeymeServerlessApplication>(*args)
}
