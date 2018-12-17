package com.airline.android

import com.airline.android.model.Flight
import com.airline.android.model.Route
import java.security.MessageDigest

val emailValidator = Regex("[a-zA-Z0-9]+@[a-z0-9]+\\.[a-z-0-9]+")
val passwordValidator = Regex("[a-zA-Z0-9]{6,}")

typealias RouteItemClick = (route: Route) -> Unit
typealias FlightItemClick = (flight: Flight) -> Unit

data class LoginRequest(
    val email: String,
    val password: String
)

data class AddPassengerRequest(
    val firstName: String,
    val lastName: String,
    val address: String,
    val phoneNumber: String
)

data class BuyTicketRequest(
    val seat: String
)

fun String.hash(): String {
    val bytes = this.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}
