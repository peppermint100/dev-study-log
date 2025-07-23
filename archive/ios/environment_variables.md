
민감한 정보를 Xcode 환경 변수에 넣기

1. Xcode 상단 Product -> Edit Scheme
2. arguments 탭에서 키, 밸류 추가
3. 코드에서 아래와 같이 사용


```swift
struct Constants {
        static let clientID = ProcessInfo.processInfo.environment["spotify_client_id"]!
        static let clientSecret = ProcessInfo.processInfo.environment["spotify_client_secret"]!
    }

```
