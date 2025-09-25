plugins {
    alias(libs.plugins.android.application)
    //id("java-library")
}

android {
    namespace = "com.iricostruttori.meditazione"
    // Determina quale cartella viene usata sotto
    // C:\Users\ADMIN\AppData\Local\Android\Sdk\platforms
    compileSdk = 34

    defaultConfig {
        applicationId = "com.iricostruttori.meditazione"
        minSdk = 24
        // Determina quale cartella viene usata sotto
        // C:\Users\ADMIN\AppData\Local\Android\Sdk\platforms
        targetSdk = 34
        versionCode = 1
        versionName = "1_0_1_28082025"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    //applicationVariants.all {
    //    if (buildType.name == "release") {
    //        outputs.all {
    //            val newName = "app-iricostruttori.apk"
    //            val outputFileName = newName
    //            val destinationDir = File("${project.rootDir}/apks")
    //            val outputFile = File(destinationDir, outputFileName)
    //        }
    //    }
    //}

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
    // --- Inizio della soluzione per il nome dell'APK in Kotlin DSL ---
    applicationVariants.all {
        val variant = this // 'this' si riferisce all'ApplicationVariant
        variant.outputs.all {
            // Verifica che l'output sia di tipo `ApkVariantOutput`
            // Questo è il modo corretto per impostare il nome del file APK nelle versioni recenti di AGP
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                outputFileName = when (variant.buildType.name) {
                    "release" -> "app_iricostruttori_release_v${variant.versionName}.apk"
                    "debug" -> "app_iricostruttori_debug_v${variant.versionName}.apk"
                    else -> "app_iricostruttori_${variant.buildType.name}_v${variant.versionName}.apk"
                }
            }
        }
    }
    // --- Fine della soluzione per il nome dell'APK in Kotlin DSL ---
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.recyclerview)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.legacy.support.v4)
    /* implementation(fileTree(mapOf(
        "dir" to "C:\\Users\\ADMIN\\AppData\\Local\\Android\\Sdk\\platforms\\android-34",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf()
        "include" to listOf("*.aar", "*.jar")
    )))
    */
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}


// *** QUI IL BLOCCO JAVADOC CORRETTO PER KOTLIN DSL ***
/*
tasks.register<Javadoc>("javadoc") {
    source = android.sourceSets.getByName("main").java.srcDirs
    setFrom(android.sourceSets.getByName("main").java.srcDirs)

    // Aggiungi tutto il classpath di runtime come source per javadoc
    classpath += configurations.getByName("runtimeClasspath")
    classpath += files(android.bootClasspath)
    classpath += files(variant.javaCompile.classpath.files)
    classpath += files(ext.androidJar)


    // Configurazione delle opzioni per javadoc
    options.apply {
        //addModules = listOf("ALL-UNNAMED")
        //addModules.add("ALL-UNNAMED")
        addModules.addAll(listOf("ALL-UNNAMED"))
    }
}
*/

// Blocco Javadoc corretto per Kotlin DSL
/*
tasks.register("javadoc", Javadoc::class.java) {
    // Usa setFrom per assegnare la sorgente
    setFrom(android.sourceSets.getByName("main").java.srcDirs)

    // Aggiungi il classpath di runtime come sorgente per Javadoc
    classpath += configurations.getByName("runtimeClasspath")
    classpath += files(android.bootClasspath)

    options.apply {
        // Aggiungi il flag --add-modules per gestire il problema
        // "requires transitive kotlin.stdlib"
        addModules.add("ALL-UNNAMED")
    }
}
*/
// *** FINE BLOCCO JAVADOC CORRETTO ***
