package space.keyme.keymeserverless.log

data class CloudWatchLogObject(
    val logGroup: Long,
    val logStream: String,
    val subscriptionFilters: ArrayList<String>?,
    val logEvents: ArrayList<CloudWatchLog>?,
)

data class CloudWatchLog(
    val id: String,
    val timestamp: Long,
    val message: String
)