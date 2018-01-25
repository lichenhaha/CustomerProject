cmake_minimum_required(VERSION 3.4.1)

# 查找cpp目录下的所有源文件
# 并将名称保存到 DIR_LIB_SRCS 变量
aux_source_directory(src/main/jni/ DIR_LIB_SRCS)
#生成连接库
add_library(libgif SHARED ${DIR_LIB_SRCS})

#add_library( native-lib SHARED
#            src/main/jni/libgif.cpp
#            src/main/jni/*.h
#            src/main/jni/*.c)

find_library( log-lib log)
find_library(jnigraphics-lib jnigraphics)


#导入jni目录下的所有头文件
include_directories(src/main/jni/)

#set(DIR ../../../../libs)

#add_library(avcodec-56 SHARED IMPORTED)
#set_target_properties(avcodec-56 PROPERTIES IMPORTED_LOCATION ${DIR}/armeabi/libavcodec-56.so)

#add_library(avdevice-56 SHARED IMPORTED)
#set_target_properties(avdevice-56  PROPERTIES IMPORTED_LOCATION  ${DIR}/armeabi/libavdevice-56.so)

#add_library(avformat-56  SHARED  IMPORTED)
#set_target_properties(avformat-56 PROPERTIES IMPORTED_LOCATION  ${DIR}/armeabi/libavformat-56.so)

#add_library(avutil-54 SHARED IMPORTED)
#set_target_properties(avutil-54  PROPERTIES IMPORTED_LOCATION  ${DIR}/armeabi/libavutil-54.so)

#add_library(postproc-53  SHARED  IMPORTED)
#set_target_properties(postproc-53  PROPERTIES IMPORTED_LOCATION ${DIR}/armeabi/libpostproc-53.so)

#add_library(swresample-1  SHARED IMPORTED)
#set_target_properties(swresample-1 PROPERTIES IMPORTED_LOCATION  ${DIR}/armeabi/libswresample-1.so)

#add_library(swscale-3  SHARED  IMPORTED)
#set_target_properties(swscale-3  PROPERTIES IMPORTED_LOCATION    ${DIR}/armeabi/libswscale-3.so)

#add_library(avfilter-5 SHARED  IMPORTED)
#set_target_properties(avfilter-5 PROPERTIES IMPORTED_LOCATION  ${DIR}/armeabi/libavfilter-5.so)



#find_package(Threads REQUIRED)
#target_link_libraries (commenlib ${CMAKE_THREAD_LIBS_INIT})

#find_package(Threads REQUIRED)
#target_link_libraries(test-lib.cpp Threads::Threads)

#add_library(ffmpeg SHARED IMPORTED)
#set_target_properties(ffmpeg PROPERTIES IMPORTED_LOCATION  ${DIR}/armeabi/libffmpeg.so)

#add_library(opencv_java3 SHARED IMPORTED)
#set_target_properties(opencv_java3  PROPERTIES IMPORTED_LOCATION ${DIR}/armeabi-v7a/libopencv_java3.so)

target_link_libraries( libgif
                       ${log-lib}
                       ${jnigraphics-lib})
