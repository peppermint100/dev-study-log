package DataStructure.heap_sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class solution {
    public static void main(String[] args) {
        List<Integer> heap_tree = new ArrayList<Integer>(Arrays.asList(new Integer[] { 5, 7, 1, 2, 6, 10 }));
        List<Integer> sorted = heap_sort(heap_tree);
        System.out.println(sorted);
    }

    public static List<Integer> heap_sort(List<Integer> tree) {
        List<Integer> sorted = new ArrayList<Integer>();
        int len = tree.size();
        while (len > 0) {
            build_heap(tree);
            swap(tree, 0, len - 1);
            sorted.add(0, tree.remove(len - 1));
            len--;
        }
        return sorted;
    }

    public static void build_heap(List<Integer> tree) {
        double mid = Math.floor(tree.size() / 2);
        while (mid >= 0) {
            System.out.println(mid);
            max_heapify(tree, (int) mid);
            mid--;
        }
    }

    public static void max_heapify(List<Integer> tree, int idx) {
        Integer node = idx;
        Integer left = idx * 2;
        Integer right = idx * 2 + 1;
        Integer len = tree.size() - 1;
        if (left > len || right > len) {
            return;
        } else {
            Integer bigger = tree.get(left) > tree.get(right) ? left : right;
            if (tree.get(bigger) < tree.get(node)) {
                return;
            }
            swap(tree, node, bigger);
            max_heapify(tree, bigger);
        }
    }

    public static void swap(List<Integer> array, Integer a, Integer b) {
        Integer temp = array.get(a);
        array.set(a, array.get(b));
        array.set(b, temp);
    }

}
