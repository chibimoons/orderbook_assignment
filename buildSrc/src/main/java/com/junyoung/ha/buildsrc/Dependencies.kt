package com.junyoung.ha.buildsrc

object Libraries {
    object Kotlin {
        private const val version = "1.9.0"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
        const val coroutinesVersion = "1.7.3"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
    }

    object AndroidX {
        object LifeCycle {
            private const val version = "2.6.1"
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        }
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