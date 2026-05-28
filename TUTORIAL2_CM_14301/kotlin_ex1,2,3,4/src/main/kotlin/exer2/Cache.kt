package org.example.exer2

import kotlin.collections.iterator

class Cache<K: Any, V: Any>{
    private val store = mutableMapOf<K, V>()

    fun put(key: K, value: V){
        if(store[key] != value){
            store[key] = value
        }
    }

    fun get(key: K): V?{
        return store[key]
    }

    fun evict(key:K): V?{
        if(store[key] != null){
            store.remove(key)
            return null
        }else{
            println("No key has been evicted.")
            return null
        }
    }

    fun size(): Int{
        return store.size
    }

    fun getOrPut(key: K, default: () -> V): V{
        val existing = store[key]
        if (existing != null){
            return existing
        }
        val newValue = default()
        store[key] = newValue
        return newValue
    }

    fun transform(key: K, action: (V) -> V ): Pair<K,V>?{
        val existing = store[key]
        if(existing == null){
            return null
        }
        val newValue = action(existing)
        store[key] = newValue
        return Pair(key,newValue)
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