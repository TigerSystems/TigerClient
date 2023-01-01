plugins {
	id("java")
	id("application")
	// id("maven-publish")
}

group = "de.MarkusTieger.tigerclient.compiler"
base.archivesBaseName = "compiler"
version = "0.0.0"

val mc_version: String = project.property("net.minecraft.version").toString();
val forge_version: String = project.property("net.minecraftforge.version").toString();
val mc_mappings: String = project.property("net.minecraft.mappings").toString();

val first_loader: String = "--::forge_loader=";
val last_loader: String = "=forge_loader::--";
		
val first_client: String = "--::mc_version=";
val last_client: String = "=mc_version::--";

System.out.println(first_loader + forge_version + last_loader);
System.out.println(first_client + mc_version + last_client);

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}
dependencies {
	implementation("com.google.code.gson:gson:2.8.9")
	
	compileOnly("org.projectlombok:lombok:1.18.24")
	annotationProcessor("org.projectlombok:lombok:1.18.24")
	
	testCompileOnly("org.projectlombok:lombok:1.18.24")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
	
	implementation(files("proguard.jar"))
	
}

application {
    mainClass.set("de.MarkusTieger.Compiler")
}

/*publishing {
    publications {
        maven(MavenPublication) {
            groupId = project.group
            artifactId = project.archivesBaseName
            version = project.version

            from components.java
        }
    }
}*/