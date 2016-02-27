package org.kaakaa.pptmuseum.jade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaakaa on 16/02/27.
 */
public class ListHelper {
    public <T> List<List<T>> eachSlice(List<T> list, int n) {
        List<List<T>> result = new ArrayList<>();
        List<T> l = new ArrayList<>();
        for(T t: list) {
            l.add(t);
            if(l.size() == 3) {
                result.add(l);
                l = new ArrayList<>();
            }
        }
        result.add(l);
        return result;
    }
}
