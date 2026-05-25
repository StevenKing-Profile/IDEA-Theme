package com.github.stevenkingprofile.ideatheme.toolWindow

import com.intellij.ide.ui.LafManager
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import javax.swing.JButton
import javax.swing.SwingUtilities

class MyToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val content = ContentFactory.getInstance().createContent(ReloadThemePanel(), null, false)
        toolWindow.contentManager.addContent(content)
    }
}

class ReloadThemePanel : JBPanel<ReloadThemePanel>(BorderLayout()) {
    init {
        val reloadButton = JButton("Reload Theme from Disk")
        reloadButton.addActionListener {
            reloadThemeRobustly()
        }
        add(reloadButton, BorderLayout.CENTER)
    }

    private fun reloadThemeRobustly() {
        try {
            val lafManager = LafManager.getInstance()
            val editorColorsManager = EditorColorsManager.getInstance()

            val customTheme = lafManager.installedThemes.find { it.name == "Matcha" }
            if (customTheme == null) {
                println("Error: 'Matcha' theme not found among installed themes.")
                return
            }

            val fallbackTheme = lafManager.installedThemes.find { it.name == "IntelliJ Light" }
            if (fallbackTheme == null) {
                println("Error: Fallback theme 'IntelliJ Light' not found.")
                return
            }

            SwingUtilities.invokeLater {
                if (lafManager.currentUIThemeLookAndFeel != fallbackTheme) {
                    lafManager.setCurrentLookAndFeel(fallbackTheme, true)
                }

                editorColorsManager.reloadKeepingActiveScheme()

                lafManager.setCurrentLookAndFeel(customTheme, true)

                println("Theme reloaded successfully!")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            println("Error reloading theme: ${e.message}")
        }
    }
}
