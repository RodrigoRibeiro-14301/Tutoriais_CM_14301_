package com.example.geminicakeapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.geminicakeapp.ui.theme.GeminiCakeAppTheme
import kotlinx.coroutines.launch


data class Treat(val label: String, val drawableRes: Int)

private val treats = listOf(
    Treat("Cake", R.drawable.img_cake),
    Treat("Cookie", R.drawable.img_cookie),
    Treat("Cupcake", R.drawable.img_cupcake)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeminiCakeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GeminiCakeScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun GeminiCakeScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedIndex by remember { mutableIntStateOf(0) }
    var promptText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var responseText by remember { mutableStateOf("") }
    var isGuessingIngredient by remember { mutableStateOf(false) }
    var mysteryIngredientText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Pick a treat, ask Gemini something about it",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            treats.forEachIndexed { index, treat ->
                TreatThumbnail(
                    treat = treat,
                    selected = index == selectedIndex,
                    onClick = { selectedIndex = index }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = promptText,
            onValueChange = { promptText = it },
            label = { Text("Your prompt (e.g. recipe, name suggestion...)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val treat = treats[selectedIndex]
                isLoading = true
                responseText = ""
                coroutineScope.launch {
                    val drawable: Drawable = ContextCompat.getDrawable(context, treat.drawableRes)!!
                    val bitmap = drawable.toBitmap(width = 512, height = 512)
                    val fullPrompt = "The attached image shows a ${treat.label.lowercase()}. $promptText"
                    responseText = GeminiClient.analyzeImage(bitmap, fullPrompt)
                    isLoading = false
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) "Asking Gemini..." else "Ask Gemini")
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (responseText.isNotBlank()) {
            Text(
                text = "Answer:",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = responseText)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                val treat = treats[selectedIndex]
                isGuessingIngredient = true
                mysteryIngredientText = ""
                coroutineScope.launch {
                    val drawable: Drawable = ContextCompat.getDrawable(context, treat.drawableRes)!!
                    val bitmap = drawable.toBitmap(width = 512, height = 512)
                    val secretPrompt = "Playfully guess ONE surprising secret ingredient that might be in this " +
                        "${treat.label.lowercase()}, with a short fun reason why (1-2 sentences). " +
                        "Be creative and lighthearted, not literal."
                    mysteryIngredientText = GeminiClient.analyzeImage(bitmap, secretPrompt)
                    isGuessingIngredient = false
                }
            },
            enabled = !isGuessingIngredient,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isGuessingIngredient) "🥁 Scanning for secrets..." else "🕵️ Guess the Secret Ingredient")
        }

        AnimatedVisibility(visible = mysteryIngredientText.isNotBlank()) {
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "🔍 Mystery Ingredient", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = mysteryIngredientText)
                }
            }
        }
    }
}

@Composable
private fun TreatThumbnail(treat: Treat, selected: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = treat.drawableRes),
            contentDescription = treat.label,
            modifier = Modifier
                .size(88.dp)
                .border(
                    width = if (selected) 3.dp else 1.dp,
                    color = if (selected) MaterialTheme.colorScheme.primary else Color.LightGray,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { onClick() }
                .padding(8.dp)
        )
        Text(text = treat.label, style = MaterialTheme.typography.labelMedium)
    }
}
