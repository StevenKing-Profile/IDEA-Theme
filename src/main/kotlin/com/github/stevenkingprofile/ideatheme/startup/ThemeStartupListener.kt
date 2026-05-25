package com.github.stevenkingprofile.ideatheme.startup

import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.colors.EditorColorsManager

class ThemeStartupListener : LafManagerListener {
    override fun lookAndFeelChanged(source: LafManager) {
        // Use currentUIThemeLookAndFeel to identify JSON-based UI themes in 2025.2
        val currentTheme = source.currentUIThemeLookAndFeel

        // 1. If Matcha is not the active theme, find it and force the switch.
        // This ensures the theme "loads" by default on startup.
        if (currentTheme?.name != "Matcha") {
            ApplicationManager.getApplication().invokeLater {
                // We must provide both parameters: 
                // p0 (matchaLaf): The LookAndFeelInfo
                // p1 (false): Disables lockEditorScheme so our custom XML can load
                source.installedThemes.find { it.name == "Matcha" }?.let { source.setCurrentLookAndFeel(it, false) }
                source.updateUI()
            }
            return
        }

        // 2. Once Matcha is active, ensure the "MatchaEditor" scheme is applied.
        if (currentTheme?.name == "Matcha") {
            val colorsManager = EditorColorsManager.getInstance()
            
            // getScheme("MatchaEditor") retrieves the scheme registered via <bundledColorScheme> 
            // in your plugin.xml by its internal name.
            val targetScheme = colorsManager.getScheme("MatchaEditor")

            if (targetScheme != null && colorsManager.globalScheme?.name != "MatchaEditor") {
                ApplicationManager.getApplication().invokeLater {
                    // We call setGlobalScheme() explicitly because the Kotlin compiler
                    // often incorrectly treats 'globalScheme' as a read-only 'val' in this SDK.
                    colorsManager.setGlobalScheme(targetScheme)
                }
            }
        }
    }
}
