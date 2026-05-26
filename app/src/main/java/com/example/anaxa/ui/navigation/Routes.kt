package com.example.anaxa.ui.navigation

object Routes {
    const val AUTH = "auth"
    const val HOME = "home"
    const val LOTS = "lots/{gameId}"
    const val LOT_DETAIL = "lot/{lotId}"
    const val CREATE_LOT = "create_lot"
    const val MY_LOTS = "my_lots"
    const val ORDERS = "orders"
    const val CHAT = "chat/{orderId}"
    const val PROFILE = "profile"
    const val USER_PROFILE = "user/{userId}"

    fun lots(gameId: Int) = "lots/$gameId"
    fun lotDetail(lotId: String) = "lot/$lotId"
    fun chat(orderId: String) = "chat/$orderId"
    fun userProfile(userId: String) = "user/$userId"
}
