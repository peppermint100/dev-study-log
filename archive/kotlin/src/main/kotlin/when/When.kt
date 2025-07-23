package `when`

fun main() {
    // 기본적인 when 사용
    val number = 3
    when (number) {
        1 -> println("One")
        2 -> println("Two")
        3 -> println("Three")
        else -> println("Other")
    }

    // 표현식으로 사용 (값 반환)
    val numberStr = when (number) {
        1, 2 -> "작은 수"  // 여러 값을 한번에 매칭
        in 3..10 -> "중간 수"  // 범위 검사
        !in 100..999 -> "세자리 수가 아님"  // 범위 밖 검사
        else -> "큰 수"
    }

    // 조건식 사용
    val x = 10
    val y = 20
    when {
        x > y -> println("x가 더 큼")
        x < y -> println("y가 더 큼")
        else -> println("x와 y는 같음")
    }

    // when을 early return으로 사용
    fun processNumber(num: Int?) {
        when {
            num == null -> return
            num < 0 -> return
            num > 100 -> return
            else -> println("유효한 숫자: $num")
        }
    }
}

class When {
}