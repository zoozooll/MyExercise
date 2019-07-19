#include <jni.h>
#include <string>
#include <iostream>
#include <limits>
#include <ctime>
#include <chrono>
#include <android/log.h>

extern "C" {
static double pi_sequential(int nb_steps);

static double pi_parallel(int nb_steps);

static double pi_parallel_no_reduction(int nb_steps);
}

using namespace std;
using namespace std::chrono;
#define CURRENT_TIME_MICRO duration_cast<microseconds>( \
system_clock::now().time_since_epoch()\
).count()

#define LOG_TAG "OMP"

#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define  LOGW(...)  __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)


extern "C" JNIEXPORT void JNICALL
Java_com_aaron_openmp_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    constexpr int a[] = { 1000000 };

    std::cout.precision(std::numeric_limits< double >::max_digits10);
    long long int begin = CURRENT_TIME_MICRO;
    for (int nb_steps : a) {
        pi_sequential(nb_steps);
    }
    long long int end = CURRENT_TIME_MICRO;
    LOGI("pi_sequential spends micro seconds: %lld",  (end - begin));

    begin = CURRENT_TIME_MICRO;
    for (int nb_steps : a) {
        pi_parallel(nb_steps);
    }
    end = CURRENT_TIME_MICRO;
    LOGI("pi_parallel spends micro seconds: %lld", (end - begin)) ;

    begin = CURRENT_TIME_MICRO;
    for (int nb_steps : a) {
        pi_parallel_no_reduction(nb_steps);
    }
    end = CURRENT_TIME_MICRO;
    LOGI("pi_parallel_no_reduction spends micro seconds: %lld", (end - begin) );
}

// sequential pi calculation
double pi_sequential(int nb_steps) {

    const double dx = 1.0 / nb_steps;

    double integral = 0.0;
    for (int i = 0; i < nb_steps; ++i) {
        const double a  = i * dx;
        const double b  = a + dx;
        const double fa = 1.0 / (1.0 + a*a);
        const double fb = 1.0 / (1.0 + b*b);
        integral += fa + fb;
    }
    return 2.0 * dx * integral;
}

// parallel pi calculation with reduction directive
double pi_parallel(int nb_steps) {

    const double dx = 1.0 / nb_steps;

    double integral = 0.0;
#pragma omp parallel for schedule(static) default(none) firstprivate(nb_steps) reduction(+:integral)
    for (int i = 0; i < nb_steps; ++i) {
        const double a  = i * dx;
        const double b  = a + dx;
        const double fa = 1.0 / (1.0 + a*a);
        const double fb = 1.0 / (1.0 + b*b);
        integral += fa + fb;
    }

    return 2.0 * dx * integral;
}

// parallel pi calculation with critical directive
double pi_parallel_no_reduction(int nb_steps) {

    const double dx = 1.0 / nb_steps;

    double integral = 0.0;
#pragma omp parallel for schedule(static) default(none) firstprivate(nb_steps) shared(integral)
    for (int i = 0; i < nb_steps; ++i) {
        const double a  = i * dx;
        const double b  = a + dx;
        const double fa = 1.0 / (1.0 + a*a);
        const double fb = 1.0 / (1.0 + b*b);
#pragma omp critical
        integral += fa + fb;
    }

    return 2.0 * dx * integral;
}

