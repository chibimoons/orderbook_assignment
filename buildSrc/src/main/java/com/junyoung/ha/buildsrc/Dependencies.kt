package com.junyoung.ha.buildsrc

object Libraries {
    object Kotlin {
        private const val version = "1.9.0"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
        const val coroutinesVersion = "1.7.3"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
    }

    object AndroidX {

        object Compose {
            const val bom = "androidx.compose:compose-bom:2023.03.00"
            const val runtime = "androidx.compose.runtime:runtime"
            const val material3 = "androidx.compose.material3:material3"
            const val foundation = "androidx.compose.foundation:foundation"
            const val layout = "androidx.compose.foundation:foundation-layout"
            const val tooling = "androidx.compose.ui:ui-tooling"
        }

        object Navigation {
            private const val version = "2.6.0"
            const val compose = "androidx.navigation:navigation-compose:$version"
            const val viewModel = "androidx.hilt:hilt-navigation-compose:1.0.0"
        }
    }

    object Hilt {
        const val version = "2.44"
        const val gradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
        const val android = "com.google.dagger:hilt-android:$version"
        const val compiler = "com.google.dagger:hilt-android-compiler:$version"
    }

    object Test {
        private const val junitVersion = "4.13.2"
        const val junit = "junit:junit:$junitVersion"

        private const val mockitoVersion = "5.4.0"
        const val mockito = "org.mockito:mockito-core:$mockitoVersion"
        const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin:5.0.0"
        const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Kotlin.coroutinesVersion}"

        private const val androidXVersion = "1.5.0"
        const val androidCore = "androidx.test:core:$androidXVersion"
    }
}