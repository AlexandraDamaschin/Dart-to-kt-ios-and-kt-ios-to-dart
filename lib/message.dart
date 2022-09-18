import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/services.dart';

class Message extends StatefulWidget {
  const Message({
    required this.viewHeight,
    Key? key,
  }) : super(key: key);

  final int viewHeight;

  @override
  _MessageState createState() => _MessageState();
}

class _MessageState extends State<Message> {
  static const channel = MethodChannel('FromDartMessageChannel');

  double viewHeight = 60;
  String customTextFromNative = 'Initial text from dart';

  @override
  void initState() {
    _onListenChannel();

    super.initState();
  }

  @override
  void dispose() {
    channel.setMethodCallHandler(null);
    super.dispose();
  }

  void _onListenChannel() {
    channel.setMethodCallHandler((call) async {
      if (call.method == 'FromDartViewHeight') {
        try {
          final nativeHeight = call.arguments as int;

          setState(() {
            viewHeight = nativeHeight.toDouble();
          });
        } catch (ex) {
          setState(() {
            viewHeight = 0;
          });
        }
      }

      if (call.method == 'FromNativeString') {
        try {
          final nativeString = call.arguments as String;

          setState(() {
            customTextFromNative = nativeString;
          });
        } catch (ex) {
          setState(() {
            customTextFromNative = 'There was an error';
          });
        }
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    const viewType = 'CustomMessageFactory';
    final creationParams = <String, String>{
      'sizedBoxHeightModifiedInNative': widget.viewHeight.toString(),
    };

    final Widget platformWidget;
    switch (defaultTargetPlatform) {
      case TargetPlatform.android:
        platformWidget = PlatformViewLink(
          viewType: viewType,
          surfaceFactory: (context, controller) {
            return AndroidViewSurface(
              controller: controller as AndroidViewController,
              gestureRecognizers: const <
                  Factory<OneSequenceGestureRecognizer>>{},
              hitTestBehavior: PlatformViewHitTestBehavior.opaque,
            );
          },
          onCreatePlatformView: (params) {
            return PlatformViewsService.initSurfaceAndroidView(
              id: params.id,
              viewType: viewType,
              layoutDirection: TextDirection.ltr,
              creationParams: creationParams,
              creationParamsCodec: const StandardMessageCodec(),
              onFocus: () {
                params.onFocusChanged(true);
              },
            )
              ..addOnPlatformViewCreatedListener(params.onPlatformViewCreated)
              ..create();
          },
        );
        break;
      case TargetPlatform.iOS:
        platformWidget = UiKitView(
          viewType: viewType,
          layoutDirection: TextDirection.ltr,
          creationParams: creationParams,
          creationParamsCodec: const StandardMessageCodec(),
        );
        break;
      default:
        throw UnsupportedError('Unsupported platform view');
    }

    return Column(
      children: [
        SizedBox(
          height: viewHeight,
          child: Container(
            color: Colors.blue,
            child: platformWidget,
          ),
        ),
        Text(customTextFromNative),
      ],
    );
  }
}
