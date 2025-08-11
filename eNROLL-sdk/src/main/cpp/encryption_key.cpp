#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_luminsoft_enroll_1sdk_core_utils_EncryptionHelper_getNativeKey(
        JNIEnv *env,
        jobject /* this */) {
    std::string part1 = "4D9f6H8";
    std::string part2 = "k2L3p0";
    std::string part3 = "Z1a";
    std::string fullKey = part1 + part2 + part3;

    return env->NewStringUTF(fullKey.c_str());
}
