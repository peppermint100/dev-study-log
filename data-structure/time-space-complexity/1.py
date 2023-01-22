def sum_n(n):
    s = 0
    i = 0
    for i in range(0, n):
        i = i+1
        s = s + i
    return s


def sum_n2(n):
    return n*(n+1)/2


print(sum_n(100))
print(sum_n2(100))
