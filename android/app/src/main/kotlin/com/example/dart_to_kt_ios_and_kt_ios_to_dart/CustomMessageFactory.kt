package com.example.dart_to_kt_ios_and_kt_ios_to_dart

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.FrameLayout
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

class CustomMessage(
    activity: Activity,
    channel: MethodChannel,
    context: Context,
    id: Int,
    creationParams: Map<String, String>,
) :
    PlatformView {
    private val view: View

    override fun getView(): View {
        return view
    }

    override fun dispose() {}

    init {
        val containerViewGroup = FrameLayout(context)

        val receivedParamFromDart =
            creationParams["sizedBoxHeightModifiedInNative"]?.toInt()?.plus(20)

        Handler(Looper.getMainLooper()).postDelayed({
            channel.invokeMethod("FromDartViewHeight", receivedParamFromDart)
        }, 0)

        Handler(Looper.getMainLooper()).postDelayed({
            channel.invokeMethod("FromNativeString", "Hello from kotlin!")
        }, 0)

        view = containerViewGroup
    }
}

class CustomMessageFactory(
    private val activity: Activity,
    private val channel: MethodChannel,
) :
    PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    override fun create(context: Context, viewId: Int, args: Any?): PlatformView {
        val creationParams = args as Map<String, String>
        return CustomMessage(activity, channel, context, viewId, creationParams)
    }
}