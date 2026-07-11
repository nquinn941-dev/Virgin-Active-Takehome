package com.quinn.virginactive.uicompose

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

object VirginColors {
    // --- Primary: Virgin red ("Lust") ---
    val Red = Color(0xFFE4181E)
    val RedContainerDark = Color(0xFF4A0E10)   // muted red surface, dark theme
    val RedContainerLight = Color(0xFFFFDAD8)  // tinted red surface, light theme
    val OnRedContainerDark = Color(0xFFFFDAD8)
    val OnRedContainerLight = Color(0xFF410004)

    // --- Secondary: warm neutral, sits quietly behind the red accent ---
    val Sand = Color(0xFFB3A69E)
    val SandContainerDark = Color(0xFF3A322D)
    val SandContainerLight = Color(0xFFEEE0D9)
    val OnSandContainerDark = Color(0xFFE8DED7)
    val OnSandContainerLight = Color(0xFF261A14)

    // --- Tertiary: amber, used for badges / "closed" / secondary alerts ---
    val Amber = Color(0xFFFFA726)
    val AmberContainerDark = Color(0xFF4D3200)
    val AmberContainerLight = Color(0xFFFFDDB0)
    val OnAmberContainerDark = Color(0xFFFFDDB0)
    val OnAmberContainerLight = Color(0xFF2A1800)

    // --- Error: deliberately distinct from primary red so destructive
    // actions don't read as "the brand color" ---
    val ErrorRed = Color(0xFFCF2B1E)
    val ErrorContainerDark = Color(0xFF601410)
    val ErrorContainerLight = Color(0xFFFFDAD4)
    val OnErrorContainerDark = Color(0xFFFFDAD4)
    val OnErrorContainerLight = Color(0xFF410100)

    // --- Neutrals / surfaces ---
    val SurfaceDark = Color(0xFF0D0D0D)
    val SurfaceContainerDark = Color(0xFF1A1A1A)
    val SurfaceContainerHighDark = Color(0xFF2B2B2B)
    val OutlineDark = Color(0xFF3D3D3D)
    val OutlineVariantDark = Color(0xFF2B2B2B)

    val SurfaceLight = Color(0xFFFFFBF9)
    val SurfaceContainerLight = Color(0xFFF3EDEB)
    val SurfaceContainerHighLight = Color(0xFFE7E0DD)
    val OutlineLight = Color(0xFF857370)
    val OutlineVariantLight = Color(0xFFD8C2BE)

    val ScrimDark = Color(0xFF000000)
    val ScrimLight = Color(0xFF000000)
}

val VirginLightColorScheme = lightColorScheme(
    primary = VirginColors.Red,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = VirginColors.RedContainerLight,
    onPrimaryContainer = VirginColors.OnRedContainerLight,

    secondary = Color(0xFF6F5B52),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = VirginColors.SandContainerLight,
    onSecondaryContainer = VirginColors.OnSandContainerLight,

    tertiary = Color(0xFF8A5700),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = VirginColors.AmberContainerLight,
    onTertiaryContainer = VirginColors.OnAmberContainerLight,

    error = VirginColors.ErrorRed,
    onError = Color(0xFFFFFFFF),
    errorContainer = VirginColors.ErrorContainerLight,
    onErrorContainer = VirginColors.OnErrorContainerLight,

    background = VirginColors.SurfaceLight,
    onBackground = Color(0xFF1C1B1B),

    surface = VirginColors.SurfaceLight,
    onSurface = Color(0xFF1C1B1B),
    surfaceVariant = Color(0xFFF0E0DD),
    onSurfaceVariant = Color(0xFF524341),

    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFFDF6F4),
    surfaceContainer = VirginColors.SurfaceContainerLight,
    surfaceContainerHigh = VirginColors.SurfaceContainerHighLight,
    surfaceContainerHighest = Color(0xFFE1DBD8),

    outline = VirginColors.OutlineLight,
    outlineVariant = VirginColors.OutlineVariantLight,

    inverseSurface = Color(0xFF313030),
    inverseOnSurface = Color(0xFFF4EFED),
    inversePrimary = Color(0xFFFFB3AD),

    scrim = VirginColors.ScrimLight,
)

val VirginDarkColorScheme = darkColorScheme(
    primary = VirginColors.Red,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = VirginColors.RedContainerDark,
    onPrimaryContainer = VirginColors.OnRedContainerDark,

    secondary = VirginColors.Sand,
    onSecondary = Color(0xFF241A15),
    secondaryContainer = VirginColors.SandContainerDark,
    onSecondaryContainer = VirginColors.OnSandContainerDark,

    tertiary = VirginColors.Amber,
    onTertiary = Color(0xFF2A1800),
    tertiaryContainer = VirginColors.AmberContainerDark,
    onTertiaryContainer = VirginColors.OnAmberContainerDark,

    error = VirginColors.ErrorRed,
    onError = Color(0xFFFFFFFF),
    errorContainer = VirginColors.ErrorContainerDark,
    onErrorContainer = VirginColors.OnErrorContainerDark,

    background = Color(0xFF0D0D0D),
    onBackground = Color(0xFFFFFFFF),

    surface = Color(0xFF0D0D0D),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF2B2B2B),
    onSurfaceVariant = Color(0xFFB3B3B3),

    surfaceContainerLowest = Color(0xFF060606),
    surfaceContainerLow = VirginColors.SurfaceDark,
    surfaceContainer = VirginColors.SurfaceContainerDark,
    surfaceContainerHigh = VirginColors.SurfaceContainerHighDark,
    surfaceContainerHighest = Color(0xFF333333),

    outline = VirginColors.OutlineDark,
    outlineVariant = VirginColors.OutlineVariantDark,

    inverseSurface = Color(0xFFFFFFFF),
    inverseOnSurface = Color(0xFF0D0D0D),
    inversePrimary = VirginColors.RedContainerLight,

    scrim = VirginColors.ScrimDark,
)
data class ExtendedColors(
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
)

val VirginExtendedDark = ExtendedColors(
    success = Color(0xFF4CAF6D),
    onSuccess = Color(0xFF06301A),
    successContainer = Color(0xFF17301F),
    onSuccessContainer = Color(0xFFA6E9BB),
)

val VirginExtendedLight = ExtendedColors(
    success = Color(0xFF2E7D46),
    onSuccess = Color(0xFFFFFFFF),
    successContainer = Color(0xFFCFF6E4),
    onSuccessContainer = Color(0xFF0E4F2A),
)

val LocalExtendedColors = staticCompositionLocalOf {
    VirginExtendedDark
}
