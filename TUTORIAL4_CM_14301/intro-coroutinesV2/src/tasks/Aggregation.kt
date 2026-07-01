package tasks

import contributors.User

fun List<User>.aggregate(): List<User> =
    groupBy { it.login }
        .map { (login, users) -> User(login, users.sumOf { it.contributions }) }
        .sortedByDescending { it.contributions }