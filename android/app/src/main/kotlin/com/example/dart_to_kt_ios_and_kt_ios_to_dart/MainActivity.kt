package com.example.dart_to_kt_ios_and_kt_ios_to_dart

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {

    private lateinit var channel: MethodChannel

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "FromDartMessageChannel")

        flutterEngine.platformViewsController
            .registry
            .registerViewFactory(
                "CustomMessageFactory",
                CustomMessageFactory(activity, channel)
            )
    }
}
