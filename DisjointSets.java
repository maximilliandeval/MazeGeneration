import java.util.*;

/*
Disjoint-Set data structure
Implemented with weighted-union-by-rank and path compression
*/

public class DisjointSets<T> {
    // Map items to parents
    private HashMap<T, T> parents = new HashMap<T, T>();
    // Map items to rank
    private HashMap<T, Integer> ranks = new HashMap<T, Integer>();
    // Tracks number of sets
    private int count;

    public DisjointSets() {
        super();
    }

    public int numElements() {
        return parents.size();
    }

    public int numSets() {
        return count;
    }

    // Add a new item in a disjoint singleton set
    public void insert(T item) {
        parents.put(item, item);
        ranks.put(item, 0);
        count++;
    }

    // Identify the set to which an element belongs using recursion
    public T find(T item) {
        T parent = parents.get(item);
        if (parent == null) return null;
        if (parent.equals(item)) return item;
        // Reattach all visited elements to the root item (path compression)
        parent = find(parent);
        parents.put(item, parent);
        return parent;
    }

    // Given two elements, union the sets of which they belong to
    public boolean union(T item1, T item2) {
        T parent1 = find(item1);
        T parent2 = find(item2);

        if (parent1 == null) {
            insert(item1);
            parent1 = item1;
        }
        if (parent2 == null) {
            insert(item2);
            parent2 = item2;
        }
        // Return false if they are already part of the same set
        if (parent1.equals(parent2)) return false;

        int rank1 = ranks.get(parent1);
        int rank2 = ranks.get(parent2);
        // Always attach the shorter tree to the root of the taller tree
        if (rank1 < rank2) {
            parents.put(parent1, parent2);
        } else {
            parents.put(parent2, parent1);
            // Update rank
            if (rank1 == rank2)
                ranks.put(parent1, rank1+1);
        }
        count--;
        return true;
    }

}
