package org.example

sealed class Event {
    data class Login(val username: String, val timestamp: Long) : Event()
    data class Purchase(val username: String, val amount: Double, val timestamp: Long) : Event()
    data class Logout(val username: String, val timestamp: Long) : Event()
}

fun List<Event>.filterByUser(username: String): List<Event> {
    val result = mutableListOf<Event>()

    for (event in this) {
        when (event) {
            is Event.Login    -> if (event.username == username) result.add(event)
            is Event.Purchase -> if (event.username == username) result.add(event)
            is Event.Logout   -> if (event.username == username) result.add(event)
        }
    }

    return result
}

fun List<Event>.totalSpent(username: String): Double {
    return this
        .filterIsInstance<Event.Purchase>()
        .filter { it.username == username }
        .sumOf { it.amount }
}

fun processEvents(events: List<Event>, handler: (Event) -> Unit) {
    for (event in events) {
        handler(event)
    }
}

fun main() {
    val events = listOf(
        Event.Login("alice", 1_000),
        Event.Purchase("alice", 49.99, 1_100),
        Event.Purchase("bob", 19.99, 1_200),
        Event.Login("bob", 1_050),
        Event.Purchase("alice", 15.00, 1_300),
        Event.Logout("alice", 1_400),
        Event.Logout("bob", 1_500)
    )

    processEvents(events) { event ->
        when (event) {
            is Event.Login    -> println("[LOGIN] ${event.username} logged in at t=${event.timestamp}")
            is Event.Purchase -> println("[PURCHASE] ${event.username} spent $${event.amount} at t=${event.timestamp}")
            is Event.Logout   -> println("[LOGOUT] ${event.username} logged out at t=${event.timestamp}")
        }
    }

    println("Total spent by alice: $${events.totalSpent("alice")}")
    println("Total spent by bob: $${events.totalSpent("bob")}")

    println("Events for alice:")
    for (event in events.filterByUser("alice")) {
        println(event)
    }
}