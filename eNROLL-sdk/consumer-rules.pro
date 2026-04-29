# ============================================================================
# eNROLL SDK - Consumer ProGuard Rules
# These rules are automatically applied to client apps that depend on this SDK
# ============================================================================

# Keep Innovatrics SDK classes from being further obfuscated by client's R8.
# Innovatrics classes are already R8-obfuscated in their AARs.
# Further obfuscation by client builds breaks internal references.
-keep class com.innovatrics.** { *; }

# Keep eNROLL SDK public API
-keep class com.luminsoft.enroll_sdk.** { *; }

# Keep JMRTD / SCUBA NFC libraries (used by Innovatrics NFC)
-keep class org.jmrtd.** { *; }
-keep class net.sf.scuba.** { *; }

# Keep BouncyCastle crypto (used by NFC passport reading)
-keep class org.bouncycastle.** { *; }
-dontwarn org.bouncycastle.**

# Keep JP2 decoder (used for JPEG2000 passport face images)
-keep class com.gemalto.jp2.** { *; }

# Innovatrics SDK references desktop Java classes not available on Android
-dontwarn java.awt.**
-dontwarn javax.swing.**
-dontwarn javax.naming.**