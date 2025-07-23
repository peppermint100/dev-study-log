package smartcast

fun main(args: Array<String>) {
    processNumber(5)
    processNumber("String")

    println(describe(5))
    println(describe("String"))
    println(describe(mutableListOf(1,2,3,4)))
}

fun processNumber(x: Any) {
    if (x is Int) { // 타입체크시 자동 형변환
        println(x + 1)
    }
}

// When을 통한 간결하고 유용한 형변환
fun describe(obj: Any): String {
    when (obj) {
        is Int -> return "숫자 $obj"
        is String -> return "문자열 $obj"
        is List<*> -> return "리스트 Object, 길이: ${obj.size}"
        else -> return "알 수 없는 객체"
    }
}