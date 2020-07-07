g = {
    1 : [ 2,3],
    2 : [ 1,4,5],
    3 : [ 1, 6],
    4 : [ 2, 7, 8],
    5 : [ 2, 9],
    6 : [3],
    7 : [4],
    8 : [4],
    9 : [5]
}


def dfs(g, start):
    stack = []
    done = set()

    stack.append(start)
    done.add(start)

    while stack:
        p = stack.pop()
        print(p)
        for x in g[p]:
            if x not in done:
                stack.append(x) 
                done.add(x)

dfs(g, 1)