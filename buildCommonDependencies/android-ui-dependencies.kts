import com.junyoung.ha.buildsrc.Libraries

android {
    defaultConfig {
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    def composeBom = platform(Libraries.AndroidX.Compose.bom)
    implementation(platform(composeBom))
    androidTestImplementation(platform(composeBom))
    debugImplementation(Libraries.AndroidX.Compose.tooling)

    implementation(Libraries.AndroidX.Compose.runtime)
    implementation(Libraries.AndroidX.Compose.material3)
    implementation(Libraries.AndroidX.Compose.foundation)
    implementation(Libraries.AndroidX.Compose.layout)

    implementation(Libraries.AndroidX.Navigation.compose)
    implementation(Libraries.AndroidX.Navigation.viewModel)
}