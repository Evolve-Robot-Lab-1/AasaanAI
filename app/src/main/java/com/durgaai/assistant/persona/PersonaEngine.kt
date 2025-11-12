package com.durgaai.assistant.persona

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.File

/**
 * Persona engine for managing user personality and communication style.
 * Phase 6-7 implementation.
 */
object PersonaEngine {

    private const val PERSONA_FILE = "persona.json"
    private val gson = Gson()

    /**
     * Load user persona from storage.
     */
    fun loadPersona(context: Context): String {
        val file = File(context.filesDir, PERSONA_FILE)
        return if (file.exists()) {
            file.readText()
        } else {
            getDefaultPersona()
        }
    }

    /**
     * Save updated persona to storage.
     */
    fun savePersona(context: Context, personaData: PersonaData) {
        val file = File(context.filesDir, PERSONA_FILE)
        file.writeText(gson.toJson(personaData))
    }

    /**
     * Update persona based on user's reply selection.
     * Adaptive learning from Phase 11-12.
     */
    fun updateFromFeedback(
        context: Context,
        selectedReply: String,
        selectedTone: String
    ) {
        val currentPersona = loadPersonaData(context)

        // Increment tone preference
        currentPersona.tonePreferences[selectedTone] =
            (currentPersona.tonePreferences[selectedTone] ?: 0) + 1

        // Update common phrases
        if (!currentPersona.commonPhrases.contains(selectedReply)) {
            currentPersona.commonPhrases.add(selectedReply)
        }

        savePersona(context, currentPersona)
    }

    /**
     * Load persona as structured data.
     */
    fun loadPersonaData(context: Context): PersonaData {
        val json = loadPersona(context)
        return try {
            gson.fromJson(json, PersonaData::class.java)
        } catch (e: Exception) {
            PersonaData.default()
        }
    }

    /**
     * Get default persona template.
     */
    private fun getDefaultPersona(): String {
        return gson.toJson(PersonaData.default())
    }
}

data class PersonaData(
    @SerializedName("communication_style")
    val communicationStyle: String = "friendly",

    @SerializedName("formality_level")
    val formalityLevel: String = "casual",

    @SerializedName("emoji_usage")
    val emojiUsage: String = "moderate",

    @SerializedName("tone_preferences")
    val tonePreferences: MutableMap<String, Int> = mutableMapOf(),

    @SerializedName("common_phrases")
    val commonPhrases: MutableList<String> = mutableListOf(),

    @SerializedName("interests")
    val interests: List<String> = emptyList(),

    @SerializedName("response_length_preference")
    val responseLengthPreference: String = "medium"
) {
    companion object {
        fun default() = PersonaData(
            communicationStyle = "friendly",
            formalityLevel = "casual",
            emojiUsage = "moderate",
            tonePreferences = mutableMapOf(
                "short" to 0,
                "polite" to 0,
                "fun" to 0,
                "formal" to 0
            ),
            commonPhrases = mutableListOf(
                "Thanks!",
                "Sounds good!",
                "Got it!",
                "Will do!"
            ),
            interests = listOf("technology", "productivity", "AI"),
            responseLengthPreference = "medium"
        )
    }
}
