
# 제너레이터란
함수의 실행을 잠깐 멈췄다가 다시 실행할 수 있는 기능

# 사용 예시와 next

```javascript
function* fn() {
	console.log(1);
	yield 1;

	console.log(2);
	yield 2;

	console.log(3);
	yield 3;

	console.log(4);
	yield 4;

	return "finish";
}
```

제너레이터 함수는 `function*` 이런 방식으로 선언한다.
이 후에 

```
const a = fn();
```

이렇게 함수를 할당하고

```
a.next();
```
를 실행하면

```
// console : 1
{ value: 1, done: false }
```


이렇게 다음 yield 문 까지 함수를 실행하고 yield 값과 현재 제너레이터 함수가 종료되었는지를 done에 반환한다.

끝까지 계속 실행하면 

```
{ value: "finish", done: true }
```

를 반환한다. 만약 이 후에도 더 실행하면

```
{ value: undefined, done: true }
```

돌려줄 값이 없으므로 value는 undefined로 출력된다.


# return

next외에도 return 메소드도 있는데 만약

```
a.return("END")
```

이렇게 바로 return 메소드를 실행하면 즉시

```
{ value: "END", done: true }
```
로 제너레이터를 끝내버린다.

# throw

```
a.throw(new Error("error"))
```
제너레이터를 종료하고 즉시 에러를 던진다.


즉 제너레이터는 다른 작업을 하다가 다시 돌아와서 next로 하던 작업을 이어나갈 수 있는 기능이 있다.
