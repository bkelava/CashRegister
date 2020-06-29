package hr.ferit.bozidarkelava.cashregister.containers

object UserContainer {

    private var userName: String = ""
    private var email: String =""

    fun setUserName(userName: String) {
        this.userName = userName
    }

    fun setEmail (email: String)
    {
        this.email=email
    }

    fun getEmail(): String
    {
        return this.email
    }
}