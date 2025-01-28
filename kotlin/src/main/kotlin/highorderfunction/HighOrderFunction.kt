package highorderfunction

// 기본적인 람다 표현식
val sum = { x: Int, y: Int -> x + y }
val square = { x: Int -> x * x }

// 고차 함수 - 함수를 매개변수로 받거나 반환하는 함수
fun processNumbers(x: Int, y: Int, operation: (Int, Int) -> Int): Int {
    return operation(x, y)
}

// 람다를 반환하는 함수
fun getOperation(type: String): (Int, Int) -> Int {
    return when (type) {
        "sum" -> { x, y -> x + y }
        "multiply" -> { x, y -> x * y }
        else -> { x, y -> x }
    }
}

fun main() {
    // 람다 사용
    println(sum(1, 2))  // 3
    println(square(3))  // 9

    // 고차 함수 사용
    val result1 = processNumbers(10, 20) { x, y -> x + y }
    val result2 = processNumbers(10, 20) { x, y -> x * y }
    println(result1)  // 30
    println(result2)  // 200

    // 반환된 람다 사용
    val sumOperation = getOperation("sum")
    println(sumOperation(5, 3))  // 8
}

class HighOrderFunction {
}