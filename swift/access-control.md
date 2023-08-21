# Module과 Source File
Module은 하나의 framework를 말한다. uikit, foundation, avfoundation, visionkit등이 여기에 해당한다.

Source File은 Module 안에 있는 Swife 파일들을 의미한다.

# 접근 제어자
Swift에는 5가지 접근제어자가 있다.

## open, public
어디서든지 접근 가능하다. 하지만 public은 클래스가 정의된 모듈 내에서만 서브 클래싱이 가능하다. open은 어디서든 서브 클래싱이 가능하다.

## internal
접근 제어자를 선언하지 않으면 자동으로 선언되는 default 값이다. 모듈 내부 어디서든지 접근 가능하다.

## fileprivate
해당 소스파일 내부에서는 사용이 가능하다. 다른 소스 파일에서는 접근이 불가능 하다. 한 소스파일 내에서 하나의 클래스 안의 fileprivate 메소드는 같은 소스파일 내 다른 클래서에서 접근이 가능하다.

## private
해당 요소를 정의한 범위 내에서만 사용 가능하다.

