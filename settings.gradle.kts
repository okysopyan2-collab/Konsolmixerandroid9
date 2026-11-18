pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "KonsolMixerAndroid9"
include(":app")plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
kotlin.code.style=officialplugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.youtubemixerconsole"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.youtubemixerconsole"
        minSdk = 26
        targetSdk = 28 // Dioptimalkan khusus untuk Android 9 (Pie)
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
}<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://android.com">

    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />

    <application
        android:allowBackup="true"
        android:label="Konsol Mixer Pie"
        android:theme="@android:style/Theme.Material.NoActionBar">

        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <service
            android:name=".FloatingMixerService"
            android:enabled="true"
            android:exported="false" />
    </application>

MainActivity.kt & FloatingMixerService.kt)

• Buat file app/src/main/java/com/example/youtubemixerconsole/MainActivity.kt lalu tempel kode MainActivity.kt yang sudah dilengkapi pengecekan izin RECORD_AUDIO dan SYSTEM_ALERT_WINDOW.
• Buat file app/src/main/java/com/example/youtubemixerconsole/FloatingMixerService.kt lalu tempel kode lengkap konsol mengambang (10-Band EQ + Stereo LED VU + Compressor + LED Gain Reduction + Master Fader LR + Preset & Simpan).

</manifest>name: Build APK dari Browser

on:
  push:
    branches: [ "main", "master" ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - name: 1. Ambil Kode dari Repositori
      uses: actions/checkout@v4

    - name: 2. Siapkan Java 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: 3. Siapkan Android SDK
      uses: android-actions/setup-android@v3

    - name: 4. Build APK dengan Gradle Cloud
      uses: gradle/actions/setup-gradle@v3
      with:
        gradle-version: '8.4'

    - name: 5. Compile Debug APK
      run: gradle assembleDebug

    - name: 6. Simpan File APK Hasil Build
      uses: actions/upload-artifact@v4
      with:
        name: Konsol-Mixer-Android9-APK
        path: app/build/outputs/apk/debug
