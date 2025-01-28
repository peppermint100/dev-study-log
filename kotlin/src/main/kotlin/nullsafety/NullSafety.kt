package nullsafety

fun main(args: Array<String>) {

//    var name: String = null
    // Koltin에서는 기본적으로 모든 타입이 non-null, null을 할당할 수 없음
    var name: String? = null // ? 키워드를 통해 옵셔널로 null 할당 가능

    val length = name?.length ?: 0 // Elvis 연산자
}