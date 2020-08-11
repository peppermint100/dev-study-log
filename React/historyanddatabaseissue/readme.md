## Issue
데이터베이스를 업데이트 후 

```
history.push(URL)
```
을 통해 업데이트 된 데이터베이스를 렌더링한 리액트 페이지에 갈 때 업데이트 되기 이전의 내용이 렌더링 되는 문제

## Solved

```
history.push(URL, {state: MESSAGE})
```
를 통해 state에 변화를 준 후

최종적으로 데이터베이스의 내용을 렌더링 하는 페이지의 useEffect에

```
useEffect(()=>{
    getData()
}, [history.location.state])
```
위 처럼 history.location.state 의존성을 추가하면 state를 기반으로 다시 데이터베이스를 fetch하여 렌더링할 수 있다.