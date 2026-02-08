import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    kotlin("plugin.serialization") version "2.0.0"
    id("app.cash.sqldelight") version "2.1.0"
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(compose.materialIconsExtended)
            implementation("media.kamel:kamel-image-default:1.0.7")
            implementation("io.github.aakira:napier:2.7.1")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")

            implementation("io.ktor:ktor-client-core:3.3.0")       // KMP core
            implementation("io.ktor:ktor-client-content-negotiation:3.3.0")
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.3.0")
            implementation("io.ktor:ktor-client-logging:3.3.0")
            // 需要加上这个，不然谷歌的接口请求会出错，可能是因为 Http2 的兼容性问题
            implementation("io.ktor:ktor-client-okhttp:3.3.0")

            // JetBrains Compose Multiplatform 的 ViewModel 支持
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.9.0")
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.9.0-beta03")

            val fileKit = "0.10.0-beta04"
            implementation("io.github.vinceglb:filekit-core:$fileKit")
            implementation("io.github.vinceglb:filekit-dialogs:$fileKit")
            implementation("io.github.vinceglb:filekit-dialogs-compose:$fileKit")
            implementation("io.github.vinceglb:filekit-coil:$fileKit")

            // 数据持久化
            implementation("com.russhwolf:multiplatform-settings:1.3.0")

            implementation("net.sf.kxml:kxml2:2.3.0")
            implementation("com.squareup.okio:okio:3.4.0") // 按需选择最新版
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation("io.ktor:ktor-client-cio:3.0.0")        // JVM / Desktop
            implementation("com.russhwolf:multiplatform-settings-jvm:1.3.0")
            implementation("app.cash.sqldelight:sqlite-driver:2.1.0")
            implementation("com.google.code.gson:gson:2.11.0")
            implementation("ch.qos.logback:logback-classic:1.5.18")
            implementation("com.squareup.retrofit2:retrofit:2.9.0")
            implementation("com.squareup.retrofit2:converter-gson:2.9.0")
            implementation("com.squareup.okhttp3:logging-interceptor:4.7.0")
            implementation("com.tencentcloudapi:tencentcloud-sdk-java-tmt:3.1.1338")
        }

        val jvmMain by getting {
            kotlin.srcDirs("src/jvmMain/kotlin", "src/jvmMain/java")
        }
    }
}


compose.desktop {
    application {
        mainClass = "me.shouheng.i18n.MainKt"

        val devVersion = "1.0.0"
        val version = if (project.hasProperty("build_version"))
            project.findProperty("build_version")!!.toString() else devVersion
        println("building app, version: $version")
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Easy I18n"
            packageVersion = version
            description = "An convenient development tool to manage i18n resources."
            vendor = "wyuhuan"
            copyright = "© 2025 wyuhuan"
            // 添加 java sql 类库
            modules("java.sql")
            macOS {
                iconFile.set(project.file("icons/app.icns"))
            }
            windows {
                shortcut = true
                iconFile.set(project.file("icons/app.ico"))
            }
            linux {
                iconFile.set(project.file("icons/app.png"))
            }
        }
        buildTypes.release.proguard {
            isEnabled.set(true) // 是否开启混淆
            obfuscate.set(true)
            configurationFiles.from(project.file("compose-desktop.pro"))
        }
        buildTypes.release {
            jvmArgs += listOf(
                "-Denv=release",
                "-Dbuild=${SimpleDateFormat("yyMMddHHmmss").format(Date())}",
                "-Dbuild_version=${version}"
            )
        }
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("me.shouheng.i18n")
        }
    }
}
