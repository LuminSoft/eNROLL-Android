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

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -falign-functions=16 -falign-jumps=16 -falign-loops=16")
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,--no-undefined")
