package no.ntnu.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import static no.ntnu.utils.Col.newMap;


public class StringCount implements Comparable<StringCount> {
    public final String str;
    private int count;
    public final Map<String, String> props = newMap();

    public StringCount(String str) {
        this.str = str;
    }

    public void increment() {
        ++count;
    }

    public int compareTo(StringCount o) {
        int x = this.count, y = o.count;
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    @Override
    public String toString() {
        return "" + str + "=" + count;
    }

    public static void filterCount(Collection<StringCount> col, final int n, Filter... additionalFilters) {
        for (Iterator<StringCount> i = col.iterator(); i.hasNext(); ) {
            StringCount next = i.next();
            boolean remove = false;
            for (Filter f : additionalFilters) {
                //D.d("f.filter(next):"+f.filter(next)+", next:"+next);
                if (f.filter(next)) {
                    remove = true;
                    break;
                }
            }
            //D.d("next:%s, remove=%s",next,remove);
            if (remove || next.count <= n) i.remove();
        }
    }

    public int getCount() {
        return count;
    }

    public void add(int k) {
        count += k;
    }

}
