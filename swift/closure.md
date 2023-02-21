# Swift Closure

# 서론

---

클로저는 하나의 코드 블럭으로 다른 언어의 람다와 비슷하다. 예를 들면 자바의 람다처럼 함수를 하나의 파라미터로서 함수를 간결하게 표현하도록 해준다.

또 자바스크립트의 클로저처럼 클로저 내에서 사용된 변수 값을 캡처해둘 수 있다.  처음 스위프트에서 클로저 사용 문법을 봤을 때 굉장히 헷갈렸는데, 개념이 명확해 질 수록 스위프트가 얼마나 모던하게 클로저라는 개념을 보여주고 싶었는지 알 수 있었다.

간단하게 스위프트에서는 모든 함수가 전부 클로저다 라고 받아들이면 조금  편했던 것 같다.

# 사용법

---

클로저는 일반적으로 아래와 같은 형태로 사용한다.

```swift
{ (parameters) -> return type in
    statements
}
```

스위프트 랭귀지 가이드에 위와 같이 나와 있는데, 개인적으로 처음에 이걸 봤을 때 더 헷갈렸던 것 같다. 자바스크립트에서도 클로저나 프로토타입을 공부를 위해 다양한 글을 참고했을 때도 본인만의 개념으로 정립해두고 이해하는게 좋다고 했다.

단순히 코드 하이라이팅의 문제로 보이긴하지만..

내가 이해한 방식을 적용한 문법은 아래와 같다.

```swift
{ (varaible: VariableType) -> ReturnType in
  process...
}
```

# 클로저 종류

---

```swift
let names = ["julie", "silver", "pepper"]

let reversedNames = names.sorted(by: { (s1: String, s2: String) -> Bool in
    return s1 < s2
})

print(reversedNames)
```

위 처럼 함수의 파라미터내에 직접 클로저를 작성하는 것을 **인라인 클로저**라고 한다.

이러한 클로저는 줄임 및 생략이 가능한데 예를 들면 `.sorted` 의 `by` 파라미터는 이미 내부 구현에서 `String, String) -> Bool` 이 명시되어 있다. 즉 모든 타입 값을 생략하고 아래처럼 표현할 수 있다.

```swift
reversedNames = names.sorted(by: { s1, s2 in return s1 > s2 } )
```

또 이 클로저는 단일 클로저라고 하여 한 개의 표현만 들어간다. 이 경우에는 한 개의 리턴 값이 있는것이 당연하므로 `return` 키워드 조차 생략할 수 있다.

```swift
reversedNames = names.sorted(by: { s1, s2 in s1 > s2 } )
```

또 클로저에서는 `shellscript` 처럼 인자이름을 기본적으로 제공하는데, 첫 번째 파라미터 이름은 `$0` 두 번째는 `$1` 로 계속 받아올 수 있다. 즉 인자 이름조차 생략이 가능하다

```swift
reversedNames = names.sorted(by: { $0 > $1 } )
```

또 굉장히 많이 활용되는 클로저 종류 중 하나가 **후위 클로저**이다. 

```swift
func someFunctionThatTakesAClosure(closure: () -> Void) {
    // function body goes here
}
```

위 처럼 함수의 마지막 파라미터로 클로저를 받고 이 클로저가 길다면 아래와 같이 표현할 수 있다.

```swift
someFunctionThatTakesAClosure() {
    // trailing closure's body goes here
}
```

위 `sorted` 메소드를 후위 클로저를 사용해 표현하면

```swift
reversedNames = names.sorted { $0 > $1 } // 추가로 인자를 받지않으므로 () 조차 생략
```

이렇게 표현 할 수 있다.

위 클로저들은 호출되는 순간 사용을 하는데, 이 외에도 함수 밖에서 비동기로 실행되는 경우가 있다. 이런 경우에는 `@escaping` 이라는 키워드를 붙이는데, 이를 **이스케이핑 클로저** 라고 한다.

```swift
var completionHandlers: [() -> Void] = []
func someFunctionWithEscapingClosure(completionHandler: @escaping () -> Void) {
    completionHandlers.append(completionHandler)
}
```

위 코드는 `completionHandler` 라는 클로저를 `completionHandlers` 라는 배열에 추가하는 코드이다. 이 후에 `completionHandlers` 에 있는 클로저를 나중에 실행할 예정이다.

그렇다면 `someFunctionWithEscapingClosure` 라는 함수가 호출될 때 내부의 클로저를 바로 실행하지 않으므로 `@escaping` 키워드가 필요하고 추가하지 않으면 다행히도 컴파일 오류를 띄워준다.

```swift
class SomeClass {
    var x = 10
    func doSomething() {
        someFunctionWithEscapingClosure { self.x = 100 }
    }
}
```

또 위와 같이 클래스 내에서 이스케이핑 클로저를 사용할 때 클래스 내의 값을 참조하려면 명시적으로 `self` 를 붙여주어야 한다. 이유는 이스케이핑 클로저는 다른 곳에서 실행 되기 때문에 `x` 값이 소속된 클래스를 명시해줘야 하기 때문이다.

또 **자동 클로저**라는 개념도 있다. 자동 클로저는 기본적으로 인자 값이 없으며 클로저 생성시에 실행하는게 아니라 호출시에 실행된다.

```swift
var customersInLine = ["Chris", "Alex", "Ewa", "Barry", "Daniella"]
print(customersInLine.count)
// Prints "5"

let customerProvider = { customersInLine.remove(at: 0) }
print(customersInLine.count)
// Prints "5"

print("Now serving \(customerProvider())!")
// Prints "Now serving Chris!"
print(customersInLine.count)
// Prints "4"
```

`customerProvider` 는 `customersInLine` 의 0번째 값을 제거한다. 하지만 `let customerProvider = ~~` 에서 `.remove` 메소드를 설정했음에도 바로 실행되지 않아 두 번째 `print` 문의 결과가 5인것을 확인 할 수 있다.

그 이후 수동으로 `customerProvider()` 를 실행한 이 후에 실행되어 4가 출력되는 것을 확인할 수 있다.

```swift
// customersInLine is ["Alex", "Ewa", "Barry", "Daniella"]
func serve(customer customerProvider: () -> String) {
    print("Now serving \(customerProvider())!")
}
serve(customer: { customersInLine.remove(at: 0) } )
// Prints "Now serving Alex!"
```

활용의 예를 들면 `serve` 의 파라미터로 자동 클로저를 할당 받도록 한다. 이 후에 `serve` 의 실행과 동시에 `.remove` 를 실행하는 클로저를 넣어주면 그 때 클로저 내의 statement가 실행되도록 할 수 있다.

또

```swift
// customersInLine is ["Ewa", "Barry", "Daniella"]
func serve(customer customerProvider: @autoclosure () -> String) {
    print("Now serving \(customerProvider())!")
}
serve(customer: customersInLine.remove(at: 0))
// Prints "Now serving Ewa!
```

위와 같이 클로저가 들어갈 파라미터에 `@autoclosure` 를 명시해주면 이 후 `serve` 의 사용에서 중괄호를 포함하지 않고 클로저가 리턴하는 함수만 넣어서 실행할 수 도 있다.

## 값 캡쳐

---

클로저는 클로저 내의 특정 상수나 값을 캡쳐할 수 있다. 아래 코드는 내부에 `incrementer` 에 사용되는 값을 캡쳐하는 예시이다.

```swift
func makeIncrementer(forIncrement amount: Int) -> () -> Int {
    var runningTotal = 0
    func incrementer() -> Int {
        runningTotal += amount
        return runningTotal
    }
    return incrementer
}
```

내부에서 `incrementer` 함수를 생성하고 이를 반환한다. 즉 클로저를 반환하는 형태가 된다. 내부 함수만 보면

```swift
func incrementer() -> Int {
  runningTotal += amount
  return runningTotal
}
```

이런 형태인데 `amount` 값을 사용함에도 실제로 파라미터는 비어있는 것을 확인할 수 있다. 즉 외부 함수로부터 `amount` 값을 캡쳐한 것이다.

```swift
let incrementByTen = makeIncrementer(forIncrement: 10)
```

위와 같이 반환된 클로저를 통해 함수를 정한다.

```swift
let incrementByTen = makeIncrementer(forIncrement: 10)

let num = incrementByTen()
let num2 = incrementByTen()
let num3 = incrementByTen()

print(num, num2, num3) // 10, 20, 30
```

위와 같이 코드를 작성하고 결과를 확인해보자. `num, num2, num3` 을 `let` 키워드로 설정하여 각각 다른 값이지만 내부에 `amount, runningTotal` 은 캡쳐되어 10씩 증가하는 것을 볼 수 있다.

만약에 여기서

```swift
let alsoIncrementByTen = incrementByTen
let num4 = alsoIncrementByTen()

print(num, num2, num3, num4) // 10, 20, 30, 40
```

이렇게 클로저를 다른 값에 할당하고 다시 호출하면 이전에 가지고 있던 값 캡쳐를 그대로 활용하여 num4(넘사..?ㅋㅋ)가 40이 되는걸 볼 수 있다.

이는 클로저가 참조 타입이기 때문에 발생한다.

```swift
let incrementByTwo = makeIncrementer(forIncrement: 2)
```

위 처럼 새로운 클로저를 생성하면 `incrementByTen` 과 `incrementByTwo2` 는 서로 다른 값이 캡쳐되어 따로 작동하는 것을 볼 수 있다.

여기까지 보면 메모리가 값 캡쳐에 의해 남용될 수 있어 보이는데, 스위프트는 해당 클로저에 더이상 값이 사용되지 않으면 값을 더 이상 캡쳐하지 않는 등 메모리 관리를 알아서 처리해준다.

