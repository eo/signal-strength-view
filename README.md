# Signal Strength View
![Icon](/sample/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

Material design signal strength view for Android

[![Download](https://api.bintray.com/packages/eo/view/signalstrength/images/download.svg) ](https://bintray.com/eo/view/signalstrength/_latestVersion)
[![License](https://img.shields.io/badge/license-Apache%202.0-green.svg)](https://github.com/eo/battery-meter-view/blob/master/LICENSE)
[![License](https://img.shields.io/badge/minSdkVersion-19-red.svg)](https://developer.android.com/about/dashboards/)

Download
--------

```groovy
dependencies {
  implementation 'eo.view:signalstrength:1.0.0'
}
```

Usage
-----
Library contains both `SignalStrengthView` and `SignalStrengthDrawable` classes. Following XML attributes have corresponding class properties.

```xml
<eo.view.signalstrength.SignalStrengthView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:signalColor="?colorAccent"
    app:signalLevel="50"
    app:signalTheme="rounded" />
```

Style
-----
Signal strength view is styleable using `signalStrengthStyle` in your theme. `Widget.SignalStrength` can be used as a base style.

Sample
------
Download sample app under releases to play with the library

![Rounded Theme Screenshot](/images/screenshot_sample_rounded.png)
![Sharp Theme Screenshot](/images/screenshot_sample_sharp.png)

License
-------

    Copyright 2018 Erdem Orman

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
