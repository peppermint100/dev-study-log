## 퀵 정렬

퀵 정렬은 이름에서도 볼 수 있듯이 빠르게 배열을 정렬하는 방법입니다. 이유는 분할 정복 알고리즘의 한 종류이기 때문입니다. 분할 정복 알고리즘은 이분 탐색처럼 한 번 실행시 처리해야 할 자료가 반이 되는 알고리즘으로 O(logN)의 시간 복잡도를 가지고 있습니다.

## 아이디어

퀵 정렬에서는 피벗이라는 값을 정하도록 합니다. 배열의 아무 인덱스를 정하여 피벗이라고 하고 그 수를 중심으로 앞에는 피벗보다 작은 값 뒤에는 피벗보다 큰 값을 위치시킵니다. 그리고 다시 나뉜 두 파티션에서 똑같이 피벗을 정하여 각각 퀵 정렬을 실행합니다. 이렇게 파티션을 나누면서 계속 퀵 정렬을 하면 배열이 최종적으로 정렬이 됩니다.

<img src="./img/quick-sort-concepts.png" alt="퀵정렬아이디어" style="zoom:48%;" />

## 파티션을 나누는 방법

어느 정도 퀵 정렬의 개념이 이해가 된다면 어떻게 피벗을 중심으로 배열을 분할할까요? 피벗을 정하고 배열의 양 끝 값을 low point, high point로 정합니다.

그리고 <strong>low point(index 0)부터 하나씩 인덱스를 올려가는데 단 피벗보다 큰 값이 나오면 잠시 멈춥니다. </strong>

그리고 <strong>high point 마지막 인덱스로 넘어가서 하나씩 인덱스는 낮춰가는데 단 피벗보다 작은 값이 나오면 잠시 멈춥니다.</strong>

그리고 멈추어진 두 low point와 high point 값을 바꿉니다. 그리고 low point의 다음 값부터 다시 반복을 합니다. 이렇게 하면 자연스럽게 피벗을 중심으로 피벗보다 작은 값, 피벗보다 큰 값으로 분할 됩니다. 단 low point와 high point가 서로 교차할 경우(index of low point > index of high point)에 반복문을 종료하도록 하고(분할 완료) 나누어진 파티션에서 새로운 피벗을 찾으면 됩니다.

<img src="./img/quick-sort2.png" alt="퀵정렬아이디어" style="zoom:48%;" />

## 구현

```typescript
const sort = (arr: Array<number>, low: number, high: number) => {
  if (high <= low) {
    // high가 low와 교차되면 중지합니다.
    return;
  }
  const mid = partition(arr, low, high); // 파티션을 나눕니다.
  sort(arr, low, mid - 1); //나눈 파티션을 각각 정렬합니다.
  sort(arr, mid, high);
};

const partition = (arr: Array<number>, low: number, high: number) => {
  const pivot = arr[Math.floor((low + high) / 2)]; // 중간 값으로 피벗을 정합니다.
  while (low <= high) {
    // 피벗과 비교하여 인덱스를 이동시킵니다.
    while (arr[low] < pivot) {
      low++;
    }
    while (arr[high] > pivot) {
      high--;
    }
    if (low <= high) {
      // low와 high값을 체크하고
      let temp = arr[low]; // 둘의 위치를 바꿔줍니다.
      arr[low] = arr[high];
      arr[high] = temp;
      low++; //바꾼 후 각각 한 칸 씩 당깁니다.(이미 pivot비교로 양 끝 값은 정렬할 필요가 없습니다.)
      high--;
    }
    return low;
  }
};

const quick_sort = (arr: Array<number>) => {
  return sort(arr, 0, arr.length - 1);
};

let arr: Array<number> = [5, 3, 6, 1, 2, 7, 8];
console.log("unsorted: ", arr);
quick_sort(arr);
console.log("sorted", arr);
```

타입스크립트로 구현한 퀵 정렬입니다.

_이미지 출처 https://gmlwjd9405.github.io/2018/05/10/algorithm-quick-sort.html _
