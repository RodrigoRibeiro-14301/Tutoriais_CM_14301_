package com.dam

fun main() {
    val input = "Name:Pedro Fazenda Address:Escola Nautica 2.15"
    val extractor = DataProcessorExtractor(input)
    println("Name: ${extractor.getName()}")
    println("Address: ${extractor.getAddress()}")
}