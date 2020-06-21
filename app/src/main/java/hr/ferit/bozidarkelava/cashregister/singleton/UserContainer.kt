package hr.ferit.bozidarkelava.cashregister.singleton

object UserContainer {

    private var userName: String = ""

    fun setUserName(userName: String) {
        this.userName = userName
    }
}