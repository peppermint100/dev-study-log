### Proxy가 설정이 안됨

React의 package.json에

```javascript
"proxy" : "http:localhost:<PORT>"
```

를 추가 했으나 axios나 fetch가 작동하지 않았다.

### 해결

```javascript
 "proxy": {
    "/api": {
      "target": "http://localhost:3003"
    },
    "/assets": {
      "target": "http://localhost:3003"
    }
  },
```

프록시 값을 위와 같이 변경. 타겟의 추가 자체에는 별 의미 없으나 axios나 fetch하는
url을 '/' 로만 넣어주면 리액트가 자기 자신의 html 페이지(public/index.html)을 가져오는 문제가 생겼다. 따라서 <strong>ajax를 사용하는 URL에 빈 / 가 아닌 유효안 URL을 넣어주면 작동한다.</strong>
