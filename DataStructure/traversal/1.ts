class Tree<T> {
  private data: T;
  private left: null | Tree<T>;
  private right: null | Tree<T>;
  private root: boolean;

  constructor(theData: T) {
    this.data = theData;
    this.left = null;
    this.right = null;
    this.root = false;
  }

  setRoot() {
    this.root = true;
  }

  setLeft(node: Tree<T>) {
    this.left = node;
  }

  setRight(node: Tree<T>) {
    this.right = node;
  }

  preOrder(node: Tree<T>) {
    if (node) {
      console.log(node.data);
      this.preOrder(node.left);
      this.preOrder(node.right);
    }
  }
  inOrder(node: Tree<T>) {
    if (node) {
      this.inOrder(node.left);
      console.log(node.data);
      this.inOrder(node.right);
    }
  }
  postOrder(node: Tree<T>) {
    if (node) {
      this.postOrder(node.left);
      this.postOrder(node.right);
      console.log(node.data);
    }
  }
}

const node1 = new Tree(1);
const node2 = new Tree(2);
const node3 = new Tree(3);
const node4 = new Tree(4);
const node5 = new Tree(5);
const node6 = new Tree(6);
const node7 = new Tree(7);
const node8 = new Tree(8);

node1.setRoot();
node1.setLeft(node2);
node1.setRight(node3);
node2.setLeft(node4);
node2.setRight(node5);
node3.setLeft(node6);
node3.setRight(node7);

node1.postOrder(node1);
