## Hoisting

호이스팅이란 자바스크립트의 함수가 실행되기 전 안에 있는 모든 변수를 모두 함수 최상단에서 선언하는 것을 의미합니다. 자바스크립트의 Parser가 함수가 실행되기 전에 함수를 전체적을 훑고 var 변수 선언과 함수 선언문만 골라서 함수의 최상단에서 선언합니다.(let, const, 함수 표현식에서는 발생하지 않습니다.)

## var 선언

```javascript
if(true){
  var name = "pepper"
}

console.log(name)
```
ES5 이후에 자바스크립트를 배운 저는 이 코드는 원래 실행되어야 하지 않는 다는 것을 알고 있습니다.  
ES5 이전의 자바스크립트에는 var 선언만이 존재했고 var선언은 코드의 최상단에 호이스팅되기 때문에 이 코드가 작동합니다.  
즉 블록 스코핑이 되지 않았습니다.

```javascript
if(true){
  let name = "pepper"
}

console.log(name)
```

위 코드를 실행하면 레퍼런스 에러가 나오게 됩니다. name을 initialize하기 전에 사용할 수 없다는 것, 즉 변수가 가장 위에서 선언이 되는 호이스팅이 발생하지 않은 것입니다. 이렇게 호이스팅이라는 특성에 의해 자바스크립트 코드의 사이즈가 커지면 불일치가 생길 수 있기 때문에 변수의 선언에는 var가 아닌 let, const를 사용하는 것이 바람직합니다.

## 함수 선언문과 함수 표현식

함수 선언문은 일반적인 프로그래밍 언어에서의 함수 선언 방식과 같습니다.

```javascript
function sayHello() {
  // 함수 선언문
  console.log("hello");
}
```

```javascript
const sayHello = () => {
  // 함수 표현식
  console.log("hello");
};
```

함수 표현식에서는 호이스팅이 발생 하지 않습니다.

```javascript
sayHello();
sayHello2();

function sayHello() {
  console.log("hello1");
}
const sayHello2 = function() {
  console.log("hello2");
};
```

이렇게 실행하면 sayHello2는 함수 표현식으로 함수가 선언이 되었기 때문에 sayHello2가 정의 되지 않았다는 레퍼런스 에러를 볼 수 있습니다. 함수 선언문으로 선언된 sayHello는 호이스팅이 발생하여 정상적으로 실행이 되죠. 이러한 오류를 없애기 위해선 함수 표현식으로 선언된 sayHello2를 코드 가장 상단으로 올려주면 됩니다.

```javascript
const sayHello2 = function() {
  console.log("hello2");
};

sayHello();
sayHello2();

function sayHello() {
  console.log("hello1");
}
```

이렇게 하면 호이스팅이 발생하지 않는 sayHello2를 먼저 선언해줌으로서 정상적인 실행이 된다는 것을 확인 할 수 있습니다.

일반적인 프로그래밍 언어에서는 호이스팅이 없고 이러한 호이스팅 때문에 의도치 않은 오류가 생길 수 있기 때문에 가능하면 var 선언을  
지양하는 것이 좋습니다. 특히 호이스팅 오류는 개발자가 쉽게 찾아내기 힘들기 때문에 더더욱 지양해야합니다.
