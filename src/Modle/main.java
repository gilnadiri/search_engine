package Modle;

import java.io.*;
import java.util.*;

public class main {
    public static void main(String[] args) throws IOException {
        // test1();
      LinkedHashMap<String,Integer> map=new LinkedHashMap<>();
      map.put("z",1);
      map.put("a",2);
      map.put("b",2);
      map.put("c",3);
      map.put("d",3);
      map.put("e",3);

      for(Map.Entry<String,Integer> entry: map.entrySet())
          System.out.println(entry.getKey());




        Searcher searcher=new Searcher("C:\\Users\\gil nadiri\\Desktop\\dest");
        searcher.Search_files_quries("C:\\Users\\gil nadiri\\Desktop\\dest\\queries.txt",false,false,new ArrayList<String>(),"C:\\Users\\gil nadiri\\Desktop\\אחזור עבודה\\corpus");

        ArrayList<Map.Entry<String,Double>> myscores=searcher.Search_single_query("blood-alcohol fatalities",false,false,new ArrayList<String>(),"C:\\Users\\gil nadiri\\Desktop\\אחזור עבודה\\corpus");
        ArrayList<String> judjescore=checkfromfile();

        relevant_i_back_from_all_the_relevant(myscores,judjescore);

    }

    private static void relevant_i_back_from_all_the_relevant(ArrayList<Map.Entry<String, Double>> myscores, ArrayList<String> judjescore) {
        ArrayList<String> my=new ArrayList<>();
        for(int i=0;i<myscores.size();i++)
            my.add(myscores.get(i).getKey());
        int ans=0;
        for(int i=0;i<my.size();i++) {
            for (int j = 0; j < judjescore.size(); j++) {
                if (my.get(i).equals(judjescore.get(j)))
                    ans = ans + 1;
            }
        }
        System.out.println("total realy relevant:"+judjescore.size()+"---->"+"relevant i returned:"+ans);
        System.out.println("number of relevant i didnt return:"+(judjescore.size()-ans));

    }

    private static ArrayList<String> checkfromfile() throws IOException {
        ArrayList<String> res=new ArrayList<>();
        BufferedReader bf= new BufferedReader(new FileReader(new File("C:\\Users\\gil nadiri\\Desktop\\אחזור עבודה" + "\\" + "1.txt")));
        for(String line; (line = bf.readLine()) != null; ) {
            if(lineisrelevant(line))
                res.add(line.split(" ")[2]);
        }
        return res;

    }

    private static boolean lineisrelevant(String line) {
        String[] s=line.split(" ");
        if(s[3].equals("1") && s[0].equals("358"))
            return true;
        else return false;
    }


    public static void test1(){
        Indexer test=new Indexer(false,"C:\\Users\\hoday\\hodaya\\שנה ג\\אחזור\\testCorpus","C:\\Users\\hoday\\hodaya\\שנה ג\\אחזור\\dest");
        test.iniateIndex();

    }
}