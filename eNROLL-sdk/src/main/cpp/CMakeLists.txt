cmake_minimum_required(VERSION 3.4.1)

add_library(
        encryption_key
        SHARED
        encryption_key.cpp)

find_library(
        log-lib
        log)

# --- BEGIN: 16 KB page-size friendly linking ---
# This flag lets the linker use a max page size of 16 KB when needed.
# It’s harmless on newer NDKs (r28+ already default to proper alignment),
# but ensures compatibility if an older NDK is used by mistake.
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,-z,max-page-size=16384")
# --- END: 16 KB page-size friendly linking ---

target_link_libraries(
        encryption_key
        ${log-lib})
