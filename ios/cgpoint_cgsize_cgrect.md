# CGPoint, CGSize, CGRect

뷰를 표시하기 위해서는 4가지 요소가 필요하다.

- 뷰가 시작되는 x 좌표
- 뷰가 시작되는 y 좌표
- 뷰의 넓이 width
- 뷰의 높이 height

이를 표기하기 위해 3가지의 뷰 표시 요소가 있다.

# CGPoint

- x, y 좌표를 표시함
- 각 좌표는 CGFloat의 형태

```swift
let pos: CGPoint = .init(x: 1, y: 1)
```

# CGSize

- width, height를 표시함
- 각 크기는 CGFloat의 형태

```swift
let size: CGSize = .init(width: 100, height: 100)
```

# CGRect

- x, y, width, height를 모두 표시
- CGPoint와 CGSize를 받는다.