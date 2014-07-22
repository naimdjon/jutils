package no.ntnu.utils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.Collections.addAll;
import static java.util.Collections.reverseOrder;

@SuppressWarnings("unused")
public class Col {

    public static <K> Set<K> intersection(Set<K> s1, Set<K> s2) {
        Set<K> retval = newSet();
        retval.addAll(s1);
        boolean res = retval.retainAll(s2);
        return retval;
    }

    public static <K, V> Map<K, V> newMap() {
        return new HashMap<K, V>();
    }

    public static <K, V> LinkedHashMap<K, V> linkedHashMap() {
        return linkedHashMap(false);
    }

    public static <K, V> LinkedHashMap<K, V> linkedHashMap(boolean synchronised) {
        LinkedHashMap<K, V> linkedHashMap = new LinkedHashMap<K, V>();
        if (synchronised) return (LinkedHashMap<K, V>) Collections.synchronizedMap(linkedHashMap);
        return linkedHashMap;
    }

    public static <K, V> Map<K, V> newMap(Map<K, V> source) {
        return new HashMap<K, V>(source);
    }

    public static <K, V> Map<K, V> concurrentMap(Map<K, V> source) {
        return new ConcurrentHashMap<K, V>(source);
    }

    public static <K, V> TreeMap<K, V> treeMap() {
        return new TreeMap<K, V>();
    }


    public static <T extends Comparable<? super T>> List<T> sortt(List<T> l) {
        if (l == null) return l;
        java.util.Collections.sort(l);
        return l;
    }

    public static <K, V> TreeMap<K, V> sortedMap() {
        return treeMap();
    }


    public static <K, V> HashMap<K, V> newHashMap(int initialcapacity) {
        return new HashMap<K, V>(initialcapacity);
    }


    public static <K, V> Hashtable<K, V> newHashtable() {
        return new Hashtable<K, V>();
    }

    public static <E> Set<E> newHashSet(Collection<? extends E> c) {
        return new HashSet<E>(c);
    }

    public static <E> Set<E> newHashSet() {
        return new HashSet<E>();
    }

    public static <E> Set<E> newSet() {
        return new HashSet<E>();
    }

    public static <E> Set<E> newSet(Collection<E> col, E... elems) {
        HashSet<E> s = new HashSet<E>(col);
        if (elems == null)
            return s;
        addAll(s, elems);
        return s;
    }

    public static <E> Set<E> newSet(E... elems) {
        HashSet<E> hashSet = new HashSet<E>();
        if (elems == null)
            return hashSet;
        addAll(hashSet, elems);
        return hashSet;
    }

    public static <E> SortedSet<E> newSortedSet() {
        return sortedSet();
    }

    public static <E> SortedSet<E> sortedSet() {
        return new TreeSet<E>();
    }

    public static <E> Set<E> newSet(int initialCapacity) {
        return new HashSet<E>(initialCapacity);
    }

    public static <E> List<E> newList() {
        return new ArrayList<E>();
    }

    public static <E> List<E> newList(int initialCapacity) {
        return new ArrayList<E>(initialCapacity);
    }


    public static <E> LinkedList<E> linkedList() {
        return new LinkedList<E>();
    }

    public static <E> Queue<E> newQueue() {
        return new LinkedList<E>();
    }

    public static <E> LinkedBlockingQueue<E> linkedBlockingQueue() {
        return new LinkedBlockingQueue<E>();
    }


    public static <E> Vector<E> newVector() {
        return new Vector<E>();
    }


    public static <K, V> Map<K, V> synchronizedMap() {
        return java.util.Collections.synchronizedMap(new HashMap<K, V>());
    }


    public static <E> boolean empty(Collection<E> col) {
        return col == null || col.isEmpty();
    }

    public static <K, V> boolean empty(Map<K, V> col) {
        return col == null || col.isEmpty();
    }


    public static <E> Stack<E> newStack() {
        return new Stack<E>();
    }


    public static <E> Collection<E> toCollection(E... objs) {
        return new HashSet<E>(Arrays.asList(objs));
    }

    public static <E> Collection<E> toCol(E... objs) {
        return new HashSet<E>(Arrays.asList(objs));
    }

    public static <E> List<E> toList(E... objs) {
        return new ArrayList<E>(Arrays.asList(objs));
    }


    public static <E> Collection<E> getValues(Map<?, E> map, Object... objs) {
        final HashSet<E> retval = new HashSet<E>();
        for (Object o : objs)
            retval.add(map.get(o));
        return retval;
    }


    public static <K, V> String prettyPrint(final Map<K, V> map) {
        StringBuffer s = new StringBuffer();
        for (Map.Entry<K, V> entry : map.entrySet())
            s.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        return s.toString();
    }

    public static <E> List<E> newList(Collection<E>... elements) {
        final List<E> l = newList();
        for (Collection<E> ee : elements)
            l.addAll(ee);
        return l;
    }

    public static <E> Collection<E> newSet(Collection<E> e) {
        return new HashSet<E>(e);
    }

    public static <T> List<T> asReverseSortedList(Collection<T> c) {
        List<T> list = new ArrayList<T>(c);
        java.util.Collections.sort(list, reverseOrder());
        return list;
    }

    public static <T> List<T> asReverseSortedList(Collection<T> c, Comparator<T> comp) {
        List<T> list = new ArrayList<T>(c);
        java.util.Collections.sort(list, reverseOrder(comp));
        return list;
    }

    public static <T> List<T> asSortedList(Collection<T> c, Comparator<T> comp) {
        List<T> list = new ArrayList<T>(c);
        java.util.Collections.sort(list, comp);
        return list;
    }

    public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
        List<T> list = new ArrayList<T>(c);
        java.util.Collections.sort(list);
        return list;
    }

    public static <E> void p(Collection<E> c) {
        print(c);
    }


    public static <E> void print(Collection<E> c) {
        for (E e : c)
            System.out.println(e);
    }


    public static <E> void print(Collection<E> c, final int topK) {
        int i = 0;
        for (E e : c) {
            if (++i > topK) break;
            System.out.println(e);
        }
    }

    public static <E> void print(List<E> c, final int topK) {
        for (int i = 0; i < c.size() && i <= topK; i++)
            System.out.println(c.get(i));
    }

    public static Map<String, String> toMapStr(String... keyValues) {
        Map<String, String> retval = newMap();
        for (String keyValue : keyValues) {
            String[] kv = keyValue.split("=|;");
            if (kv.length != 2) {
                System.out.println("The splitted keyvalue length was not 2: " + kv.length);
                continue;
            }
            retval.put(kv[0], kv[1]);
        }
        return retval;
    }

    public static <E> void filter(Collection<E> col, Collection<E> filter) {
        for (Iterator<E> it = col.iterator(); it.hasNext(); ) {
            E next = it.next();
            if (filter.contains(next)) it.remove();
        }
    }



    public static Map toMap(final Object[] array) {
        if (array == null) {
            return null;
        }
        final Map map = new HashMap((int) (array.length * 1.5));
        for (int i = 0; i < array.length; i++) {
            Object object = array[i];
            if (object instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) object;
                map.put(entry.getKey(), entry.getValue());
            } else if (object instanceof Object[]) {
                Object[] entry = (Object[]) object;
                if (entry.length < 2) {
                    throw new IllegalArgumentException("Array element " + i + ", '"
                            + object
                            + "', has a length less than 2");
                }
                map.put(entry[0], entry[1]);
            } else {
                throw new IllegalArgumentException("Array element " + i + ", '"
                        + object
                        + "', is neither of type Map.Entry nor an Array");
            }
        }
        return map;
    }

    public static Object[] subarray(Object[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        int newSize = endIndexExclusive - startIndexInclusive;
        Class type = array.getClass().getComponentType();
        if (newSize <= 0) {
            return (Object[]) Array.newInstance(type, 0);
        }
        Object[] subarray = (Object[]) Array.newInstance(type, newSize);
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

    public static char[] subarray(char[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return new char[0];
        }

        char[] subarray = new char[newSize];
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

}