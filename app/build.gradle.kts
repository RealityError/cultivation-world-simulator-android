import java.io.File

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.chaquo.python")
}

val appVersionName = "0.1.0"
val runtimeAssetStamp = providers.environmentVariable("CWS_RUNTIME_ASSET_VERSION").orNull
    ?: System.currentTimeMillis().toString()

android {
    namespace = "io.fourthfever.cwsandroid"
    compileSdk = 35

    defaultConfig {
        applicationId = "io.fourthfever.cwsandroid"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = appVersionName
        buildConfigField("String", "RUNTIME_ASSET_VERSION", "\"$appVersionName-$runtimeAssetStamp\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters += listOf("arm64-v8a", "x86_64")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

fun npmCommand(vararg args: String): List<String> {
    val prefix = if (System.getProperty("os.name").startsWith("Windows", ignoreCase = true)) {
        listOf("cmd", "/c", "npm")
    } else {
        listOf("npm")
    }
    return prefix + args
}

val sourceRepoRoot = rootProject.file(".")

val installWebDeps by tasks.registering(Exec::class) {
    onlyIf {
        !sourceRepoRoot.resolve("web/node_modules").exists()
    }
    workingDir = sourceRepoRoot.resolve("web")
    commandLine(npmCommand("install"))
}

val buildWebDist by tasks.registering(Exec::class) {
    dependsOn(installWebDeps)
    workingDir = sourceRepoRoot.resolve("web")
    commandLine(npmCommand("run", "build"))
}

val syncRuntimeBundle by tasks.registering(Sync::class) {
    dependsOn(buildWebDist)
    from(sourceRepoRoot.resolve("src")) {
        into("cws-runtime/src")
    }
    from(sourceRepoRoot.resolve("static")) {
        into("cws-runtime/static")
    }
    from(sourceRepoRoot.resolve("assets")) {
        into("cws-runtime/assets")
    }
    from(sourceRepoRoot.resolve("web/dist")) {
        into("cws-runtime/web/dist")
    }
    into(layout.buildDirectory.dir("generated/runtimeAssets"))
}

android.sourceSets.getByName("main").assets.srcDir(layout.buildDirectory.dir("generated/runtimeAssets"))

tasks.named("preBuild") {
    dependsOn(syncRuntimeBundle)
}

fun resolveBuildPython311(): String? {
    val candidates = listOfNotNull(
        System.getenv("CHAQUOPY_BUILD_PYTHON"),
        System.getenv("PYTHON311_HOME")?.let { "$it/python.exe" },
        System.getenv("LOCALAPPDATA")?.let { "$it/Programs/Python/Python311/python.exe" },
        "C:/Program Files/Python311/python.exe",
        "C:/Python311/python.exe",
    )

    return candidates
        .map { it.replace("\\", "/") }
        .firstOrNull { File(it).exists() }
}

chaquopy {
    defaultConfig {
        version = "3.11"
        resolveBuildPython311()?.let { buildPython(it) }
        pip {
            install("-r", rootProject.file("requirements-runtime.txt").absolutePath)
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.activity:activity-ktx:1.9.2")
    implementation("androidx.webkit:webkit:1.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    testImplementation("junit:junit:4.13.2")
}
