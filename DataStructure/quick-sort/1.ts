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
