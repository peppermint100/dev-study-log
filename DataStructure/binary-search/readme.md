## 탐색

자료구조에서 탐색은 순차적인 구조에서 원하는 값을 찾는 것을 의미합니다. 예를 들면

```python
a = [1, 5, 7, 10, 21, 45]
```

여기서 10이라는 숫자를 찾아서 그 인덱스를 반환하는 코드를 작성해보면

```python
def search(num, list):
    for i in range(0, len(list)):
        if list[i] == num:
            return i
    return None
```

이렇게 반복문으로 리스트의 처음부터 하나하나 살펴보면서 찾을 수 있습니다. 이 방법은 순차 탐색이라고 하며 O(n)의 계산 복잡도를 가지고 있습니다. 반복문을 사용하므로 리스트가 커질수록 계산의 수가 많아지기 때문입니다.

## 이진 탐색

이진 탐색 또는 이분 탐색이라고 하는 탐색 방법은 순차 탐색의 계산 복잡도를 개선하기 위해 등장했습니다. 원리는 간단합니다. 전체 리스트의 중간 값과 찾으려는 숫자의 값을 비교하여 큰지 작은지를 결정하고 탐색의 방향을 작은 쪽으로 할 것인지 큰 쪽으로 할 것인지를 정하는 것입니다.

```python
def binary_search(num, list):
    start = 0
    end = len(list) - 1

    while start <= end:
        mid = (start + end) // 2 # 먼저 중간지점을 찾아 줍니다.
        if list[mid] == num: # 우리가 찾은 중간 지점의 데이터가 우리가 찾는 데이터라면
            return mid # 리턴합니다
        elif list[mid] > num: # 만약 다를 경우 크기를 비교해서 시작지점 또는
            end = mid - 1     # 끝 지점을 옮겨 줍니다.
        elif list[mid] < num:
            start = mid + 1
    return None
```

이러한 이진 탐색은 한번 계산할 때마다 우리가 계산해야할 남은 양이 반씩 줄어듭니다. 이진 탐색은 계산 복잡도는 O(logn)입니다.
