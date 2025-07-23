# Frame, Bounds

Frame과 Bounds는 모두 CGRect로 이루어져 있다. 즉 위치와 크기를 나타낸다.

# Frame

- 현재 뷰의 한단계 위 레이어(이하 SuperView)의 좌표계에서 위치와 크기를 나타냄
- Frame의 CGRect의 CGPoint는 SuperView의 origin으로부터 얼마나 떨어져 있는지를 나타낸다.
- Frame의 CGRect의 CGSize는 뷰 전체에 대한 Size를 나타냄. 즉 사각형의 경우 사각형의 넓이, 높이가 그대로지만 사각형을 Rotate 했을 경우 CGSize가 커짐.
- 즉 View를 모두 감싸는 사각형의 width, height가 Size가 된다.
- 같은 사각형이어도 Rotate가 되느냐에 따라서 CGPosition, CGSize가 달라질 수 있다.
- 계층이 여러개인 뷰에서 한 뷰의 CGPosition을 변경하면 변경된 뷰 아래 계층의 뷰들의 CGPosition도 변경 된다.

```swift
- 뷰의 위치와 크기를 잡을 때 사용한다.
```

# Bounds

- 뷰 자기 자신의 좌표계에서 위치와 크기를 나타낸다.
- 자기 자신을 기준으로 하기 때문에 뷰 생성시 무조건 CGPosition은 (0, 0)
- Bounds의 CGSize는 뷰 자체의 영역을 나타낸다. 즉 Rotate해도 똑같다.
- CGPosition도 Rotate해도 변하지 않는다. 원점에서 그대로 회전시키기 때문
- 계층이 여러개인 뷰에서 한 뷰의 CGPosition을 변경하면 해당 Viewport의 위치가 변경된다.

```swift
- 회전한 뷰의 크기를 알고 싶을 때 사용한다.
- 뷰 내부에 그림을 그릴 때 사용한다.(Frame은 SuperView를 기준으로 함)
- ScrollView에서 스크롤 할 때 사용(스크롤은 큰 화면에서 어디를 보여줄지를 선택함)
```

[출처](https://babbab2.tistory.com/46)