# AlarmClock
Sử dụng phiên bản Android Studio Cá heo

## Các chức năng
- Đặt nhiều báo thức
- Đếm ngược thời gian (tối đa 1 tiếng)
- Đồng hồ bấm giờ

## Cấu hình và liên quan
* Cấu hình trong build.gradle (app)

    
        plugins {
            id 'com.android.application'
        }

        android {
            namespace 'n03.group3.alarmapp'
            compileSdk 33

            defaultConfig {
                applicationId "n03.group3.alarmapp"
                minSdk 30
                targetSdk 33
                versionCode 1
                versionName "1.0"

                testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                release {
                    minifyEnabled false
                    proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
                }
            }

            buildFeatures {
                viewBinding true
            }

            compileOptions {
                sourceCompatibility JavaVersion.VERSION_1_8
                targetCompatibility JavaVersion.VERSION_1_8
            }
        }

        dependencies {
            implementation 'androidx.appcompat:appcompat:1.6.1'
            implementation 'com.google.android.material:material:1.8.0'
            implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
            testImplementation 'junit:junit:4.13.2'
            androidTestImplementation 'androidx.test.ext:junit:1.1.5'
            androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
        }
    
* Máy ảo sử dụng để thử nghiệm: Pixel 6 API 30
