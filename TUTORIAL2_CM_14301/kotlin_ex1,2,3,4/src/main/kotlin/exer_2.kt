package org.example

class Cache<K: Any, V: Any>{
    private val store = mutableMapOf<K, V>()

    fun put(key: K, value: V){
        store[key] = value
    }

    fun get(key: K): V?{
        return store[key]
    }

    fun evict(key: K){
        store.remove(key)
    }

    fun size(): Int{
        return store.size
    }

    fun getOrPut(key: K, default: () -> V): V{
        val existing = store[key] ?: default()
        store[key] = existing
        return existing
    }

    fun transform(key: K, action: (V) -> V): Boolean{
        val existing = store[key]
        if(existing == null){
            return false
        }
        store[key] = action(existing)
        return true
    }

    fun snapshot(): Map<K, V>{
        return store.toMap()
    }

    fun filterValues(predicate : (V) -> Boolean): Map<K, V>{
        val result = mutableMapOf<K, V>()
        for((key, value) in store){
            if(predicate(value)){
                result[key] = value
            }
        }
        return result
    }
}

fun main(){

    println("--- Word frequency Cache ---")
    val cache = Cache<String, Int>()
    cache.put("kotlin", 1)
    cache.put("skalla", 1)
    cache.put("haskell", 1)

    println("Size: ${cache.size()}")
    println("Frequency of \"kotlin\": ${cache.get("kotlin")}")

    println("getOrPut \"kotlin\": ${cache.getOrPut("kotlin", {0})}")
    println("getOrPut \"java\": ${cache.getOrPut("java", {0})}")
    println("Size after getOrPut: ${cache.size()}")

    println("Transform \"kotlin\" (+1): ${cache.transform("kotlin", {it+1})}")
    println("Transform \"cobol\" (+1): ${cache.transform("cobol", {it+1})}")
    println("Snapshot: ${cache.snapshot()}\n")

    println("--- Id registry cache ---")
    val idCache = Cache<Int, String>()
    idCache.put(1, "Alice")
    idCache.put(2, "Bob")

    println("Id 1 -> ${idCache.get(1)}")
    println("Id 2 -> ${idCache.get(2)}")

    println("After evict id 1, size: ${idCache.size()}")
    println("Id 1 after evict: ${idCache.evict(1)}\n")

    println("--- Challenge ---")
    val challengeCache = cache.filterValues{it > 0}
    println("Words with count > 0: $challengeCache")
    val challengeCache2 = cache.filterValues{it > 1}
    println("Words with count > 1: $challengeCache2")
    


}