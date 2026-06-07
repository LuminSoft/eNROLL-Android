package com.luminsoft.enroll_sdk.core.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DynamicLocalizationManagerTest {

    @Test
    fun parseFlatJsonKeepsOnlyStringValues() {
        val result = DynamicLocalizationParser.parse(
            """
            {
              "continue_to_next": "Next",
              "ignored_number": 3
            }
            """.trimIndent(),
            "en"
        )

        assertEquals("Next", result["continue_to_next"])
        assertFalse(result.containsKey("ignored_number"))
    }

    @Test
    fun parseLanguageGroupedJsonUsesRequestedLanguage() {
        val result = DynamicLocalizationParser.parse(
            """
            {
              "en": { "skip": "Skip now" },
              "ar": { "skip": "تخطي الآن" }
            }
            """.trimIndent(),
            "ar"
        )

        assertEquals("تخطي الآن", result["skip"])
    }

    @Test
    fun parseWrappedJsonUsesRequestedLanguage() {
        val result = DynamicLocalizationParser.parse(
            """
            {
              "localizationOverrides": {
                "en": { "done": "Finish" },
                "ar": { "done": "إنهاء" }
              }
            }
            """.trimIndent(),
            "en"
        )

        assertEquals("Finish", result["done"])
    }

    @Test
    fun protectedKeysAreRejected() {
        assertFalse(DynamicLocalizationAllowlist.isProtectedKey("continue_to_next"))
        assertTrue(DynamicLocalizationAllowlist.isProtectedKey("dot_face_auto_capture_instruction_pitch_too_high"))
        assertTrue(DynamicLocalizationAllowlist.isProtectedKey("nfc_reading_failed"))
        assertTrue(DynamicLocalizationAllowlist.isProtectedKey("epassportPreScanTitle"))
        assertTrue(DynamicLocalizationAllowlist.isProtectedKey("sample_nfc_reading_title"))
    }
}
