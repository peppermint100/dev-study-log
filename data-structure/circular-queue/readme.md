## 원형 큐

일반적인 큐는 선입 선출의 방식으로 dequeue(queue.pop(0))되면 0번째 인덱스에 낭비가 생깁니다. 이러한 공간 낭비를 방지하기 위해 가장 앞 인덱스(front)와 가장 마지막 인덱스(rear)를 서로 연결하여 공간의 낭비가 없도록 합니다. 큐의 최대 사이즈(MAX_SIZE)를 정해놓고 index % MAX_SIZE 모듈로 연산을 통해 현재 인덱스를 계산할 수 있도록 합니다.

```python
class circular_queue:
    def __init__(self, max):
        self.max = max
        self.front = 0
        self.rear = 0
        self.size = 0
        self.list = [None] * self.max
```

먼저 큐의 사이즈를 정해주고 큐를 꺼낼(dequeue) 인덱스를 front 그리고 큐를 추가할(enqueue) 인덱스를 rear라고 정해줍니다. 그리고 현재 큐의 사이즈도 정해주고 큐의 상태를 확인하기 위해 list도 정해줍니다.

```python
    def isEmpty(self):
        return self.size == 0

    def isFull(self):
        return self.size == self.max
```

큐가 비어있는지 꽉 찼는지 확인해줄 메소드입니다. 이는 큐가 최대 크기(self.max) 이상으로 삽입 될 때 또는 큐가 비었을 때 큐를 꺼내려고 하면 에러를 발생시킵니다.

```python
    def enqueue(self, data):
        if self.isFull():
            raise Exception('queue is full')
        self.list[self.rear] = data
        self.rear = self.rear + 1 % self.max
        self.size = self.size + 1

    def dequeue(self):
        if self.isEmpty():
            raise Exception('queue is empty')

        self.list[self.front] = None
        self.front = self.front + 1 % self.max
        self.size = self.size - 1
```

삽입과 추가 메소드입니다. 현재 인덱스는 모듈로 연산을 통해야만 원형 큐가 계속 돌아갈 수 있습니다.
