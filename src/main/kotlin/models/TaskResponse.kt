import kotlinx.serialization.Serializable

@Serializable
data class TaskResponse(val id:Int,val title: String,var completed: Boolean = false)