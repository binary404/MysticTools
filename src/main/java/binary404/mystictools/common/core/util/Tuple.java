package binary404.mystictools.common.core.util;

public class Tuple<K, V> {

    public final K key;
    public final V value;

    public Tuple(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple tuple = (Tuple) o;
        return (key == null ? tuple.key == null : key.equals(tuple.key)) && (value == null ? tuple.value == null : value.equals(tuple.value));
    }

    @Override
    public int hashCode() {
        int result = key == null ? 0 : key.hashCode();
        result = 31 * result + (value == null ? 0 : value.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

}
