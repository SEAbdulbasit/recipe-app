import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
}

val copyJsResources = tasks.create("copyJsResourcesWorkaround", Copy::class.java) {
    from(project(":shared").file("src/commonMain/composeResources"))
    into("build/processedResources/js/main")
}

val copyWasmResources = tasks.create("copyWasmResourcesWorkaround", Copy::class.java) {
    from(project(":shared").file("src/commonMain/composeResources"))
    into("build/processedResources/wasmJs/main")
}

afterEvaluate {
    project.tasks.getByName("jsProcessResources").finalizedBy(copyJsResources)
    project.tasks.getByName("wasmJsProcessResources").finalizedBy(copyWasmResources)
    project.tasks.getByName("wasmJsDevelopmentExecutableCompileSync").dependsOn(copyWasmResources)
    project.tasks.getByName("jsBrowserProductionExecutableDistributeResources").mustRunAfter(copyJsResources)
    project.tasks.getByName("jsDevelopmentExecutableCompileSync").mustRunAfter(copyJsResources)
    project.tasks.getByName("wasmJsDevelopmentExecutableCompileSync").mustRunAfter(copyWasmResources)
    project.tasks.getByName("jsProductionExecutableCompileSync").mustRunAfter(copyJsResources)
    project.tasks.getByName("wasmJsProductionExecutableCompileSync").mustRunAfter(copyWasmResources)
    //project.tasks.getByName("wasmDevelopmentExecutableCompileSync").mustRunAfter(copyWasmResources)
    project.tasks.getByName("wasmJsDevelopmentExecutableCompileSync").dependsOn(copyWasmResources)
}

kotlin {
    js(IR) {
        moduleName = "recipeapp"
        browser {
            commonWebpackConfig {
                outputFileName = "recipeapp.js"
            }
        }
        binaries.executable()
    }
    wasmJs {
        moduleName = "recipeapp"
        browser {
            commonWebpackConfig {
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {

                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.rootDir.path)
                        add(project.rootDir.path + "/shared/")
                        add(project.rootDir.path + "/nonAndroidMain/")
                        add(project.rootDir.path + "/webApp/")
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val jsWasmMain by creating {
            dependencies {
                implementation(project(":shared"))
                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }
        val jsMain by getting {
            dependsOn(jsWasmMain)
        }
        val wasmJsMain by getting {
            dependsOn(jsWasmMain)
        }
    }
}

compose.experimental {
    web.application {}
}
