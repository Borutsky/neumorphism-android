# Neumorphism Android
[![](https://jitpack.io/v/Borutsky/neumorphism-android.svg)](https://jitpack.io/#Borutsky/neumorphism-android)

Neumorphic components for Android. Supports different types of shapes and several neumorphic states.
Elevation determines automatically by checking component parent views. Currently implemented:

- NeumorphicFrameLayout

<img src="https://raw.githubusercontent.com/Borutsky/neumorphism-android/master/screenshots/preview.png" height="500">

## Gradle Dependency

Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency :
```groovy
dependencies {
	implementation 'com.github.Borutsky:neumorphism-android:1.0.0-alpha01'
}
```

## Usage

Parent of Neumorphic element should always contain **android:clipChildren="false"**. It is required to avoid shadow to be clipped.
Background of the window and all the Neumorphic components should have the same color. By respecting these advices you will be able to build a Neumorphic app with unified view of all elements.

### NeumorphicFrameLayout Example:

```xml
<com.borutsky.neumorphism.NeumorphicFrameLayout
        android:layout_width="300dp"
        android:layout_height="300dp"
        app:background_color="#55B9F3"
        app:corner_radius="40dp"
        app:shape="rectangle"
        app:state="flat"/>
```

### NeumorphicFrameLayout Attributes:
```"background_color"``` -  background color <br>
```"corner_radius"``` -  rectangle corner radius: dimension <br>
```"shape"``` -  shape: rectangle or circle <br>
```"state"``` -  neumorphic state: flat, convex, concave or pressed <br>
