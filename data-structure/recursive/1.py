def fac(n):
    s = 1
    for i in range(1, n + 1):
        s = s * i
    return s


print(fac(3))


def fac2(n):
    if(n == 0):
        return 1
    else:
        return n * fac2(n-1)


print(fac2(5))
