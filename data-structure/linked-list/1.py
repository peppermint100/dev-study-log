class Node:
    def __init__(self, data):
        self.data = data
        self.next = None


class singly_linked_list:
    def __init__(self):
        self.head = None

    def append(self, node):
        if self.head == None:
            self.head = node

        else:
            currentNode = self.head
            while currentNode.next != None:
                currentNode = currentNode.next
            currentNode.next = node

    def insertByIdx(self, node, idx):
        if self.head == None:
            raise Exception('List is Empty')

        else:
            if idx == 0:
                node.next = self.head
                self.head = node
            else:
                currentNode = self.head
                currentIdx = 0
                while currentNode.next != None:
                    currentNode = currentNode.next
                    currentIdx = currentIdx + 1
                    if(currentIdx == idx-1):
                        node.next = currentNode.next
                        currentNode.next = node
                        break

    def deleteByIdx(self, idx):
        if self.head == None:
            raise Exception("List is Empty")
        else:
            currentIdx = 0
            currentNode = self.head
            while currentNode.next != None:
                if(currentIdx == idx-1):
                    prevNode = currentNode
                    currentNode = prevNode.next
                    nextNode = currentNode.next
                    prevNode.next = nextNode
                    break
                else:
                    currentNode = currentNode.next
                    currentIdx = currentIdx + 1

    def show(self):
        if self.head == None:
            raise Exception("List is Empty")
        else:
            currentNode = self.head
            while currentNode != None:
                print(currentNode.data)
                currentNode = currentNode.next


node = Node(1)
node2 = Node(2)
node3 = Node(3)
node4 = Node(4)
nodea = Node("a")
linked_list = singly_linked_list()

linked_list.append(node)
linked_list.append(node2)
linked_list.append(node3)
linked_list.append(node4)

linked_list.insertByIdx(nodea, 2)

linked_list.show()
