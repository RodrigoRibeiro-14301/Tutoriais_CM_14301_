package org.example.exer1

fun main() {
    val events = listOf(
        Login("alice", 1_000),
        Purchase("alice", 49.99, 1_100),
        Purchase("bob", 19.99, 1_200),
        Login("bob", 1_050),
        Purchase("alice", 15.00, 1_300),
        Logout("alice", 1_400),
        Logout("bob", 1_500)
    )

    processEvents(events) { event ->
        when (event) {
            is Login    -> println("[LOGIN] ${event.username} logged in at t=${event.timestamp}")
            is Purchase -> println("[PURCHASE] ${event.username} spent $${event.amount} at t=${event.timestamp}")
            is Logout   -> println("[LOGOUT] ${event.username} logged out at t=${event.timestamp}")
        }
    }

    println("Total spent by alice: $${events.totalSpent("alice")}")
    println("Total spent by bob: $${events.totalSpent("bob")}")

    println("Events for alice:")
    for (event in events.filterByUser("alice")) {
        println(event)
    }
}