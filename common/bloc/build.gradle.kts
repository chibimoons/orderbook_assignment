import com.junyoung.ha.buildsrc.Libraries

plugins {
    id("kotlin")
}

apply {
    from("$rootDir/buildCommonDependencies/test-dependencies.kts")
}

dependencies {
    implementation(Libraries.Kotlin.stdlib)
    implementation(Libraries.Kotlin.coroutines)
}