def search(num, list):
    for i in range(0, len(list)):
        if list[i] == num:
            return i
    return -1


a = [1, 5, 7, 10, 21, 45]

# print(search(10, a))


def binary_search(num, list):
    start = 0
    end = len(list) - 1

    while start <= end:
        mid = (start + end) // 2
        if list[mid] == num:
            return mid
        elif list[mid] > num:
            end = mid - 1
        elif list[mid] < num:
            start = mid + 1
    return None


print(binary_search(10, a))
