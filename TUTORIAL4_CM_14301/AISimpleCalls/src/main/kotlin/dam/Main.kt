package dam

import kotlinx.coroutines.runBlocking

/**
 * Main entry point for the LLM Assistant application
 */
fun main() = runBlocking {
    println("\n🤖 Starting LLM Assistant application... 😀😀😀😀😀\n")

    // Get configuration properties
    val properties = getProperties()

    // Set up logging
    configureLogging(properties)
    println()

    // Write LLM used
    println("✨ Using AI_LLM: ${properties.getProperty("AI_LLM")}")

    // Use the factory to create the appropriate assistant based on configuration
    val assistant: AIAssistant = AIAssistantFactory.createAssistant(properties)
    println()

    // Write system and model
    println("✨ Using: ${assistant.getSystem()} ${assistant.model}")

    // Write mode (CHAT or SENTIMENT)
    println("✨ Using MODE: ${assistant.mode}\n")

    // Display a welcome message, adapted to the current mode
    if (assistant.mode == "SENTIMENT") {
        println("💬 Type text and press Enter to get its sentiment rating (1-7) and justification as JSON.")
    } else {
        println("💬 Type your questions and press Enter to chat with the AI.")
    }
    println("💬 Press Ctrl+D (Unix/Mac) or Ctrl+Z (Windows) to exit.\n")

    // Main interaction loop
    while (true) {
        println("➖➖➖➖➖➖➖➖➖➖")
        // Ask for question input and read it from the console
        print("🧠 Your question: ")
        val input = readlnOrNull() ?: break

        // If blank input, write a help message and continue to ask for input
        if (input.isBlank()) {
            println("⚠️ Please enter a question or press Ctrl+D to exit.")
            continue
        }

        // Process input
        val output = assistant.processInput(input)
        println("\n🤖 Answer: $output\n\n")
    }

    // Bye message
    println("\n👋 Thank you for using LLM Assistant. Goodbye!")

}
