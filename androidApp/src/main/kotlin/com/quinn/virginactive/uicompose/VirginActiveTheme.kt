package com.quinn.virginactive.uicompose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun VirginActiveTheme(
    useSystemTheme: Boolean,
    darkTheme: Boolean = if (useSystemTheme) isSystemInDarkTheme() else true,
    content: @Composable () -> Unit
) {
    val colourScheme = if (darkTheme) VirginDarkColorScheme else VirginLightColorScheme
    val extendedColors = if (darkTheme) VirginExtendedDark else VirginExtendedLight

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colourScheme,
            content = content,
        )
    }
}