package org.example.exer1

fun List<Event>.filterByUser(username: String): List<Event> {    // this pode ser trocado por fun filterByUser(list: List<Event>, username)
    val result = mutableListOf<Event>()

    for (event in this) {        //event in list
        when (event) {
            is Login    -> if (event.username == username) result.add(event)
            is Purchase -> if (event.username == username) result.add(event)
            is Logout   -> if (event.username == username) result.add(event)
        }
    }

    return result
}

fun List<Event>.totalSpent(username: String): Double {
    return this
        .filterIsInstance<Purchase>()
        .filter { it.username == username }   //it= purchase -> purchase.username == username
        .sumOf { it.amount }
}

fun processEvents(events: List<Event>, handler: (Event) -> Unit) {
    for (event in events) {
        handler(event)
    }
}