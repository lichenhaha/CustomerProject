LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
LOCAL_C_INCLUDES := \
    ./libs/include

LOCAL_MODULE 	:= libyuv
LOCAL_LDLIBS 	:= -ljnigraphics -llog

LOCAL_SRC_FILES := \
    source/compare.cc           \
    source/compare_common.cc    \
    source/compare_neon64.cc    \
    source/compare_gcc.cc       \
    source/convert.cc           \
    source/convert_argb.cc      \
    source/convert_from.cc      \
    source/convert_from_argb.cc \
    source/convert_to_argb.cc   \
    source/convert_to_i420.cc   \
    source/cpu_id.cc            \
    source/planar_functions.cc  \
    source/rotate.cc            \
    source/rotate_any.cc        \
    source/rotate_argb.cc       \
    source/rotate_common.cc     \
    source/rotate_mips.cc       \
    source/rotate_neon64.cc     \
    source/rotate_gcc.cc        \
    source/row_any.cc           \
    source/row_common.cc        \
    source/row_mips.cc          \
    source/row_neon64.cc        \
    source/row_gcc.cc	        \
    source/scale.cc             \
    source/scale_any.cc         \
    source/scale_argb.cc        \
    source/scale_common.cc      \
    source/scale_mips.cc        \
    source/scale_neon64.cc      \
    source/scale_gcc.cc         \
    source/video_common.cc

ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
    LOCAL_CFLAGS += -DLIBYUV_NEON
    LOCAL_SRC_FILES += \
        source/compare_neon.cc.neon    \
        source/rotate_neon.cc.neon     \
        source/row_neon.cc.neon        \
        source/scale_neon.cc.neon
endif

include $(BUILD_STATIC_LIBRARY)

