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
-keep class com.google.android.gms.internal.** { *; }
-keep class com.google.android.gms.measurement.** { *; }
-keep class com.google.android.gms.internal.measurement.** { *; }

-dontwarn com.google.android.gms.**
-dontwarn com.google.firebase.**

# Firebase Crashlytics
-keepattributes *Annotation*
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

# OSS Licenses
-keep class com.google.android.gms.oss.licenses.** { *; }

# ============================================
# Kakao SDK
# ============================================
-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class com.kakao.sdk.** { *; }
-dontwarn com.kakao.sdk.**

# ============================================
# Retrofit & OkHttp
# ============================================
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.**

# Retrofit (with R8 full mode)
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-keep,allowobfuscation,allowshrinking class retrofit2.Response

-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

# Retrofit 인터페이스 보호
-keep interface com.jparkbro.network.** { *; }
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# ============================================
# Kotlin Serialization
# ============================================
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keep,includedescriptorclasses class com.jparkbro.model.**$$serializer { *; }
-keepclassmembers class com.jparkbro.model.** {
    *** Companion;
}
-keepclasseswithmembers class com.jparkbro.model.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Serializable 클래스들 보호
-keep @kotlinx.serialization.Serializable class * { *; }
-keepclassmembers @kotlinx.serialization.Serializable class * {
    *;
}

# Kotlin Serialization core
-keep class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**

# ============================================
# Model 모듈 보호
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
# Hilt / Dagger
# ============================================
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

-keepclasseswithmembernames class * {
    @dagger.* <fields>;
}
-keepclasseswithmembernames class * {
    @javax.inject.* <fields>;
}
-keepclasseswithmembernames class * {
    @dagger.* <methods>;
}

-dontwarn dagger.**
-dontwarn javax.inject.**

# ============================================
# Jetpack Compose
# ============================================
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# ============================================
# DataStore
# ============================================
-keep class androidx.datastore.** { *; }
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite { *; }

# ============================================
# Navigation
# ============================================
-keep class * extends androidx.navigation.Navigator { *; }
-keep class * implements androidx.navigation.NavArgs { *; }

# ============================================
# ViewModel
# ============================================
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keep class * extends androidx.lifecycle.AndroidViewModel { *; }

# ============================================
# Coil (이미지 로딩)
# ============================================
-keep class coil.** { *; }
-dontwarn coil.**

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
