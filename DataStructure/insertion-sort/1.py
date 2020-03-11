def insert_sort(list):
    for i in range(1, len(list)):
        while i > 0 and list[i] < list[i-1]:
            list[i], list[i-1] = list[i-1], list[i]  # swap
            i = i-1


a = [3, 4, 5, 1, 2]
insert_sort(a)
print(a)
