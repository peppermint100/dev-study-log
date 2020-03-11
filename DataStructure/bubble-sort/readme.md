## 버블 정렬

버블 정렬은 단순한 정렬 방법 중 하나입니다. 가장 오른쪽에서 부터 인접한 인덱스를 비교하여 인덱스 값이 작은 쪽의 값이 작으면 두 자리를 바꿔주고 0번 인덱스까지 진행합니다. 그렇게 하면 가장 작은 값이 0번 인덱스에 위치하게 되고 다시 가장 오른쪽으로부터 1번 인덱스까지 같은 작업을 진행합니다. 그 다음은 2번 인덱스까지 같은 작업을 진행해주면 됩니다.

```python
def bubble_sort(list):
    for i in range(0, len(list) - 1):
        for end in range(len(list)-1, i, -1):
            if list[end] < list[end-1]:
                list[end], list[end-1] = list[end-1], list[end]
                end = end - 1
```

버블 정렬은 두 개의 반복문을 사용하므로 시간 복잡도는 O(n^2)입니다.
