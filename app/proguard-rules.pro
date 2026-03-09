# Add project specific ProGuard rules here.

# Google Cloud Translation API
-keep class com.google.cloud.translate.** { *; }
-dontwarn com.google.cloud.translate.**

# Keep annotations for reflection
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Keep custom application class
-keep class com.example.translateapp.TranslateApplication { *; }

# Keep data classes
-keep class com.example.translateapp.** { *; }

# Keep for JSON serialization
-keep class * implements com.google.cloud.translate.** { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepclassmembers class ** {
    public void onEvent(*)(**);
}

# Retain generic type information for use by reflection
-keepattributes Signature,InnerClasses,EnclosingMethod
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault
-keepattributes *Annotation*
