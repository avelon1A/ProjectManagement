

sealed class RegisterValidation(){
    object  Succes: RegisterValidation()
    data class Failed(val message:String):RegisterValidation()
}
data class RegisterFeildState(
    val email:RegisterValidation,
    val password:RegisterValidation
)
