import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
	id("net.kyori.blossom") version "1.3.0"
    id("java-library")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "de.MarkusTieger"
version = "0.0.0"

blossom {
    replaceTokenIn("src/main/java/de/MarkusTieger/Installer.java")
    replaceToken("%mc_version%", project.property("net.minecraft.version"))
    replaceToken("%forge_version%", project.property("net.minecraftforge.version"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // implementation "uk.co.caprica:vlcj:4.7.2"
    implementation("com.google.code.gson:gson:2.9.0")
    
    implementation ("com.formdev:flatlaf:2.4")
    implementation ("com.formdev:flatlaf-intellij-themes:2.4")
}

tasks.jar {
	archiveBaseName.set("Installer")
	archiveVersion.set("")
   	manifest {
    	attributes["Main-Class"] = "de.MarkusTieger.Installer"
   	}
}

tasks.shadowJar {
   archiveBaseName.set("Installer")
   archiveClassifier.set("all")
   archiveVersion.set("")
}