package com.dam

import annotations.Extract

abstract class DataProcessor(val input: String) {
    @Extract(regex = "Name:(.+)")
    abstract fun getName(): String?

    @Extract(regex = "Address:(.+)")
    abstract fun getAddress(): String?
}