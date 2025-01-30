package scopefuntion

/*
Scope Function(범위 지정 함수)
- 특정 객체에 대한 작업을 블록에 넣어서 실행
- 해당 블록이 범위가 되므로 범위 지정 함수라고 한다
- 수신 객체 지정 함수라고도 하는데, 블록 람다식에서 수신 객체를 람다의 입력 파라미터(수신 객체)로 지정하기 때문이다.


public inline fun <T> T{수신객체}.also(block: (T{파라미터}) -> Unit): T {

also, let -> 수신 객체를 람다의 파라미터로 지정 -> it이라는 키워드로 접근가능
apply, run, with -> 수신 객체를 수신 객체로 지정 -> this 키워드로 접근 가능

 */


class Person(
    val name: String,
    var age: Int
)

fun main() {
    val person: Person = Person("lee", 30)
    person.age = 31

    // with으로 this 참조 생략, 마지막 라인을 반환
    with(person) {
        print(name)
        print(age)
        age > 50
    }

    // also로 참조 변경 객체 자신을 반환
    person.also() {
        print(it.name)
        print(it.age)
    }

    // 수신 객체를 변경하고 수신객체 자체를 반환
    person.apply {
        age += 1
    }

    // 수신 객체를 변경하고 마지막 라인을 반환
    person.run {
        age += 1
        age > 50
    }

    val personMaybe = null

    // null check 후 코드 실행, 마지막 라인을 반환
    personMaybe.let {
        println("Person is exists")
        false
    }
}
