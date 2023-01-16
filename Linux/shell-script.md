## Shell Script

```
#!/bin/bash
```
를 가장 먼저 작성해준다. 파일이름은 .sh로 해주는데, 필수는 아니지만 일반적인 약속이다.

### echo
일반적인 print 문

### variable
```
hello = "hello"
world = world
```
이런식으로 변수를 선언, "를 사용하지 않으면 기본적으로 string 형태로 판단.

`echo "${hello}world"` 이런식으로 string 내에 Javascript와 비슷하게 변수를 넣을 수 있다.

### loop
```
for i in `ls`; do
  echo i
done
```
일반적으로 ` 사이에 커맨드를 넣을 수 있다. 위 shellscript는 ls의 결과를 i에 하나 씩 넣으며 echo로 출력하게 된다.

``` for i in {2..9}; do
  for j in {1..9}; do
    echo "${i} * ${j} =  $(( $i * j))"
done
```
이렇게 range를 지정해 줄 수 있다

$(( i * j))를 통해 string escape를 하여 연산을 할 수 있다. 이 방법이 아니면 `expr 1 + 2`을 사용할 수도 있다.

### 실행
`bash s1.sh` 이런 식으로 작성한 쉘 스크립트를 실행할 수 있다.

그냥 `./s1.sh`로 실행할 수도 있는데, 권한이 막혀있기 때문에 `chmod +x s1.sh`로 실행 권한을 준 다음 실행할 수 있다.


### 파라미터
$0은 파일명 즉 파일 자체를 가리키데 된다.
이 후로 $1은 첫 번째 파라미터, $2는 두 번째 파라미터를 의미 하게 된다.

```
bash ./s1.sh aaa bbb
```
이면 $1은 aaa, $2는 bbb를 뜻한다.

$#은 총 파라미터 개수를 의미하게 된다.(이를  통해 파라미터가 입력되었는지 아닌지 확인 가능, 명령어 자체는 제외)

### IF
```
if [ $# -gt 0]; then
  MSG="$1"
if
```
숫자의 비교는 -ge, -le, gt, lt, eq, ne(not equal) 등으로 비교한다.


### Array
배열은

```
arr=("aaa" "bbb" "ccc" 123)
```
이렇게 , 없이 지정할 수 있다. 띄어쓰기로 join을 해주며 ,로하면 ,를 문자열로 인식하게 된다.

```
echo "arr=$arr"
echo ${arr[0]}
```
c언어처럼 그냥 `$arr`은 첫 번째 원소를 말하고 만약 인덱스를 통해 출력하고 싶다면 ${}안에 감싸주어야 한다. 그렇지 않으면 []를 문자열로 인식하게 된다.

`${arr[@]}`은 모든 arr를 뜻하게 된다.

배열에 값을 넣고싶다면

```
arr[1]="aaa"
이런식으로 넣을 수 있다. 만약 배열의 길이보다 큰 인덱스에 값을 넣으면 값에 상관없이 stack처럼 가장 마지막에 추가 된다.


### Function

```
say_hello(){
  echo "This is $0 function"
  echo "My first name is $1"
  echo "My full name is $@"
}

say_hello "pepper" "mint100"
```

이렇게 함수를 사용할 수 있다. $#은 역시 파라미터 갯수를 뜻하고, $0은 함수 이름을 뜻하게 된다. $1, $2 순서대로 각각 첫 번째, 두번째 파라미터이며 $@은 모든 파라미터를 뜻한다.

### IFS
IFS는 전역변수로 커맨드의 아웃풋을 어떻게 구분하는지를 나타낸다. 예를 들면 

