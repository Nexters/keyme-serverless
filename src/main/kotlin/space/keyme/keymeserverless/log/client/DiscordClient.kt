package space.keyme.keymeserverless.log.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import space.keyme.keymeserverless.log.dto.DiscordCreateMessageRequest


@FeignClient(name = "discord", url = "\${feign.discord.url}")
interface DiscordClient {
    @PostMapping
    fun postMessage(@RequestBody request: DiscordCreateMessageRequest): Unit
}