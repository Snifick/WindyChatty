package com.flowtrust.windychatty.ui

sealed class NavigationRoutes(val route: String) {

    data object AuthGraph : NavigationRoutes(route = "auth_graph")
    data object AuthPhone : NavigationRoutes(route = "auth_phone")
    data object AuthCode : NavigationRoutes(route = "auth_code/{phone}/{code}") {
        fun createRoute(phone: String, code: String): String {
            return "auth_code/$phone/$code"
        }
    }
    data object Register : NavigationRoutes(route = "register/{phone}/{code}"){
        fun createRoute(phone: String, code: String): String {
            return "register/$phone/$code"
        }
    }

    data object MainGraph : NavigationRoutes(route = "main_graph")

    data object MainChats : NavigationRoutes(route = "main_chats")
    data object MainDialog : NavigationRoutes(route = "main_dialog")

    data object MainProfile : NavigationRoutes(route = "main_profile")
}