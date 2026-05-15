package org.example.exer3

class Pipeline {
    private val stages = mutableListOf<Pair<String, (List<String>) -> List<String>>>()

    fun addStage(name: String, transform: (List<String>) -> List<String>) {
        stages.add(Pair(name, transform))
    }

    fun execute(input: List<String>): List<String> {
        return stages.fold(input) { current, (_,transform) -> transform(current) }
    }

    fun describe() {
        println("Pipeline stages:")
        stages.forEachIndexed { index, (name,_) ->
            println("${index + 1}. $name")
        }
    }
}