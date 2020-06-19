package hr.ferit.bozidarkelava.cashregister.Singleton

object UserContainer {

    private var userName: String = ""

    fun setUserName(userName: String) {
        this.userName = userName
    }
}