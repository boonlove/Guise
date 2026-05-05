@file:Suppress("UnstableApiUsage")

val version = ModuleVersion()

plugins {
    id("kotlinx-serialization")
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
}

android {
    namespace = "com.houvven.twig"
    compileSdk = ASdk.compile

    signingConfigs {
        // 尝试从 keystore.properties 文件加载签名配置
        val keystorePropsFile = rootProject.file("keystore.properties")
        if (keystorePropsFile.exists()) {
            val si = SignatureInfo(keystorePropsFile)
            val storeFile = si.storeFile
            val storePassword = si.storePassword
            val keyAlias = si.keyAlias
            val keyPassword = si.keyPassword
            val hasCustomSigning = file(storeFile).exists() && storePassword.isNotEmpty() && keyAlias.isNotEmpty() && keyPassword.isNotEmpty()
            if (hasCustomSigning) {
                try {
                    register("customSigning") {
                        this.storeFile = storeFile
                        this.storePassword = storePassword
                        this.keyAlias = keyAlias
                        this.keyPassword = keyPassword

                        enableV1Signing = true
                        enableV2Signing = true
                        enableV3Signing = true
                    }
                    logger.lifecycle("✅ 已从 keystore.properties 加载签名配置")
                } catch (e: Exception) {
                    logger.lifecycle("⚠️ 签名配置加载失败。(${e.message})")
                }
            } else if (!file(storeFile).exists()) {
                logger.lifecycle("⚠️ 签名配置加载失败。密钥文件不存在：${storeFile}")
            } else {
                logger.lifecycle("⚠️ keystore.properties 文件中缺少必要的配置项，签名配置加载失败。")
            }
        } else {
            logger.lifecycle("ℹ️ 未找到 keystore.properties 文件。")
        }
    }

    defaultConfig {
        applicationId = namespace
        minSdk = ASdk.min
        targetSdk = ASdk.target
        versionCode = version.versionCode
        versionName = version.versionName
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("long", "BUILD_TIME", "${System.currentTimeMillis()}")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            signingConfig = signingConfigs.findByName("customSigning")

            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
        }
        debug {
            signingConfig = signingConfigs.findByName("customSigning")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.majorVersion
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        dex {
            useLegacyPackaging = true
        }
    }
}

dependencies {
    compileOnly(project(":abcl"))
    implementation(project(":androidc"))
    implementation(project(":ktxposed"))
    compileOnly(libs.xposed.api)
    implementation(libs.mmkv)
    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    // Ktor
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    // Accompanist
    implementation(libs.accompanist.webview)
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.placeholder)
    implementation (libs.accompanist.drawablepainter)
    // Lottie
    implementation(libs.lottie.compose)
    // Room
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)
    // Compose
    implementation(platform(libs.compose.bom))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    // https://mvnrepository.com/artifact/androidx.compose.material3/material3
    @Suppress("UseTomlInstead")
    implementation("androidx.compose.material3:material3:1.1.0-alpha08")
    implementation("androidx.compose.material:material-icons-extended")
    // Compose Rich Text
    implementation(libs.richtext.commonmark)
}
