import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

val copyJsResources = tasks.create("copyJsResourcesWorkaround", Copy::class.java) {
    from(project(":shared").file("src/commonMain/resources"))
    into("build/processedResources/js/main")
}

val copyWasmResources = tasks.create("copyWasmResourcesWorkaround", Copy::class.java) {
    from(project(":shared").file("src/commonMain/resources"))
    into("build/processedResources/wasm/main")
}

afterEvaluate {
    project.tasks.getByName("jsProcessResources").finalizedBy(copyJsResources)
    project.tasks.getByName("wasmProcessResources").finalizedBy(copyWasmResources)
}

kotlin {
    js(IR) {
        moduleName = "recipe-app"
        browser {
            commonWebpackConfig {
                outputFileName = "recipe-app.js"
            }
        }
        binaries.executable()
    }

    wasm {
        moduleName = "recipe-app"
        browser {
            commonWebpackConfig {
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).copy(
                    open = mapOf(
                        "app" to mapOf(
                            "name" to "google chrome canary",
                            "arguments" to listOf("--js-flags=--experimental-wasm-gc ")
                        )
                    ),
                    static = (devServer?.static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.rootDir.path)
                        add(project.rootDir.path + "/shared/")
                        add(project.rootDir.path + "/nonAndroidMain/")
                        add(project.rootDir.path + "/webApp/")
                    },
                )
            }
        }
        binaries.executable()
    }

    sourceSets {
        val jsWasmMain by creating {
            dependencies {
                implementation(project(":shared"))
//                implementation(compose.runtime)
//                implementation(compose.ui)
//                implementation(compose.foundation)
//                implementation(compose.material)
//                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
//                implementation(compose.components.resources)
            }
        }
        val jsMain by getting {
            dependsOn(jsWasmMain)
            dependencies {
            }
        }
        val wasmMain by getting {
            dependsOn(jsWasmMain)
        }
    }
}

compose.experimental {
    web.application {}
}

compose {
    val composeVersion = project.property("compose.wasm.version") as String
    kotlinCompilerPlugin.set(composeVersion)
    val kotlinVersion = project.property("kotlin.version") as String
    kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=$kotlinVersion")
}
