package space.keyme.keymeserverless.log.dto

data class DiscordCreateMessageRequest(
    val content: String,
    val embeds: ArrayList<DiscordEmbededMessage>,
)

data class DiscordEmbededMessage(
    val title: String,
    val description: String
)
