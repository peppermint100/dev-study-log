## Closure

---

```javascript
function outerFunc(outerVar) {
  return function innerFunc(innerVar) {
    console.log(" outerVar : ", outerVar);
    console.log(" innerVar: ", innerVar);
  };
}

const outer = outerFunc("outerVar");
outer("innerVar");
```

클로저의 기본적인 개념은 함수(외부함수, outerFunc) 내의 함수(내부함수,innerFunc)에서 외부 함수의
변수에 접근할 수 있음을 뜻한다.

```javascript
function outerFunc(outerVar) {
  const anotherOuterVar = "that variable naming";
  return function innerFunc(innerVar) {
    console.log(" outerVar : ", outerVar);
    console.log(" innerVar: ", innerVar);
    console.log("another : ", anotherOuterVar); // 이 함수의 외부함수의 변수이므로 접근가능
  };
}

const outer = outerFunc("outerVar");
outer("innerVar");
console.log(anotherOuterVar); //이는 실행되지 않는다.
```

이렇게 내부 함수에서 외부함수로의 접근이 가능한 특징은 함수형 프로그래밍 언어에서 많이 사용되는
중요한 특징 중 하나이다.

```javascript
const getData = url => {
  fetch(url)
    .then(res => {
      res.json();
      console.log("url : ", url);
    })
    .then(json => console.log(json))
    .catch(err => console.error(err));
};
```

위와 같은 fetch에서도 then으로 처리되는 프로미스의 콜백함수에서도 위에서 받는 파라미터들을
전부 내부 함수에서 접근할 수 있다.
