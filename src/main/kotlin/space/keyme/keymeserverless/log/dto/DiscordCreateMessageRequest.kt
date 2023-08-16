package space.keyme.keymeserverless.log.dto

data class DiscordCreateMessageRequest(
    val content: String,
    val embeds: List<DiscordEmbededMessage>,
)

data class DiscordEmbededMessage(
    val title: String,
    val description: String,
    val fields: List<DiscordEmbededField>
)

data class DiscordEmbededField(
    val name: String,
    val value: String,
    val text: String
)
