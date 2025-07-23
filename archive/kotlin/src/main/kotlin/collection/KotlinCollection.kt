package collection

fun main() {
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

     // filter: 조건에 맞는 요소만 선택
    val evenNumbers = numbers.filter { it % 2 == 0 }
    println("짝수: $evenNumbers")  // [2, 4, 6, 8, 10]

    // map: 각 요소를 변환
    val squared = numbers.map { it * it }
    println("제곱: $squared")  // [1, 4, 9, 16, 25, 36, 49, 64, 81, 100]

    // filter와 map 체이닝
    val squaredEven = numbers
        .filter { it % 2 == 0 }
        .map { it * it }
    println("짝수의 제곱: $squaredEven")  // [4, 16, 36, 64, 100]

    // find: 조건에 맞는 첫 번째 요소 찾기
    val firstEven = numbers.find { it % 2 == 0 }
    println("첫 번째 짝수: $firstEven")  // 2

    // groupBy: 특정 조건으로 그룹핑
    val groups = numbers.groupBy { if (it % 2 == 0) "짝수" else "홀수" }
    println("그룹: $groups")  // {홀수=[1, 3, 5, 7, 9], 짝수=[2, 4, 6, 8, 10]}
}

class KotlinCollection {
}