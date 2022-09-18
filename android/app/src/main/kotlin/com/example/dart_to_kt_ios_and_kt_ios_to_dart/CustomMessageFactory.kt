package com.example.dart_to_kt_ios_and_kt_ios_to_dart

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentContainer
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformViewFactory


class CustomMessage (
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
        val osmLayoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val osm = KlarnaOSMViewExtended(context, channel)

        val containerViewGroup = FrameLayout(context)
        containerViewGroup.addView(osm, 0, osmLayoutParams)

        view = containerViewGroup
    }
}

class KlarnaOSMViewExtended(context: Context, private val channel: MethodChannel)  {
        Handler(Looper.getMainLooper()).postDelayed({
            channel.invokeMethod("KlarnaOSMViewHeight", 20)
        }, 0)
}

class KlarnaOnSiteMessageFactory(
    private val activity: Activity,
    private val channel: MethodChannel,
) :
    PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    override fun create(context: Context, viewId: Int, args: Any?): PlatformView {
        val creationParams = args as Map<String, String>
        return CustomMessage(activity, channel, context, viewId, creationParams)
    }
}