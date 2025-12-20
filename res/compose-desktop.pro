# 指定压缩级别
-optimizationpasses 5
# 不跳过非公共的库的类成员
-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses
# 混淆时采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
# 把混淆类中的方法名也混淆了
-useuniqueclassmembernames
# 优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification
# 将文件来源重命名为“SourceFile”字符串
-renamesourcefileattribute SourceFile
# 方法名等混淆指定配置
-obfuscationdictionary dict.txt
# 类名混淆指定配置
-classobfuscationdictionary dict.txt
# 包名混淆指定配置
-packageobfuscationdictionary dict.txt
# 保留行号
-keepattributes SourceFile,LineNumberTable
# 是否使用大小写混合
-dontusemixedcaseclassnames
# 不开启优化：https://www.guardsquare.com/manual/configuration/optimizations，启用会对 gson 进行优化，导致异常
# java.lang.NoClassDefFoundError: proguard/optimize/gson/_OptimizedJsonWriter
-dontoptimize
# 混淆时是否记录日志
-verbose
#代码优化选项，不加该行会将没有用到的类删除，这里为了验证时间结果而使用，在实际生产环境中可根据实际需要选择是否使用
#-dontshrink
#保留注解，如果不添加改行会导致我们的@Keep注解失效
-keepattributes *Annotation*
# 忽略警告，避免打包时某些警告出现
-ignorewarnings
# 预校验
#-dontpreverify
# 保护JS回调接口
-keepattributes *JavascriptInterface*
-dontwarn org.xmlpull.v1.**
-dontwarn android.content.res.**
-keep class org.xmlpull.v1.** { *; }

# 记录生成的日志数据,gradle build时在本项目根目录输出
# Apk包内所有class的内部结构
-dump class_files.txt
# 未混淆的类和成员
-printseeds seeds.txt
# 列出从Apk中删除的代码
-printusage unused.txt
# 混淆前后的映射
-printmapping mapping.txt

# 保持所有实现Serializable接口的类成员
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 保持枚举类不被混淆
-keepclassmembers enum * {
 public static **[] values();
 public static ** valueOf(java.lang.String);
}

# 过滤泛型
-keepattributes Signature

# 保持测试相关的代码
-dontnote junit.framework.**
-dontnote junit.runner.**
-dontwarn org.junit.**

# 保留 Kotlin 核心类和特性（避免数据类、密封类、扩展函数失效）
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-keepclassmembers class kotlin.Metadata {
    <fields>;
}
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault, InnerClasses, EnclosingMethod, Signature

# 保留 Kotlin 数据类（data class）的结构（避免 equals/hashCode/toString 失效）
-keepclassmembers class * extends kotlin.Enum {
    public static **[] values();
    public static** valueOf(java.lang.String);
}
#-keepclassmembers class * {
#    @kotlin.jvm.JvmField <fields>;
#    @kotlin.jvm.JvmStatic <methods>;
#}

# 保留 JVM 核心类（避免反射、异常处理等基础功能出错）
-keep class java.lang.** { *; }
-keep class java.util.** { *; }

# 保留 Compose 核心注解和类（UI 渲染依赖）
-keep class androidx.compose.** { *; }
-keep class androidx.activity.** { *; }
-keep class androidx.lifecycle.** { *; }

# 保留 @Composable 注解的函数和类（Compose 编译器依赖这些信息）
-keep @androidx.compose.runtime.Composable class * { *; }
-keep class * {
    @androidx.compose.runtime.Composable <methods>;
}

# 保留 Compose 状态相关类（如 State、MutableState）
-keep class androidx.compose.runtime.State { *; }
-keep class androidx.compose.runtime.MutableState { *; }
-keep class androidx.compose.runtime.snapshots.SnapshotStateList { *; }

# 保留 Compose 布局和组件类（避免 Button、Column 等组件失效）
-keep class androidx.compose.foundation.** { *; }
-keep class androidx.compose.material.** { *; }  # 若使用 Material 组件
-keep class androidx.compose.ui.** { *; }

# 保留协程核心类（避免 launch、async 等功能异常）
-keep class kotlinx.coroutines.** { *; }
-keep class kotlinx.coroutines.flow.** { *; }  # 若使用 Flow

# 保留协程注解（如 @OptIn(CoroutinesInternalApi::class)）
-keep class kotlinx.coroutines.internal.** { *; }  # 内部 API 依赖

# 保留 Compose Desktop 核心类
-keep class androidx.compose.desktop.** { *; }
-keep class org.jetbrains.skiko.** { *; }  # Skia 渲染引擎依赖

# 保留主程序入口（main 函数所在类，根据实际类名修改）
-keep class me.shouheng.i18n.MainKt {
    public static void main(java.lang.String[]);
}

# 保留桌面窗口配置类（如 Window、Application 等）
#-keep class * extends androidx.compose.desktop.ui.tooling.preview.Preview { *; }

# Ktor 网络库（示例）
-keep class io.ktor.** { *; }
-keep class kotlinx.serialization.** { *; }  # 若使用 Ktor 序列化

# 数据库相关
-keep class org.sqlite.** { *; }
-keep class java.sql.DriverManager { *; }

# https://github.com/vinceglb/FileKit
-keep class com.sun.jna.** { *; }
-keepclassmembers class com.sun.jna.** { *; }
-dontwarn com.sun.jna.**
-keep class * implements com.sun.jna.** { *; }

# Proguard 中涉及到的 log 工具，不添加会报错
-keep class org.apache.logging.** { *; }
# 网络接口的对象使用的注解
-keep @kotlinx.serialization.Serializable class * { *; }
# 反射修改了其中的部分逻辑
-keep class com.google.gson.stream.JsonWriter { *; }
