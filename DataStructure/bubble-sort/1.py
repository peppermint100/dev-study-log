def bubble_sort(list):
    for i in range(0, len(list) - 1):
        for end in range(len(list)-1, i, -1):
            if list[end] < list[end-1]:
                list[end], list[end-1] = list[end-1], list[end]
                end = end - 1


a = [4, 5, 2, 3, 1]
bubble_sort(a)
print(a)
