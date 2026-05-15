package org.example.exer3

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
        this.addStage("Trim"){ list ->
            list.map{it.trim()}
        }
        addStage("Filter errors"){ list ->
            list.filter{it.contains("ERROR")}
        }
        this.addStage("Uppercase"){ list ->
            list.map{it.uppercase() }
        }
        addStage("Add index"){ list ->
            list.mapIndexed { index, line -> "${index+1}. $line"  }
        }
    }

    pipeline.describe()
    println("Result: ")
    pipeline.execute(logs).forEach{println(it)}
}

















