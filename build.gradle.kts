import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.ifce.edu"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting

        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)

                //Decompose
                implementation("com.arkivanov.decompose:decompose:1.0.0-beta-01")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:1.0.0-beta-01")

                implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
                implementation(fileTree(mapOf("dir" to "lib-dl", "include" to listOf("*.jar"))))
                implementation(fileTree(mapOf("dir" to "lib-ext", "include" to listOf("*.jar"))))


                //Gson
                implementation("com.google.code.gson:gson:2.10")

            }
        }

    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "FinalProject"
            packageVersion = "1.0.0"
        }
    }
}