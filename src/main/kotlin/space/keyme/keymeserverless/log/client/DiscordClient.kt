package space.keyme.keymeserverless.log.service

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@FeignClient(name = "discord", url = "\${feign.discord.url}")
interface DiscordClient {
    @PostMapping
    fun getUserProfile(@RequestBody accessToken: String?): Unit
}