package org.example

class Pipeline{
    private val stages: MutableList<Pair<String, (List<String>) ->List<String>>> = mutableListOf()

    fun addStage(name: String, transform : (List<String>) -> List<String>){
        stages.add(Pair(name, transform))
    }

    fun execute(input: List<String>): List<String>{
        return stages.fold(input){ current, (_, transform) -> transform(current)}
    }

    fun describe(){
        stages.forEachIndexed { index, (name, _) ->
            println("Pipeline Stages : ${index + 1}: $name")
        }
    }
}

fun buildPipeline(init: Pipeline.() -> Unit): Pipeline {
    val pipeline = Pipeline()
    pipeline.init()
    return pipeline
}

fun main(){
    val logs = listOf(
        " INFO : server started",
        " ERROR : disk full",
        " DEBUG : checking config",
        " ERROR : out of memory",
        " INFO : request received",
        " ERROR : connection timeout"
    )

    val pipeline = buildPipeline {
        addStage("Trim"){ list ->
            list.map{it.trim()}
        }
        addStage("Filter errors"){ list ->
            list.filter{it.contains("ERROR")}
        }
        addStage("Uppercase"){ list ->
            list.map{it.uppercase() }
        }
        addStage("Add index"){ list ->
            list.mapIndexed { index, line -> "${index+1}. $line"  }
        }
    }

    pipeline.describe()
    println()
    println("Result: ")
    pipeline.execute(logs).forEach{println(it)}
}

















