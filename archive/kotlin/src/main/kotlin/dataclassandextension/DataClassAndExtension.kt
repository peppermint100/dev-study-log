package dataclassandextension

fun main(args: Array<String>) {
    val user = User(1, "lee", "lee@naver.com")
    println(user.nickname)
}

// Java의 DTO/VO 클래스를 대체
data class User (
    val id: Long,
    val name: String,
    val email: String
) {
    // 커스텀 Getter
    val isValid: Boolean
        get() = email.contains("@")

    // backing field를 사용하는 프로퍼티
    var nickname: String = name
        get() = field.uppercase()  // getter
        set(value) {               // setter
            field = value.trim()   // field는 backing field를 가리킴
        }

    // 계산된 프로퍼티 (computed property)
    val nameWithEmail: String
        get() = "$name <$email>"
}

class DataClassAndExtension {
}

// 확장 함수를 통해 기존 클래스에 새로운 메소드 추가
fun String.removeFirstAndLast(): String {
    if (length <= 2) return this
    return substring(1, length - 1)
}