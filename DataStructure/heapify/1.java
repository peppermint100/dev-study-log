package Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class solution {
    public static void main(String[] args) {
        List<Integer> heap_tree = new ArrayList<Integer>(
                Arrays.asList(new Integer[] { 0, 21, 8, 15, 12, 11, 9, 6, 5, 2 }));

        System.out.println(heap_tree);
        max_heapify(heap_tree, 2);
        System.out.println(heap_tree);
    }

    public static void max_heapify(List<Integer> tree, int idx) {
        // if data has no children return
        if (idx * 2 > tree.size()) {
            return;
        }
        // if data is in right place return
        else if (tree.get(idx) >= tree.get(idx * 2) && tree.get(idx) >= tree.get(idx * 2 + 1)) {
            return;
        }
        // switch seats with bigger child
        else {
            Integer data = tree.get(idx);
            Integer leftChild = tree.get(idx * 2);
            Integer rightChild = tree.get(idx * 2 + 1);

            int biggerIdx = leftChild > rightChild ? tree.indexOf(leftChild) : tree.indexOf(rightChild);

            tree.set(idx, tree.get(biggerIdx));
            tree.set(biggerIdx, data);
            System.out.println(biggerIdx);
            // recursive
            max_heapify(tree, biggerIdx);
        }

    }
}
