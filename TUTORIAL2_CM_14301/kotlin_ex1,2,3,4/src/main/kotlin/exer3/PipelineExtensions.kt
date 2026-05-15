package org.example.exer3

fun buildPipeline(init: Pipeline.() -> Unit): Pipeline {
    val pipeline = Pipeline()
    pipeline.init()
    return pipeline
}