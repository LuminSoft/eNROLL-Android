# Keep the SDK classes
-keep class com.luminsoft.enroll_sdk.** { *; }

# Keep AndroidX and Material3 classes
-keep class androidx.** { *; }
-keep class com.google.android.material.** { *; }

# Keep any custom annotations
-keep @interface com.luminsoft.enroll_sdk.** { *; }

# Keep public classes and methods in your SDK
-keep public class com.luminsoft.enroll_sdk.** { public *; }

-keep class androidx.constraintlayout.widget.** { *; }