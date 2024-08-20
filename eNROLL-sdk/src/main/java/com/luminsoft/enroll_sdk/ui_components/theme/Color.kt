package com.luminsoft.enroll_sdk.ui_components.theme

import androidx.compose.ui.graphics.Color


data class AppColors(
    val white: Color = Color(0xffffffff),
    val appBlack : Color= Color(0xff333333),
    val backGround: Color = Color(0xFFFFFFFF),
    val primary : Color= Color(0xFF1D56B8),
    val secondary : Color= Color(0xff5791DB),
    val successColor: Color = Color(0xff61CC3D),
    val warningColor: Color = Color(0xFFF9D548),
    val errorColor : Color= Color(0xFFDB305B),
    val textColor : Color= Color(0xff004194),
)

class ConstantColors private constructor() {
    companion object {
        val onSecondaryContainer = Color(0xffDFE5F2)
        val inversePrimary = Color(0xffF6F9FF)
        val onBackground = Color(0xffFCFDFF)
        val darkPurple = Color(0xFF1C1526)
    }
}


