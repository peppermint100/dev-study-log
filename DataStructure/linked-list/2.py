class Node:
    def __init__(self, data):
        self.next = None
        self.prev = None
        self.data = data


class double_linked_list:
    def __init__(self):
        self.head = None

    def append(self, node):
        if(self.head == None):
            self.head = node
        else:
            currentNode = self.head
            while currentNode.next != None:
                currentNode = currentNode.next
            node.prev = currentNode
            currentNode.next = node

    def show(self):
        if self.head == None:
            raise Exception('List is empty')
        else:
            currentNode = self.head
            print(currentNode.data)
            while currentNode.next != None:
                currentNode = currentNode.next
                print(currentNode.data)

    def size(self):
        if self.head == None:
            return 0
        else:
            count = 1
            currentNode = self.head
            while currentNode.next != None:
                count = count + 1
                currentNode = currentNode.next
            return count

    def insertByIdx(self, node, idx):
        if self.size() == 0:
            raise Exception('List is empty')


node = Node(1)
node2 = Node(2)
node3 = Node(3)
node4 = Node(4)
nodea = Node("a")
linked_list = double_linked_list()

linked_list.append(node)
linked_list.append(node2)
linked_list.append(node3)
linked_list.append(node4)
print(linked_list.size())
# linked_list.show()
