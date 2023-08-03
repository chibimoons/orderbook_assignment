plugins {
    id("kotlin")
}

apply {
    from("$rootDir/buildCommonDependencies/pure-kotlin-dependencies.kts")
    from("$rootDir/buildCommonDependencies/test-dependencies.kts")
}

dependencies {
    implementation(project(":common:bloc"))
    implementation(project(":features:recenttrades:domain"))
}