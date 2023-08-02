plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply {
    from("$rootDir/buildCommonDependencies/pure-kotlin-dependencies.kts")
    from("$rootDir/buildCommonDependencies/android-common-dependencies.kts")
    from("$rootDir/buildCommonDependencies/test-dependencies.kts")
    from("$rootDir/buildCommonDependencies/test-android-dependencies.kts")
}

android {
    namespace = "com.junyoung.ha.features.orderbook.datasource"
}

dependencies {
    implementation(project(":features:common:domain"))
    implementation(project(":features:recenttrades:domain"))
    implementation(project(":features:recenttrades:repository"))
}