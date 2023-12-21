package collections.myHashMap;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MySimpleHashMap<K, V> implements MySimpleMap<K, V> {
    public static void main(String[] args) {
        MySimpleMap<String, String> strings = new MySimpleHashMap<>();
        strings.insert("1", "odin");
        strings.insert("2", "dva");
        strings.insert("3", "tri");
        strings.insert("4", "chetyre");
        strings.delete("3");
        System.out.println(strings.get("2"));
        for (String s : strings) {
            System.out.println(s);
        }
    }
    private Node<K, V>[] hashTable;
    private int size = 0;
    private float threshold;

    public MySimpleHashMap() {
        hashTable = new Node[16];
        threshold = hashTable.length * 0.75f;
    }

    @Override
    public boolean insert(final K key, final V value) {
        if (size + 1 >= threshold) {
            threshold *= 2;
            arrayDoubling();
        }
        Node<K, V> newNode = new Node<>(key, value);
        int index = hash(key);
        if (hashTable[index] == null) {
            return simpleAdd(index, newNode);
        }
        List<Node<K, V>> nodeList = hashTable[index].getNodes();
        for (Node<K, V> node : nodeList) {
            if (keyExist(node, newNode, value) ||
                    collisionProcessing(node, newNode, nodeList)) {
                return true;
            }
        }
        return false;
    }


    private boolean simpleAdd(int index, Node<K, V> newNode) {
        hashTable[index] = new Node<>(null, null);
        hashTable[index].getNodes().add(newNode);
        size++;
        return true;
    }

    private boolean keyExist(
            final Node<K, V> node,
            final Node<K, V> newNode,
            final V value) {

        if (newNode.getKey().equals(node.getKey())) {
            node.setValue(value);
            return true;
        }
        return false;
    }

    private boolean collisionProcessing(
            final Node<K, V> node,
            final Node<K, V> newNode,
            final List<Node<K, V>> nodeList) {
        if (newNode.hashCode() == node.hashCode()
                && !Objects.equals(newNode.getKey(), node.getKey())) {
            nodeList.add(newNode);
            size++;
            return true;
        }
        return false;
    }

    private void arrayDoubling() {
        Node<K, V>[] oldaHashTable = hashTable;
        hashTable = new Node[oldaHashTable.length * 2];
        size = 0;
        for (Node<K, V> node : hashTable) {
            if (node != null) {
                for (Node<K, V> n : node.getNodes()) {
                    insert(n.key, n.value);
                }
            }
        }
    }


    @Override
    public boolean delete(K key) {
        int index = hash(key);
        if (hashTable[index] == null)
            return false;

        if (hashTable[index].getNodes().size() == 1) {
            hashTable[index] = null;
            return true;
        }
        List<Node<K, V>> nodeList = hashTable[index].getNodes();
        for (Node<K, V> node : nodeList) {
            if (key.equals(node.getKey())) {
                nodeList.remove(node);
                return true;
            }
        }
        return false;
    }

    private int hash(final K key) {
        int hash = 31;
        hash = hash * 17 + key.hashCode();
        return hash % hashTable.length;
    }

    @Override
    public V get(final K key) {
        int index = hash(key);
        if (index < hashTable.length &&
                hashTable[index] != null) {
            if (hashTable[index].getNodes().size() == 1) {
                return hashTable[index].getNodes().get(0).getValue();
            }
            List<Node<K, V>> list = hashTable[index].getNodes();
            for (Node<K, V> node : list) {
                if (key.equals(node.getKey())) {
                    return node.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    public Iterator<V> iterator() {
        return new Iterator<V>() {
            int counterArray = -1;
            int valuesCounter = 0;
            Iterator<Node<K, V>> subIterator = null;

            @Override
            public boolean hasNext() {
                if (valuesCounter == size)
                    return false;
                if (subIterator == null || !subIterator.hasNext()) {
                    if (moveToNextCell()) {
                        subIterator = hashTable[counterArray].getNodes().iterator();
                    } else {
                        return false;
                    }
                }
                return subIterator.hasNext();
            }

            private boolean moveToNextCell() {
                counterArray++;
                while (counterArray < hashTable.length && hashTable[counterArray] == null) {
                    counterArray++;
                }
                return counterArray < hashTable.length && hashTable[counterArray] != null;
            }

            @Override
            public V next() {
                valuesCounter++;
                return subIterator.next().getValue();
            }
        };
    }

    private class Node<K, V> {
        private List<Node<K, V>> nodes;
        private int hash;
        private K key;
        private V value;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            nodes = new LinkedList<>();
        }

        private List<Node<K, V>> getNodes() {
            return nodes;
        }

        private int hash() {
            return hashCode() % hashTable.length;
        }

        private K getKey() {
            return key;
        }

        private V getValue() {
            return value;
        }

        private void setValue(V value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (o == null || getClass() != o.getClass()) return false;
            Node<?, ?> node = (Node<?, ?>) o;
            return hash ==
                    node.hashCode()
                    && Objects.equals(nodes, node.nodes)
                    && Objects.equals(key, node.getKey())
                    && Objects.equals(value, node.getValue());
        }

        @Override
        public int hashCode() {
            hash = 31;
            hash = hash * 17 + key.hashCode();
            return hash;
        }
    }
}
