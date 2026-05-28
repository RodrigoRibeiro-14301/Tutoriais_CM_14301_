package org.example.exer2

fun main(){

    println("--- Word frequency Cache ---")
    val cache = Cache<String, Int>()
    cache.put("kotlin", 1)
    cache.put("skalla", 1)
    cache.put("haskell", 1)

    println("Size: ${cache.size()}")
    println("Frequency of \"kotlin\": ${cache.get("kotlin")}")

    println("getOrPut \"kotlin\": ${cache.getOrPut("kotlin") { 0 }}")
    println("getOrPut \"java\": ${cache.getOrPut("java") { 0 }}")
    println("Size after getOrPut: ${cache.size()}")
    val action : (Int) -> Int = {it + 1}
    println("Transform \"kotlin\" (+1): ${cache.transform("kotlin", action)}")
    println("Transform \"cobol\" (+1): ${cache.transform("cobol") { it + 1 }}")
    println("Snapshot: ${cache.snapshot()}\n")

    println("--- Id registry cache ---")
    val idCache = Cache<Int, String>()
    idCache.put(1, "Alice")
    idCache.put(2, "Bob")

    println("Id 1 -> ${idCache.get(1)}")
    println("Id 2 -> ${idCache.get(2)}")

    println("Id 1 after evict: ${idCache.evict(1)}")
    println("After evict id 1, size: ${idCache.size()}\n")


    println("--- Challenge ---")
    val challengeCache = cache.filterValues { it > 0 }
    println("Words with count > 0: $challengeCache")
    val challengeCache2 = cache.filterValues { it > 1 }
    println("Words with count > 1: $challengeCache2")
}