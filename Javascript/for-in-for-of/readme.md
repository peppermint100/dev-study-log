```javascript
const arr = [3, 5, 7];

arr.foo = "hello world";

for (let i in arr) {
  console.log(i);
}

for (let i of arr) {
  console.log(i);
}
```

```console
// for in의 결과
0
1
2
foo
```

for in은 배열을 모두 방문하고 마지막에 추가한 foo 프로퍼티까지 string의 형태로 보여줍니다.

```console
for of의 결과
3
5
7
```

for of는 반복가능(iterable)한 객체에만 사용할 수 있으며 배열의 내용 그대로 number의 형태로 보여줍니다.
따라서 for of는 반복가능하지 않은 object에서는 사용할 수 없습니다.

```javascript
const obj = {
  name: "lee in gyu",
  job: "developer",
  age: 26
};

for (let i of obj) {
  console.log(i);
}
```

```console
// error
TypeError: obj is not iterable
```
