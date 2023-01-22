var sort = function (arr, low, high) {
    if (high <= low) {
        return;
    }
    var mid = partition(arr, low, high);
    sort(arr, low, mid - 1);
    sort(arr, mid, high);
};
var partition = function (arr, low, high) {
    var pivot = arr[Math.floor((low + high) / 2)];
    while (low <= high) {
        while (arr[low] < pivot) {
            low++;
        }
        while (arr[high] > pivot) {
            high--;
        }
        if (low <= high) {
            var temp = arr[low];
            arr[low] = arr[high];
            arr[high] = temp;
            low++;
            high--;
        }
        return low;
    }
};
var quick_sort = function (arr) {
    return sort(arr, 0, arr.length - 1);
};
var arr = [5, 3, 6, 1, 2, 7, 8];
console.log(arr);
quick_sort(arr);
console.log(arr);
