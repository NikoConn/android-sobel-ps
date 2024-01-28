#include <jni.h>
#include "image-ps/image/mat.h"
#include "image-ps/image/operations.h"

Mat<unsigned char> jintArray2Mat(JNIEnv* env, jintArray array, int width, int height) {
    Mat<unsigned char> out(height, width);
    jint* jint_array = env->GetIntArrayElements(array, JNI_FALSE);

    for (int i = 0; i < width * height; i++) {
        int column = i % width;
        int row = i / width;

        jint jint_value = jint_array[i];
        out.set(row, column, jint_value);
    }

    return out;
}

jintArray Mat2jintArray(JNIEnv* env, Mat<unsigned char> mat) {
    jint jint_array[mat.rows * mat.cols];
    int width = mat.cols;
    int height = mat.rows;

    for (int i = 0; i < width * height; i++) {
        int column = i % width;
        int row = i / width;

        jint_array[i] = mat.at(row, column);
    }

    jintArray out = env->NewIntArray(width * height);
    env->SetIntArrayRegion(out, 0, width * height, jint_array);
    return out;
}

extern "C" JNIEXPORT jintArray JNICALL Java_com_example_sobel_MainActivity_sobelJNI(
        JNIEnv* env,
        jobject,
        jintArray image,
        int width,
        int height
        ) {
        Mat<unsigned char> mat_image = jintArray2Mat(env, image, width, height);
        Mat<unsigned char> sobel_image = sobel(mat_image, 1, 1);
        return Mat2jintArray(env, sobel_image);
}