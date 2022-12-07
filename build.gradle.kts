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

kotlin {
    dependencies {
        //MOM
        commonMainImplementation("org.apache.activemq:activemq-core:5.6.0")

        //Espaco Tuplas
        commonMainImplementation("org.apache.river:reggie:2.2.3")
        commonMainImplementation("net.jini:jini-core:2.1")
        commonMainImplementation("net.jini:jini-ext:2.1")
        commonMainImplementation("org.apache.river:outrigger:2.2.3")

    }
}
