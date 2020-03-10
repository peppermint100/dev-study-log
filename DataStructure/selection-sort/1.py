
def sel_sort(list):
    for i in range(0, len(list)-1):
        min = list[i]
        for j in range(i+1, len(list)-1):
            if min > list[j]:
                min = list[j]
                temp = list[i]
                list[i] = list[j]
                list[j] = temp

        i = i+1
    return list


a = [5, 1, 23, 67, 21, 72]
print(sel_sort(a))
