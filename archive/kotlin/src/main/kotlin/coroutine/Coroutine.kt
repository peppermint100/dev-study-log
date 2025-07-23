package coroutine

import kotlinx.coroutines.*
import kotlin.random.Random

fun main() = runBlocking {  // 코루틴의 시작점
    // 1. 기본적인 코루틴 실행
    println("시작")

    launch {  // 새로운 코루틴 시작
        delay(1000L)  // 1초 대기 (Thread.sleep과 비슷하지만 스레드를 차단하지 않음)
        println("코루틴 내부")
    }

    println("끝")

    // 2. async를 사용한 비동기 결과 받기
    val deferred = async {
        delay(1000L)
        "비동기 결과"  // 반환값
    }

    println("async 결과: ${deferred.await()}")  // await로 결과 기다림

    // 3. 여러 비동기 작업 동시 실행
    val result1 = async {
        delay(1000L)
        "첫 번째 결과"
    }

    val result2 = async {
        delay(1000L)
        "두 번째 결과"
    }

    println("두 결과: ${result1.await()} , ${result2.await()}")

    processNumbers()
}

/*
Dispatcher 종류
1. Dispatchers.Default
- CPU 집약적인 작업에 사용
- 복잡한 계산, 리스트 정렬, JSON 파싱 등
- 코어 수에 비례한 스레드 풀 사용

2. Dispatchers.IO
- 입출력 작업에 최적화
- 파일 읽기/쓰기, 네트워크 요청, DB 작업
- 대규모 스레드 풀 사용

3. Dispatchers.Main
- UI 관련 작업을 위한 메인 스레드 사용
- 안드로이드에서 주로 사용
- 일반 스프링 애플리케이션에서는 거의 사용 안 함

4. Dispatchers.Unconfined
- 특별한 디스패처를 지정하지 않음
- 처음에는 호출한 코루틴의 스레드에서 실행
- 중단 후에는 다른 스레드에서 실행될 수 있음
- 사용을 권장하지 않음
 */
suspend fun processNumbers() {
    // coroutineScope는 자신을 호출한 컨텍스트의 디스패처를 사용
    coroutineScope {
        launch(Dispatchers.Default) {
            val random = Random.nextInt(100, 500)
            delay(random.toLong())
            println("1")
        }
        launch(Dispatchers.Default) {
            val random = Random.nextInt(100, 500)
            delay(random.toLong())
            println("2")
        }
        launch(Dispatchers.Default) {
            val random = Random.nextInt(100, 500)
            delay(random.toLong())
            println("3")
        }
        launch(Dispatchers.Default) {
            val random = Random.nextInt(100, 500)
            delay(random.toLong())
            println("4")
        }
    }
}

// 순서 보장 1
suspend fun processNumbersInOrder() {
    coroutineScope {
        // 방법 1: launch를 순차적으로 실행
        launch { println("1") }.join()
        launch { println("2") }.join()
        launch { println("3") }.join()

        // 방법 2: withContext 사용
        withContext(Dispatchers.Default) { println("4") }
        withContext(Dispatchers.Default) { println("5") }
        withContext(Dispatchers.Default) { println("6") }

        // 방법 3: 하나의 launch 안에서 순차 실행
        launch {
            println("7")
            println("8")
            println("9")
        }
    }
}


// 순서 보장 2
suspend fun processNumbersWithAsync() {
    coroutineScope {
        val result1 = async { "1" }
        val result2 = async { result1.await() + "2" }
        val result3 = async { result2.await() + "3" }
        println(result3.await())  // "123" 출력 보장
    }
}

class Coroutine {
}