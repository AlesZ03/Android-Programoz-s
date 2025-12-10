package com.example.myapplication.utils

/**
 * Wrapper egy eseményhez. Arra használjuk, hogy a LiveData-ban lévő adatot
 * csak egyszer lehessen felhasználni (pl. egy navigációhoz vagy egy Toast üzenethez).
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Kívülről csak olvasni lehet

    /**
     * Visszaadja a tartalmat, és megakadályozza az újbóli felhasználást.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Visszaadja a tartalmat, akkor is, ha már fel lett dolgozva.
     */
    fun peekContent(): T = content
}
