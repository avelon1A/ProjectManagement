
import android.util.Patterns

fun emailValid(email:String):RegisterValidation{
    if (email.isEmpty()){
        return RegisterValidation.Failed("email cannot be empty")

    }
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
    {
        return RegisterValidation.Failed("not a valid email")
    }
    return RegisterValidation.Succes

}

fun passwordValidate(password:String):RegisterValidation{
    if (password.isEmpty()){
        return RegisterValidation.Failed("password cannot be empty")

    }
    if (password.length < 6){
        return RegisterValidation.Failed("password must be equal to 6 to more than 6")

    }

    return RegisterValidation.Succes

}