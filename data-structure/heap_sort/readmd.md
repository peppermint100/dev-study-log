## Idea

1. 힙의 중간 부분부터 root node까지 heapify를 진행한다.
2. 그렇게하면 가장 큰 수가 root node에 오게 된다.
3. heap deleting의 원리로 root node와 가장 끝 노드의 자리를 바꾸고 root node를 스택에 집어넣는다.
4. 1~3을 반복한다.
