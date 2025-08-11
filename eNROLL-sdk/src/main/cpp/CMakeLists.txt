cmake_minimum_required(VERSION 3.4.1)

add_library(
        encryption_key
        SHARED
        encryption_key.cpp)

find_library(
        log-lib
        log)

target_link_libraries(
        encryption_key
        ${log-lib})
