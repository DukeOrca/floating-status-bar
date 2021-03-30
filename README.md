# FloatingStatusBar

## References
### [[Android] Activity, Service 여러 번 호출시 인스턴스는?](https://readystory.tistory.com/52)
Activity와 Service는 안드로이드 4대 컴포넌트로써 Activity는 startActivity()를, Service는 Context.startService() 또는 bindService()를 사용하여 실행할 수 있습니다.
안드로이드 프레임워크가 제공하는 컴포넌트는 우리가 **new 키워드를 통해서 직접 생성하는 것이 아닙니다.**

#### 서비스 여러 번 호출시(when to start Service multiple)
안드로이드 개발자들의 교과서인 [*developer*](https://developer.android.com/reference/android/app/Service?authuser=1#ProcessLifecycle) 사이트에 따르면 **서비스는 여러번 호출되어도 인스턴스가 하나**입니다!

그렇다면 이미 서비스가 실행되어 있는 상태에서는 startService()가 무시될까요?

아닙니다. 이미 서비스가 있더라도 호출이 되는 순간 **onStartCommand() 메소드가 실행**되고, 만약 이를 실행할 수 없는 상태라면 onCreate() 메소드를 실행하고서 대기하게 됩니다.

만약 내가 기존의 인스턴스를 메모리 해제하고서 새로운 인스턴스를 만들고 싶다면?

안드로이드 서비스는 stopSelf() 또는 Context.stopService()를 실행하기 전까지는 인스턴스가 메모리 해제되지 않기 때문에 만약 새로운 **인스턴스를 생성하고 싶으시다면 stopSelf() 또는 context.stopService() 메소드를 실행하신 후에 다시 서비스를 호출**하시면 되겠습니다.

### [[Android O] Not allowed to start service Intent](https://parkho79.tistory.com/12)
### [Android: How to detect when App goes background/foreground](https://medium.com/@iamsadesh/android-how-to-detect-when-app-goes-background-foreground-fd5a4d331f8a)
### [Android: permission denied for window type 2038 using TYPE_APPLICATION_OVERLAY](https://stackoverflow.com/questions/46208897/android-permission-denied-for-window-type-2038-using-type-application-overlay)
### [android.app.RemoteServiceException: Context.startForegroundService() did not then](https://developside.tistory.com/96)
### [check android application is in foreground or not? [duplicate]](https://stackoverflow.com/questions/8489993/check-android-application-is-in-foreground-or-not)
### [How to detect when an Android app goes to the background and come back to the foreground](https://stackoverflow.com/questions/4414171/how-to-detect-when-an-android-app-goes-to-the-background-and-come-back-to-the-fo)
### [Sticky overlay without WindowManager.LayoutParams.TYPE_PHONE](https://stackoverflow.com/questions/55251502/sticky-overlay-without-windowmanager-layoutparams-type-phone)
### [What is the difference between the words 'setting' and 'configuration' in the context of software industry?](https://www.quora.com/What-is-the-difference-between-the-words-setting-and-configuration-in-the-context-of-software-industry)
### [When adding view to window with WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, it is not getting touch event](https://stackoverflow.com/questions/37138546/when-adding-view-to-window-with-windowmanager-layoutparams-type-system-overlay/37348311)
### [[안드로이드] 최상단 위치에 View 뛰우기 (Service, overlay, M check permission)](https://milkissboy.tistory.com/46)
