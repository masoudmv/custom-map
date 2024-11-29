
//import org.example.MyMap;
//
import org.example.MyMap;

import java.beans.PropertyEditorManager;
import java.util.Arrays;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {

        MyMap<Integer,Integer> myMap= new MyMap<>();

        myMap.put(40, 5);
        myMap.put(4, 2);
        myMap.put(10, 9);

        MyMap<Integer,Integer> yourMap= new MyMap<>();


        yourMap.put(33,5);
        yourMap.put(324,33);
        yourMap.put(873,86);
        yourMap.put(59,5325);
        yourMap.put(844,635);
        yourMap.put(83,65);


        yourMap.put(83,99);
        yourMap.put(13,45);
        yourMap.put(23,85);
        yourMap.put(33,75);


        myMap.clear();


        MyMap.MyIteratorImp iteratorImp = (MyMap.MyIteratorImp) yourMap.iterator();
        iteratorImp.next();
        System.out.println(iteratorImp.next());
        System.out.println(iteratorImp.nextIndex());


    }
}