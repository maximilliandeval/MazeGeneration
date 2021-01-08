import java.util.*;

public class DisjointSets<T> {

    private HashMap<T, T> parents = new HashMap<T, T>();
    private HashMap<T, Integer> ranks = new HashMap<T, Integer>();
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

    public void insert(T item) {
        parents.put(item, item);
        ranks.put(item, 0);
        count++;
    }

    public T find(T item) {
        T parent = parents.get(item);
        if (parent == null) return null;
        if (parent.equals(item)) return item;

        parent = find(parent);
        parents.put(item, parent);
        return parent;
    }

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

        if (parent1.equals(parent2)) return false;

        int rank1 = ranks.get(parent1);
        int rank2 = ranks.get(parent2);
        if (rank1 < rank2) {
            parents.put(parent1, parent2);
        }
        else {
            parents.put(parent2, parent1);
            if (rank1 == rank2)
                ranks.put(parent1, rank1+1);
        }
        count--;
        return true;
    }

}