import com.junyoung.ha.buildsrc.Libraries

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply {
    from("$rootDir/buildCommonDependencies/android-common-dependencies.kts")
    from("$rootDir/buildCommonDependencies/test-dependencies.kts")
    from("$rootDir/buildCommonDependencies/test-android-dependencies.kts")
}

android {
    namespace = "com.junyoung.ha.presentation"
}

dependencies {
    implementation(project(":common:bloc"))
    implementation(Libraries.AndroidX.LifeCycle.viewModelKtx)
}