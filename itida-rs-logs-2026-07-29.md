# ITIDA RS Signing SDK Android Logs

Date/time: 2026-07-29 14:39-14:42 EEST

## Test Environment

```text
Device: Android Emulator
Model: sdk_gphone64_arm64
Android version: 16
API level: 36
App package: com.luminsoft.EnrollTestingApp
SDK artifact: com.itida.rssigning:rssigning:1.0.17
```

## AAR Integrity / Signing Check

The AAR checksum matches the SHA-256 file included with the SDK package:

```text
rssigning-1.0.17.aar SHA-256:
c5f5393183ea304ee65ef2461b07314bd08a37197202f54e97e4e56c8a7e676c
```

JAR signing verification:

```text
rssigning-1.0.17.aar: jar is unsigned.
classes.jar inside AAR: jar is unsigned.
```

The AAR POM does not declare all runtime dependencies. We had to add Kotlin serialization JSON explicitly:

```gradle
debugImplementation "org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3"
```

Without this dependency, the SDK crashed before calling the service:

```text
java.lang.NoClassDefFoundError:
Failed resolution of: Lkotlinx/serialization/json/Json;
at com.itida.rssigning.service.RSSigning.<init>
```

## Internet Check From Emulator

```text
PING 8.8.8.8 (8.8.8.8) 56(84) bytes of data.
64 bytes from 8.8.8.8: icmp_seq=1 ttl=255 time=74.8 ms
64 bytes from 8.8.8.8: icmp_seq=2 ttl=255 time=74.5 ms

--- 8.8.8.8 ping statistics ---
2 packets transmitted, 2 received, 0% packet loss, time 1001ms
rtt min/avg/max/mdev = 74.564/74.695/74.827/0.303 ms
```

## Android Network State

Android reports the emulator default network as Wi-Fi and validated, but not an Android VPN transport:

```text
Active default network: 104
InterfaceName: wlan0
Transports: WIFI
Capabilities: NOT_METERED&INTERNET&NOT_RESTRICTED&TRUSTED&NOT_VPN&VALIDATED
```

## TCP Reachability Checks

Public URL host/port:

```text
http://197.44.231.205:8000

HTTP/1.1 400 Bad Request
Server: Microsoft-HTTPAPI/2.0
public_exit=0
```

Local URL host/port:

```text
http://197.168.1.39:8000

Terminated
local_exit=124
```

Note: although the raw TCP check to the local URL timed out in this run, the SDK call to the same base URL returned a backend error response shown below.

## SDK Call Configuration

The Android app passed these fields to the SDK:

```text
baseUrl
authUsername
authPassword
nationalId
channelId
pdfFiles: generated test PDF, "itida-test.pdf"
```

SDK call shape:

```kotlin
RSSigning.getInstance(context).sign(
    appearanceConfiguration = AppearanceConfiguration(),
    signingConfiguration = SigningConfiguration(
        authUsername = username,
        authPassword = password,
        nationalId = nationalId,
        channelId = channelId,
        baseUrl = baseUrl,
        pdfFiles = listOf(FileDetails("itida-test.pdf", samplePdfBytes())),
    ),
)
```

## SDK Result - Public URL

```text
07-29 14:41:49.850 20034 20061 I ITIDA_RS_TEST:
baseUrl=http://197.44.231.205:8000
status=FAILURE
code=3484
message=License expired!
data=ErrorDetails
```

## SDK Result - Local URL

```text
07-29 14:42:09.246 20112 20140 I ITIDA_RS_TEST:
baseUrl=http://197.168.1.39:8000
status=FAILURE
code=3484
message=License expired!
data=ErrorDetails
```

## Summary

```text
1. The emulator has internet access.
2. The public ITIDA host/port is reachable from the emulator.
3. The SDK is integrated and the sign() method is invoked successfully.
4. The latest SDK response is no longer connection failure; it is:
   code=3484, message=License expired!
5. Please confirm/provide a valid Android SDK license or confirm how the license should be configured.
```
