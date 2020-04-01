var Tree = /** @class */ (function () {
    function Tree(theData) {
        this.data = theData;
        this.left = null;
        this.right = null;
        this.root = false;
    }
    Tree.prototype.setRoot = function () {
        this.root = true;
    };
    Tree.prototype.setLeft = function (node) {
        this.left = node;
    };
    Tree.prototype.setRight = function (node) {
        this.right = node;
    };
    Tree.prototype.preOrder = function (node) {
        if (node) {
            console.log(node.data);
            this.preOrder(node.left);
            this.preOrder(node.right);
        }
    };
    Tree.prototype.inOrder = function (node) {
        if (node) {
            this.inOrder(node.left);
            console.log(node.data);
            this.inOrder(node.right);
        }
    };
    Tree.prototype.postOrder = function (node) {
        if (node) {
            this.postOrder(node.left);
            this.postOrder(node.right);
            console.log(node.data);
        }
    };
    return Tree;
}());
var node1 = new Tree(1);
var node2 = new Tree(2);
var node3 = new Tree(3);
var node4 = new Tree(4);
var node5 = new Tree(5);
var node6 = new Tree(6);
var node7 = new Tree(7);
var node8 = new Tree(8);
node1.setRoot();
node1.setLeft(node2);
node1.setRight(node3);
node2.setLeft(node4);
node2.setRight(node5);
node3.setLeft(node6);
node3.setRight(node7);
node1.postOrder(node1);
