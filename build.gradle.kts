import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij.platform")
    id("org.jetbrains.changelog")
}

dependencies {
    implementation("com.google.code.gson:gson:2.14.0") // Added for JSON parsing
    testImplementation("junit:junit:4.13.2")

    // IntelliJ Platform Gradle Plugin Dependencies Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-dependencies-extension.html
    intellijPlatform {
        intellijIdea("2025.2.6.2")
        testFramework(TestFrameworkType.Platform)
    }
}

intellijPlatform {
    sandboxContainer.set(file("${layout.buildDirectory.get()}/idea-sandbox"))
    autoReload.set(true)
}

tasks {
    runIde {
        // Enforces internal developer look-and-feel menus
        systemProperty("idea.is.internal", "true")
        
        args("/home/s/Dev/PoC/AWS/Bedrock/PromptAttack")
    }
}