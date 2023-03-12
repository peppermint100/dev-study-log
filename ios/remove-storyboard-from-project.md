
1. Main.storyboard 파일을 Move to trash로 삭제
2. 프로젝트 세팅 -> Info 탭에 접근
3. Main stroyboard file base name, Application Secene Manifest.Secene Configuration.Application Session Role.Item 0.Storyboard Name 삭제
4. Scene Deligate에서 앱 실행시 보여줄 init 뷰 컨트롤러를 아래와 같이 세팅

```swift
    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        // Use this method to optionally configure and attach the UIWindow `window` to the provided UIWindowScene `scene`.
        // If using a storyboard, the `window` property will automatically be initialized and attached to the scene.
        // This delegate does not imply the connecting scene or session are new (see `application:configurationForConnectingSceneSession` instead).
        guard let windowScene = (scene as? UIWindowScene) else { return }
        
        let window = UIWindow(windowScene: windowScene)
        window.rootViewController = ViewController()
        window.makeKeyAndVisible()
        self.window = window
    }
```

5. LaunchScreen.storyboard는 건들지 않음(이 파일이 애플 기기의 다양한 화면 크기에 대한 이미지를 제공하기 때문. 이로 인해 앱이 다른 기기 화면에 맞게 적용되는 것)

[출처](https://beepeach.tistory.com/671)
