cmake_minimum_required(VERSION 3.4.1)

aux_source_directory(src/main/cpp/source SRC_LIST)
include_directories(libs/include  src/main/cpp)


add_library(
             libyuv
             SHARED
             src/main/cpp/yuvlib.cpp
             ${SRC_LIST})
find_library(
              log-lib
              log )

target_link_libraries(
                       libyuv
                       ${log-lib} )