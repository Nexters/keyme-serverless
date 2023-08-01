package space.keyme.keymeserverless.log

import com.amazonaws.services.lambda.runtime.events.CloudWatchLogsEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import space.keyme.keymeserverless.log.dto.CloudWatchLogObject
import space.keyme.keymeserverless.log.client.DiscordClient
import space.keyme.keymeserverless.log.dto.DiscordCreateMessageRequest
import space.keyme.keymeserverless.log.dto.DiscordEmbededMessage
import java.util.Base64

@Component
class ErrorLogHandler(
    private val objectMapper: ObjectMapper,
    private val discordClient: DiscordClient
) {
    @Bean
    fun errLogSendToDiscord(): (CloudWatchLogsEvent) -> Unit {
        return {
            val base64EncodedData = it.awsLogs.data
            val decodedString = String(bytes = Base64.getDecoder().decode(base64EncodedData))
            val logObject = objectMapper.readValue(decodedString, CloudWatchLogObject::class.java)
            val allMessage = logObject.logEvents?.map { it.message }?.joinToString("\n\n")
            val postMessage = DiscordCreateMessageRequest(
                content = "🔥 Error Log",
                embeds = arrayListOf(
                    DiscordEmbededMessage(
                        title = "에러내용",
                        description = allMessage!!
                    )
                ),
            )

            discordClient.postMessage(postMessage)
        }
    }
}
