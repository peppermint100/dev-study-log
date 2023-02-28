
# 메모리 구조
프로세스 즉 하나의 앱이 실행되면 OS에서 메모리 공간을 4가지로 나누어 할당해준다.
종류에는 Code, Data, Heap, Stack이 있다

## Code
- 소스코드가 기계어 형태로 저장된다.
- 컴파일 타임에 결정되고, 중간에 코드가 변경되지 않도록 Read Only 상태로 저장된다.

## Data
- 전역 변수, static 변수가 저장된다.
- 프로그램 시작과 동시에 할당되며 프로그램이 종료되면 메모리에서 해제된다.
- 실행 도중 변수 값이 변경될 수 있으므로 read write 형태로 저장된다.

## Heap
- ARC에 의해서 관리되는 영역이다. 런타임에 결정되며 데이터 크기가 확실하지 않은 가변적인 데이터들을 담고 있다.
- Class 인스턴스, Closure, String, Array, Dictionary, Set 등이 저장된다. 위 타입들은 내부 데이터를 컴파일 타임에 정확히 알기 어렵기 때문에 Heap에 저장한다.
- Struct 안의 Class, Class 안의 Struct 모두 가변성이 있으므로 Heap에 저장
- 할당, 해제 작업이 ARC에 의해 계속 일어나므라 속도가 느림
- 힙 경합(2개 이상 스레드가 동시 접근시 lock이 걸림) 작업으로 인한 속도 저하

## Stack
- 함수의 지역변수, 파라미터, 리턴 값 등등이 저장된다.
- 함수가 종료되면 메모리를 해제 한다.
- 메모리 할당/해제 과정이 간단하다. 함수가 실행되면 할당하고 함수가 끝나면 이전 Stack 포인터로 돌아가서 해제하면 된다. 즉 Heap에 저장되는 타입들 보다 효율적이고 빠르다.
- 너무 큰 메모리는 가변적인 Heap에 저장해야 한다. 만약 그렇지 않고 Stack에서 무한 루프, 무한 재귀가 호출되면 Stack Overflow 발생

## Heap과 Stack
- Heap과 Stack은 같은 메모리 공간을 사용한다. Heap은 낮은 메모리 주소 부터 할당 받으며 Stack은 높은 메모리 주소부터 할당 받는다.
- Heap이 자신의 영역 외로 메모리를 확장하려고 하면 Heap Overflow 발생



[출처1](https://velog.io/@leeyoungwoozz/Swift-%EB%A9%94%EB%AA%A8%EB%A6%AC-%EA%B5%AC%EC%A1%B0)
[출처2](https://babbab2.tistory.com/25)


