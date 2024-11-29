package org.example;//package org.example;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Predicate;


public class MyMap<K,V>{

    class Node<K,V>{
        K key;
        V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node<K,V>[] nodes;
//    private K[] keys;
    private int size = 0;

    public MyMap() {
        this.nodes = new Node[100];
    }

    // TODO: Add all the methods based on the document and make sure the names are the same
    public V get(K key){
        for (int i = 0; i < size; i++) {
            if (nodes[i].key.equals(key)) return nodes[i].value;
        }
        return null;
    }

    public V getOrDefault(K key, V initValue){
        for (int i = 0; i < size; i++) {
            if (nodes[i].key.equals(key)) return nodes[i].value;
        }
        return initValue;
    }

    public V put(K key, V value){
        for (int i = 0; i < size; i++) {
            if (nodes[i].key.equals(key)){
                V oldValue = nodes[i].value;
                nodes[i].value = value;
                return oldValue;
            }
        }
        ensureCapacity();
        nodes[size] = new Node<>(key, value);
        size ++;
        return null;
    }


    private void ensureCapacity(){
        if (size == nodes.length){
            Node<K,V>[] newNodes = new Node[2 * nodes.length];
            System.arraycopy(nodes, 0, newNodes, 0, nodes.length);
            nodes = newNodes;
        }
    }

    public int indexOf(K key){
        for (int i = 0; i < size; i++) {
            if (nodes[i].key.equals(key)) return i;
        }
        return -1;
    }

    public V putIfAbsent(K key, V value){
        for (int i = 0; i < size; i++) {
            if (nodes[i].key.equals(key)){
                return nodes[i].value;
            }
        }
        ensureCapacity();
        nodes[size] = new Node<>(key, value);
        size ++;
        return value;
    }


    public <K> K[] keys(){
        K[] keys = (K[]) new Object[size];
        for (int i = 0; i < size; i++) {
            keys[i] = (K) nodes[i].key;
        }
        return keys;
    }



    public <V> V[] values(){
        V[] values = (V[]) new Object[size];
        for (int i = 0; i < size; i++) {
            values[i] = (V) nodes[i].value;
        }
        return values;
    }

    public void putAll(MyMap<? extends K,? extends V> map){
        for (int i = 0; i < map.size; i++) {
            this.put(map.nodes[i].key, map.nodes[i].value);
        }
    }

    public boolean containsKey(K key){
        for (int i = 0; i < size; i++) {
            if (nodes[i].key.equals(key)) return true;
        }
        return false;
    }

    public boolean containsValue(V value){
        for (int i = 0; i < size; i++) {
            if (nodes[i].value.equals(value)) return true;
        }
        return false;
    }

    public int size(){
        return size;
    }

    public void clear(){
        for (int i = 0; i < size; i++) {
            nodes[i] = null;
        }
        size=0;
    }

    public V remove(K key){
        for (int i = 0; i < size; i++) {
            if (nodes[i].key.equals(key)){
                V oldValue = nodes[i].value;
                System.arraycopy(nodes, i+1, nodes, i, nodes.length-i-1);
                size --;
                return oldValue;
            }
        }
        return null;
    }

    public V remove(int index){
        if (index<0 || size<=index) return null;
        V oldValue = nodes[index].value;
        System.arraycopy(nodes, index+1, nodes, index, nodes.length-index-1);
        size --;
        return oldValue;
    }

    public void removeAll(K[] keys){
        if (keys == null) return;
        for (K key:keys){
            remove(key);
        }
    }

    public void removeIfValue(Predicate<? super V> predicate){
        for (int i = 0; i < size;) {
            if (predicate.test(nodes[i].value)) {
                remove(i);
            } else {
                i++;
            }
        }
    }

    public void removeIfKey(Predicate<? super K> predicate){
        for (int i = 0; i < size;) {
            if (predicate.test(nodes[i].key)) {
                remove(i);
            } else {
                i++;
            }
        }
    }

    public void sort(Comparator<? super K> comparator) {
        boolean sorted = false;
        int lastUnsorted = size - 1; // optimization to reduce the number of passes
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < lastUnsorted; i++) {
                if (!(comparator.compare(nodes[i].key, nodes[i + 1].key) > 0)) {
                    // Swap nodes[i] and nodes[i + 1]
                    Node<K, V> temp = nodes[i];
                    nodes[i] = nodes[i + 1];
                    nodes[i + 1] = temp;
                    sorted = false;
                }
            }
            lastUnsorted--;
        }
    }

    public MyIterator<K> iterator(){

        return new MyIteratorImp();
    }

    public void replaceAll(BiFunction<? super K,?  super V, ? extends V> biFunction){
        for (int i = 0; i < size; i++) {
            nodes[i].value=  biFunction.apply(nodes[i].key, nodes[i].value);
        }

    }

    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        for (int i = 0; i < size; i++) {
            if (nodes[i].key.equals(key)) {
                V newValue = biFunction.apply(nodes[i].key, nodes[i].value);
                if (newValue == null) {
                    remove(i);
                    return null;
                } else {
                    nodes[i].value = newValue;
                    return newValue;
                }
            }
        }

        V newValue = biFunction.apply(key, null);
        if (newValue != null) {
            ensureCapacity();
            nodes[size++] = new Node<>(key, newValue);
        }
        return newValue;
    }




    public class MyIteratorImp implements MyIterator<K>{
        private int pointer;

        @Override
        public K nextByAmount(int amount) {
            for (int i = 0; i < amount-1; i++) {
                next();
            }
            return next();
        }

        @Override
        public K prevByAmount(int amount) {
            for (int i = 0; i < amount-1; i++) {
                previous();
            }
            return previous();
        }

        @Override
        public boolean hasPrevious() {
            return 0 < pointer;
        }

        @Override
        public K previous() {
            if (pointer <= 0) throw new NoSuchElementException();
            pointer -= 1;
            return nodes[pointer].key;
        }

        @Override
        public boolean hasNext() {
            return pointer < size;
        }

        @Override
        public K next() {
            if (pointer >= size) throw new NoSuchElementException();
            return nodes[pointer++].key;
        }

        @Override
        public void remove() {
            if (size == 0 || pointer < 0 || size <= pointer) return;
            System.arraycopy(nodes, pointer, nodes, pointer-1, size-pointer);
            size--;
            pointer--;
        }

        @Override
        public int previousIndex() {
            return pointer - 1;
        }

        @Override
        public int nextIndex() {
            return pointer;
        }
    }



    interface MyIterator<K> extends Iterator<K> {
        K nextByAmount(int amount);

        K prevByAmount(int amount);

        boolean hasPrevious();

        K previous();

        void remove();
        // same as Iterator remove

        int previousIndex();

        int nextIndex();
    }


}
