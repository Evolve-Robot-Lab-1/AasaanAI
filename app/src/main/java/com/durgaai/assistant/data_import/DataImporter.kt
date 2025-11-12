package com.durgaai.assistant.data_import

import android.content.Context
import com.durgaai.assistant.persona.PersonaData
import com.durgaai.assistant.persona.PersonaEngine
import com.google.gson.Gson
import java.io.File

/**
 * Data importer for persona and configuration.
 * Allows users to import/export their AI persona.
 */
object DataImporter {

    private val gson = Gson()

    /**
     * Import persona from JSON string.
     */
    fun importPersona(context: Context, jsonData: String): Boolean {
        return try {
            val personaData = gson.fromJson(jsonData, PersonaData::class.java)
            PersonaEngine.savePersona(context, personaData)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Import persona from file.
     */
    fun importPersonaFromFile(context: Context, filePath: String): Boolean {
        return try {
            val file = File(filePath)
            if (file.exists()) {
                val jsonData = file.readText()
                importPersona(context, jsonData)
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Export persona to JSON string.
     */
    fun exportPersona(context: Context): String {
        return PersonaEngine.loadPersona(context)
    }

    /**
     * Export persona to file.
     */
    fun exportPersonaToFile(context: Context, filePath: String): Boolean {
        return try {
            val personaJson = exportPersona(context)
            val file = File(filePath)
            file.writeText(personaJson)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Reset persona to default.
     */
    fun resetPersona(context: Context) {
        PersonaEngine.savePersona(context, PersonaData.default())
    }
}
