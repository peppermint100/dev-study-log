
# Sync, Async와 Serial, Concurrent
Sync, Async는 작업을 하나씩 동기적으로 실행할지 비동기적으로 바로 시작할지이고
Serial, Concurrent는 작업을 순차적으로 처리할지 병렬적으로 처리할지에 대한 개념이다.
비슷해보이지만 다르다.

# Deadlock
main queue에서 sync 작업을 열고 그 안에서 main queue에 또 작업을 할당하면 데드락이 발생한다.

```swift
DispatchQueue.main.sync {
    DispatchQueue.main.sync {
        print("deadlock")
    }
}
```

이렇게 하면 
1. 첫 번째 메인 스레드가 싱크작업을 위해 진행을 멈춘다
2. 내부에서 메인 스레드의 싱크작업이 시작되려 하지만 1번에서 멈췄기 때문에 시작되지 못한다.
3. 데드락

같은 의미로 하나의 스레드 싱크 내에서 그 스레드에 싱크 작업을 실행하는 것도 안된다.

[출처](https://jeong9216.tistory.com/514)
