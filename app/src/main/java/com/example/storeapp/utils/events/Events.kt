package com.example.storeapp.utils.events

sealed class Event() {
    object IsUpdateProfile : Event()

    class DetailProductId(val value: Int) : Event()
    object IsUpdateCart : Event()
}