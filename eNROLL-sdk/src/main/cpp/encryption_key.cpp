#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_luminsoft_enroll_1sdk_core_utils_EncryptionHelper_getNativeKey(
        JNIEnv *env,
        jobject) {
    std::string part1 = "4D9f6H8";
    std::string part2 = "k2L3p0";
    std::string part3 = "Z1a";
    std::string fullKey = part1 + part2 + part3;

    return env->NewStringUTF(fullKey.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_luminsoft_enroll_1sdk_core_utils_FirebaseKeys_getNativeApiKey(
        JNIEnv *env, jobject) {
    // split/obfuscate as you like
    std::string a1 = "AIzaSyDW";
    std::string a2 = "bbXDAkQVjEybtFWwRsR";
    std::string a3 = "hxLN3yMKD_GI";
    std::string apiKey = a1 + a2 + a3;
    return env->NewStringUTF(apiKey.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_luminsoft_enroll_1sdk_core_utils_FirebaseKeys_getNativeProjectId(
        JNIEnv *env, jobject) {
    // split/obfuscate as you like
    std::string a1 = "477";
    std::string a2 = "8190";
    std::string a3 = "23880";
    std::string apiKey = a1 + a2 + a3;
    return env->NewStringUTF(apiKey.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_luminsoft_enroll_1sdk_core_utils_FirebaseKeys_getNativeApplicationId(
        JNIEnv *env, jobject) {
    // split/obfuscate as you like
    std::string a1 = "b68d9";
    std::string a2 = "90024d";
    std::string a3 = "3a4bdf257bd";
    std::string apiKey = a1 + a2 + a3;
    return env->NewStringUTF(apiKey.c_str());
}
/*
-keep class com.luminsoft.enroll_sdk.core.utils.EncryptionHelper { *; }
-keepclasseswithmembernames class * { native <methods>; }
 */
