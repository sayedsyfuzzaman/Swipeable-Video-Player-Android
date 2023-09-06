# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#********************************Start rules for gson****************************************#

-keep class com.banglalink.toffee.ui.player.** { <fields>; }

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

#-keep class com.banglalink.toffee.ui.common** { *; }

#-keepnames public class * extends androidx.fragment.app.Fragment
-keepnames public class * extends com.google.android.material.appbar.AppBarLayout.*
-keep class com.banglalink.toffee.ui.widget.AppBarLayoutBehavior {
    public <methods>;
}
-keepnames abstract class com.google.android.material.appbar.HeaderBehavior
-keepclassmembers class com.google.android.material.appbar.HeaderBehavior {
    private java.lang.Runnable flingRunnable;
    android.widget.OverScroller scroller;
}
#-keep class androidx.navigation** { *; }

-keep class androidx.mediarouter.app.MediaRouteActionProvider {*;}
-keepnames class androidx.media3.cast.CastPlayer$StatusListener
-keepclassmembers class androidx.media3.cast.CastPlayer {
    private androidx.media3.cast.CastPlayer$StatusListener statusListener;
}

#-keep class com.google.android.exoplayer2** { *; }
#-keep class com.loopnow.fireworklibrary** { *; }
#-keep class com.loopnow.fireworkplayer** { *; }
#-keep class com.banglalink.toffee.ui.firework.FireworkFragment

-dontwarn com.google.ads.**
-keep class com.google.** { *; }
-keep interface com.google.** { *; }
-keep class com.google.ads.interactivemedia.** { *; }
-keep interface com.google.ads.interactivemedia.** { *; }

-keep class * extends java.util.ListResourceBundle { *; }

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class com.medallia.** { *; } 
-dontwarn com.medallia.**
-keep class com.conviva.** { *; }

-keep class okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

-keep class retrofit2.** { *; }
-dontwarn retrofit2.**

-keep class com.google.protobuf.** { *; }
-keep class com.google.android.gms.** { *; }
-keep class com.google.firebase.** { *; }
-keep class io.invertase.firebase.** { *; }
-dontwarn io.invertase.firebase.**
-keep class io.invertase.firebase.messaging.** { *; }

-keepattributes LineNumberTable,SourceFile
-renamesourcefileattribute SourceFile
-keep public class * extends java.lang.Exception

-keep class com.google.obf.** { *; }
-keep interface com.google.obf.** { *; }

-keepattributes InnerClasses -keep class **.R -keep class **.R$* { <fields>; }
-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel

-dontwarn com.beloo.widget.chipslayoutmanager.Orientation
-dontwarn com.google.protobuf.java_com_google_ads_interactivemedia_v3__sdk_1p_binary_b0308732GeneratedExtensionRegistryLite$Loader

-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }
-keep class * extends com.google.protobuf.GeneratedMessageInfoFactory { *; }
-keep,allowobfuscation class * extends com.google.protobuf.GeneratedMessageLite { *; }
-assumevalues class com.google.protobuf.Android { static boolean ASSUME_ANDROID return true; }
-dontwarn com.google.protobuf.java_com_google_android_gmscore_sdk_target_granule__proguard_group_gtm_N1281923064GeneratedExtensionRegistryLite**
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

-dontwarn com.google.android.exoplayer2.ExoPlayer
-dontwarn com.google.android.exoplayer2.ExoPlayerLibraryInfo
-dontwarn com.google.android.exoplayer2.Format
-dontwarn com.google.android.exoplayer2.PlaybackException
-dontwarn com.google.android.exoplayer2.Player$Listener
-dontwarn com.google.android.exoplayer2.Timeline$Window
-dontwarn com.google.android.exoplayer2.Timeline
-dontwarn com.google.android.exoplayer2.Tracks$Group
-dontwarn com.google.android.exoplayer2.Tracks
-dontwarn com.google.android.exoplayer2.analytics.AnalyticsListener$EventTime
-dontwarn com.google.android.exoplayer2.analytics.AnalyticsListener
-dontwarn com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$DecoderInitializationException
-dontwarn com.google.android.exoplayer2.source.LoadEventInfo
-dontwarn com.google.android.exoplayer2.source.MediaLoadData
-dontwarn com.google.android.exoplayer2.video.VideoSize