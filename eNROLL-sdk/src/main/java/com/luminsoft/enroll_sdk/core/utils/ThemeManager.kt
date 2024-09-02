package com.luminsoft.enroll_sdk.core.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources.Theme

class ThemeContextWrapper(base: Context?) : ContextWrapper(base) {
    override fun getTheme(): Theme {
        return baseContext.theme
    }
}