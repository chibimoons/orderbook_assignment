import com.junyoung.ha.buildsrc.Libraries

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

apply {
    from("$rootDir/buildCommonDependencies/android-common-dependencies.kts")
    from("$rootDir/buildCommonDependencies/android-ui-dependencies.kts")
    from("$rootDir/buildCommonDependencies/test-dependencies.kts")
    from("$rootDir/buildCommonDependencies/test-android-dependencies.kts")
}

android {
    namespace = "com.junyoung.ha.assignment"

    defaultConfig {
        namespace = "com.junyoung.ha.websocketsample"
        versionCode = 1
        versionName = "1.0"
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
}

dependencies {
    implementation(Libraries.Network.okhttp)
}