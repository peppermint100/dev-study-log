# Protocal
---
프로토콜에 대해 알게된 내용들을 정리합니다.



## 프로토콜의 메소드 필수 구현과 선택 구현
---
프로토콜에 메소드를 작성하면 기본적으로 필수 구현하도록 되어 있는데,
TableView, CollectionView의 Datasource나 Delegate는 선택적으로 구현하게 되어
있다. 어떻게 하는지 궁금해서 방법을 찾아보았다.

```swift
@objc protocal SomeProtocal{
	func a()
	@objc optional func b()
}
```

위 코드처럼 구현하면 된다. a()는 필수 구현이고 b()는 선택 구현이다.
위 방법은 objective-c에서 사용하던 문법이므로 @objc 키워드를 붙여야 한다.
또 objective-c 문법의 프로토콜은 클래스만 채택할 수 있으며 구조체나 열거형을
채택할 수는 없다

[출처](https://velog.io/@shin_ms/%ED%94%84%EB%A1%9C%ED%86%A0%EC%BD%9CProtocol%EC%9D%98-%EC%84%A0%ED%83%9D%EC%A0%81-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84)
