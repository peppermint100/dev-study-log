def gcd(a, b):
    k = min(a, b)
    for i in range(k, 1, -1):
        if a % i == 0 and b % i == 0:
            return i
    return 1


print(gcd(10, 5))


def gcd2(a, b):
    k = min(a, b)
    if(b == 0):
        return a
    return gcd2(b, a % b)


print(gcd2(10, 5))
