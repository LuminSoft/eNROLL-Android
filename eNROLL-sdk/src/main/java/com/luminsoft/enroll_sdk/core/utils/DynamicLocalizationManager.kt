package com.luminsoft.enroll_sdk.core.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.util.Log
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.luminsoft.enroll_sdk.core.models.LocalizationCode
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.ui_components.theme.EnrollLocalizationOverrides
import java.util.Locale

private const val TAG = "DynamicLocalization"

object DynamicLocalizationManager {
    private var overrides: Map<LocalizationCode, Map<String, String>> = emptyMap()

    fun configure(context: Context, localizationOverrides: EnrollLocalizationOverrides?) {
        if (localizationOverrides == null) {
            overrides = emptyMap()
            return
        }

        overrides = buildMap {
            localizationOverrides.englishFileName
                ?.let { loadFile(context, it, LocalizationCode.EN) }
                ?.takeIf { it.isNotEmpty() }
                ?.let { put(LocalizationCode.EN, it) }

            localizationOverrides.arabicFileName
                ?.let { loadFile(context, it, LocalizationCode.AR) }
                ?.takeIf { it.isNotEmpty() }
                ?.let { put(LocalizationCode.AR, it) }
        }
    }

    fun wrapContext(context: Context): Context {
        return DynamicLocalizationContextWrapper(context)
    }

    fun wrapResources(context: Context, resources: Resources): Resources {
        return DynamicLocalizationResources(context, resources)
    }

    fun getString(context: Context, id: Int): String? {
        return getString(context, context.resources, id)
    }

    internal fun getString(context: Context, resources: Resources, id: Int): String? {
        val key = runCatching { resources.getResourceEntryName(id) }.getOrNull() ?: return null
        if (!DynamicLocalizationAllowlist.isCustomizableKey(context, resources, key)) return null
        return overrides[EnrollSDK.localizationCode]?.get(key)
    }

    private fun loadFile(
        context: Context,
        fileName: String,
        language: LocalizationCode
    ): Map<String, String>? {
        val candidates = if (fileName.substringAfterLast('/', fileName).contains(".")) {
            listOf(fileName)
        } else {
            listOf(fileName, "$fileName.json")
        }

        candidates.forEach { candidate ->
            val json = runCatching {
                context.assets.open(candidate).bufferedReader().use { it.readText() }
            }.getOrNull()

            if (json != null) {
                return DynamicLocalizationParser.parse(json, language.key)
                    .filterKeys { DynamicLocalizationAllowlist.isProtectedKey(it).not() }
            }
        }

        Log.w(TAG, "Localization override file not found: $fileName")
        return null
    }
}

internal object DynamicLocalizationParser {
    fun parse(json: String, languageKey: String): Map<String, String> {
        val root = runCatching { JsonParser.parseString(json) }.getOrNull() ?: return emptyMap()
        val rootObject = root.asJsonObjectOrNull() ?: return emptyMap()

        rootObject["localizationOverrides"]
            ?.asJsonObjectOrNull()
            ?.get(languageKey)
            ?.asJsonObjectOrNull()
            ?.let { return stringMap(it) }

        rootObject[languageKey]
            ?.asJsonObjectOrNull()
            ?.let { return stringMap(it) }

        return stringMap(rootObject)
    }

    private fun stringMap(jsonObject: JsonObject): Map<String, String> {
        return jsonObject.entrySet().mapNotNull { (key, value) ->
            value.takeIf { it.isJsonPrimitive && it.asJsonPrimitive.isString }
                ?.asString
                ?.let { key to it }
        }.toMap()
    }

    private fun JsonElement.asJsonObjectOrNull(): JsonObject? {
        return takeIf { it.isJsonObject }?.asJsonObject
    }
}

internal object DynamicLocalizationAllowlist {
    private val protectedPrefixes = listOf(
        "dot_",
        "dot_document_",
        "dot_face_",
        "dot_nfc_",
        "nfc_",
        "sample_"
    )

    private val protectedExactKeys = setOf(
        "app_name",
        "navigation_destination_nfc_reading"
    )

    fun isCustomizableKey(context: Context, resources: Resources, key: String): Boolean {
        if (isProtectedKey(key)) return false
        return resources.getIdentifier(key, "string", context.packageName) != 0
    }

    fun isProtectedKey(key: String): Boolean {
        return key in protectedExactKeys ||
                key.startsWith("epassport", ignoreCase = true) ||
                protectedPrefixes.any { key.startsWith(it, ignoreCase = true) }
    }
}

private class DynamicLocalizationContextWrapper(base: Context) : ContextWrapper(base) {
    private val dynamicResources: Resources by lazy {
        DynamicLocalizationResources(this, baseContext.resources)
    }

    override fun getResources(): Resources = dynamicResources
}

private class DynamicLocalizationResources(
    private val context: Context,
    private val baseResources: Resources
) : Resources(
    baseResources.assets,
    baseResources.displayMetrics,
    baseResources.configuration
) {
    override fun getText(id: Int): CharSequence {
        return DynamicLocalizationManager.getString(context, baseResources, id)
            ?: baseResources.getText(id)
    }

    override fun getText(id: Int, def: CharSequence?): CharSequence {
        return DynamicLocalizationManager.getString(context, baseResources, id)
            ?: baseResources.getText(id, def)
    }

    override fun getString(id: Int): String {
        return DynamicLocalizationManager.getString(context, baseResources, id)
            ?: baseResources.getString(id)
    }

    override fun getString(id: Int, vararg formatArgs: Any?): String {
        val override = DynamicLocalizationManager.getString(context, baseResources, id)
        return if (override != null) {
            String.format(Locale.getDefault(), override, *formatArgs)
        } else {
            baseResources.getString(id, *formatArgs)
        }
    }
}

private val LocalizationCode.key: String
    get() = name.lowercase(Locale.US)
