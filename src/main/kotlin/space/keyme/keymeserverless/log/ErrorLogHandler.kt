package space.keyme.keymeserverless.log

import com.amazonaws.services.lambda.runtime.events.CloudWatchLogsEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import space.keyme.keymeserverless.log.dto.CloudWatchLogObject
import space.keyme.keymeserverless.log.client.DiscordClient
import space.keyme.keymeserverless.log.dto.DiscordCreateMessageRequest
import space.keyme.keymeserverless.log.dto.DiscordEmbededField
import space.keyme.keymeserverless.log.dto.DiscordEmbededMessage
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import java.util.Base64
import java.util.zip.GZIPInputStream


@Component
class ErrorLogHandler(
    private val objectMapper: ObjectMapper,
    private val discordClient: DiscordClient
) {
    @Bean
    fun errLogSendToDiscord(): (CloudWatchLogsEvent) -> Unit {
        return { it ->
            // 요청 파싱
            val base64EncodedData = it.awsLogs.data
            val decodedData = Base64.getDecoder().decode(base64EncodedData)
            val inputStream = GZIPInputStream(ByteArrayInputStream(decodedData))
            val jsonString = inputStream.bufferedReader(StandardCharsets.UTF_8).use { it.readText() }
            val logObject = objectMapper.readValue(jsonString, CloudWatchLogObject::class.java)

            /*
                에러형식
                [%-5level] --- %msg --- %X{traceId} --- %X{requestUri} --- %d{yyyy:MM:dd HH:mm:ss.SSS, ${logback.timezone:-Asia/Seoul}} --- [%thread] --- [%C %M] --- [%logger{40}:%line] %n
            */
            val allMessage = logObject.logEvents
                ?.map { it.message }
                ?.map { it.split("---") }
                ?.map {
                    DiscordEmbededMessage(
                        title = if (it[0].contains("ERROR")) "🔥 ERROR" else "⚠️ WARN",
                        description = it[1], // Message
                        fields = arrayListOf(
                            DiscordEmbededField("▶️ TraceId", it[2], ""),
                            DiscordEmbededField("▶️ Time", it[4], ""),
                            DiscordEmbededField("▶️ Uri", it[3], ""),
                            DiscordEmbededField("", "", ""),
                            DiscordEmbededField("◾️ Thread", it[5], ""),
                            DiscordEmbededField("◾️ Handler", it[6], ""),
                            DiscordEmbededField("◾️ Line", it[7], ""),
                        )
                    )
                }

            val postMessage = DiscordCreateMessageRequest(
                content = "Error/Warn Log",
                embeds = allMessage ?: arrayListOf()
            )

            discordClient.postMessage(postMessage)
        }
    }
}
