## 선택 정렬

선택 정렬은 배열 중 가장 작은 값을 찾아서 가장 첫 번째 인덱스와 바꾸고 그 다음 인덱스값과 그 두 번째로 작은 값을 바꾸어서 배열을 정렬해 나갑니다.

```python
def sel_sort(list):
    for i in range(0, len(list)-1):
        min = list[i]
        for j in range(i+1, len(list)-1): # 최소값을 이미 배치한 인덱스는 제외합니다.
            if min > list[j]:
                min = list[j]
                # i 인덱스와 j 인덱스의 값을 바꿔줍니다.
                   list[i], list[j] = list[j] , list[i]
        i = i+1
    return list
```

선택 정렬은 첫 번째 반복문에서 n번의 계산을 그리고 두 번째 반복문에서 n번의 계산을 하므로 시간 복잡도는 O(n^2)이라고 할 수 있습니다.
