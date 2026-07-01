package com.example.geminicakeapp

import android.graphics.Bitmap
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream

object GeminiClient {

    private const val MODEL = "gemini-2.5-flash"
    private val client = OkHttpClient()

    suspend fun analyzeImage(bitmap: Bitmap, prompt: String): String = withContext(Dispatchers.IO) {
        val base64Image = bitmapToBase64(bitmap)

        val parts = JSONArray()
            .put(JSONObject().put("text", prompt))
            .put(
                JSONObject().put(
                    "inline_data",
                    JSONObject()
                        .put("mime_type", "image/jpeg")
                        .put("data", base64Image)
                )
            )

        val contents = JSONArray()
            .put(JSONObject().put("role", "user").put("parts", parts))

        val requestBodyJson = JSONObject().put("contents", contents).toString()

        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1/models/$MODEL:generateContent?key=${BuildConfig.GEMINI_API_KEY}")
            .addHeader("Content-Type", "application/json")
            .post(requestBodyJson.toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            val responseBody = response.body?.string() ?: return@withContext "Error: empty response"

            if (!response.isSuccessful) {
                return@withContext "Error ${response.code}: $responseBody"
            }

            try {
                val json = JSONObject(responseBody)
                val candidates = json.optJSONArray("candidates")
                if (candidates == null || candidates.length() == 0) {
                    return@withContext "Error: No candidates found in the API response"
                }
                val content = candidates.getJSONObject(0).getJSONObject("content")
                val text = content.getJSONArray("parts").getJSONObject(0).getString("text")
                text.trim()
            } catch (e: Exception) {
                "Error parsing response: ${e.message}"
            }
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }
}
