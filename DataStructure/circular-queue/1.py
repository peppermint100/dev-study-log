class circular_queue:
    def __init__(self, max):
        self.max = max
        self.front = -1
        self.rear = 0
        self.size = 0
        self.list = [None] * self.max

    def isEmpty(self):
        return self.size == 0

    def isFull(self):
        return self.size == self.max

    def enqueue(self, data):
        if self.isFull():
            raise Exception('queue is full')
        self.list[self.rear] = data
        self.rear = self.rear + 1 % self.max
        self.size = self.size + 1

    def dequeue(self):
        if self.isEmpty():
            raise Exception('queue is empty')

        self.front = self.front + 1 % self.max
        self.list[self.front] = None
        self.size = self.size - 1


cq = circular_queue(8)
cq.enqueue("a")
cq.enqueue("b")
cq.enqueue("c")
cq.dequeue()
print("front: ", cq.front, "rear: ", cq.rear)
print(cq.list)
