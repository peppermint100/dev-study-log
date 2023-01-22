- float가 걸린 요소들은 height를 가지지 않는다

```css
.content{
    float:left;
    width:  29.33%;
    margin :1%;
    padding:1%;
}
```
를 통해 3개의 요소를 33.33%의 width를 가지게 하여 정렬할 수 있다.

- float로 인해 height를 가지지 않게 된 요소를 독립시키고 싶다면 침범하는 요소에 

```css
.content-interrunting{
    clear:both;
}
```

를 사용하면 된다.