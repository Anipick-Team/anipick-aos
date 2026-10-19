# ============================================
# AniPick ProGuard Rules
# ============================================

# ============================================
# 디버깅 정보 보존 (Crashlytics 스택트레이스용)
# ============================================
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# ============================================
# Firebase & Google Analytics
# ============================================
-keep class com.google.android.gms.** { *; }
-keep class com.google.firebase.** { *; }

-dontwarn com.google.android.gms.**
-dontwarn com.google.firebase.**

# Firebase Crashlytics
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
-keep class com.google.firebase.crashlytics.** { *; }

# Firebase Performance
-keep class com.google.firebase.perf.** { *; }

# ============================================
# Google Play Services
# ============================================
-keep class com.google.android.play.** { *; }
-keep interface com.google.android.play.** { *; }
-dontwarn com.google.android.play.**

# ============================================
# Kakao SDK
# ============================================
-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class com.kakao.sdk.** { *; }
-dontwarn com.kakao.sdk.**

# Retrofit: Kakao SDK가 내부에서 쓴다 (앱 네트워크는 Ktor)
# 빠지면 R8 full mode가 Call<T> 제네릭 시그니처를 지워 "Unable to create call adapter"로 크래시
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-keep,allowobfuscation,allowshrinking class retrofit2.Response

-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# ============================================
# OkHttp (Coil 이미지 로딩, Kakao SDK)
# ============================================
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.**

# OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# ============================================
# Kotlin Serialization
# ============================================
-dontnote kotlinx.serialization.AnnotationsKt

# Serializable 클래스들 보호
-keep @kotlinx.serialization.Serializable class * { *; }
-keepclassmembers @kotlinx.serialization.Serializable class * {
    *;
}

# Kotlin Serialization core
-keep class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**

# ============================================
# Model 모듈 보호 (v1 소셜 로그인 요청/응답 모델)
# ============================================
-keep class com.jparkbro.model.** { *; }
-keepclassmembers class com.jparkbro.model.** { *; }

# ============================================
# Enum 클래스 보호
# ============================================
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    **[] $VALUES;
    public *;
}

# ============================================
# Jetpack Compose
# ============================================
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# ============================================
# DataStore
# ============================================
-keep class androidx.datastore.** { *; }

# ============================================
# ViewModel
# ============================================
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keep class * extends androidx.lifecycle.AndroidViewModel { *; }

# ============================================
# Coroutines
# ============================================
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# ============================================
# Kotlin
# ============================================
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# ============================================
# Gson (TypeAdapter)
# ============================================
-keep class * extends com.google.gson.TypeAdapter

# ============================================
# General Android
# ============================================
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.view.View

# Parcelable
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

# Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# ============================================
# R8 Full Mode 호환성
# ============================================
-allowaccessmodification
-repackageclasses

# Missing class 경고 무시
-dontwarn java.lang.invoke.StringConcatFactory
