package Modle;

import java.io.RandomAccessFile;
import java.util.*;

public class main {
    public static void main(String[] args) {
       // test1();
      Map<String,TokenInfo> map=new HashMap<>();
      map.put("A",new TokenInfo(10,""));
      map.put("HODAYA",new TokenInfo(9,""));
      map.put("B",new TokenInfo(20,""));
      map.put("GIL",new TokenInfo(6,""));
      map.put("C",new TokenInfo(100,""));
      map.put("E",new TokenInfo(2,""));
      map.put("p",new TokenInfo(100,""));
      map.put("T",new TokenInfo(50,""));
      ArrayList<String> list=top_five_yeshooyot(map);
      int i=0;



    }


        public static ArrayList<String> top_five_yeshooyot(Map<String, TokenInfo> terms_after_parser) {
            HashMap<String,Integer> yeshooyot=new HashMap<>();
            for(Map.Entry<String,TokenInfo>  entry: terms_after_parser.entrySet())
                if(Character.isUpperCase(entry.getKey().charAt(0)))
                    yeshooyot.put(entry.getKey(),entry.getValue().frequentInDoc);

            List<Map.Entry<String, Integer>> list = new LinkedList<>(yeshooyot.entrySet());
            list.sort(new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                   return o2.getValue().compareTo(o1.getValue());
                }
            });
            ArrayList<String> top_5=new ArrayList<>();
            for(int i=0;i<5;i++)
                top_5.add(list.get(i).getKey());
            return top_5;
        }



    public static void test1(){
        Indexer test=new Indexer(false,"C:\\Users\\hoday\\hodaya\\שנה ג\\אחזור\\testCorpus","C:\\Users\\hoday\\hodaya\\שנה ג\\אחזור\\dest");
        test.iniateIndex();

    }
}
