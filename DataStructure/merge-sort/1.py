def merge_sort(list):
    n = len(list)

    if n <= 1:
        return

    mid = n // 2
    g1 = list[:mid]
    g2 = list[mid:]

    merge_sort(g1)
    merge_sort(g2)

    i1 = 0
    i2 = 0
    ia = 0

    while i1 < len(g1) and i2 < len(g2):
        if g1[i1] < g2[i2]:
            list[ia] = g1[i1]
            i1 += 1
            ia += 1
        else:
            list[ia] = g2[i2]
            i2 += 1
            ia += 1

    while i1 < len(g1):
        list[ia] = g1[i1]
        i1 += 1
        ia += 1

    while i2 < len(g2):
        list[ia] = g2[i2]
        i2 += 1
        ia += 1


d = [6, 8, 3, 9, 10, 1, 2, 4, 7, 5]
merge_sort(d)
print(d)
