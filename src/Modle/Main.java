package Modle;

import com.sun.javafx.collections.MappingChange;

import java.io.*;
import java.sql.Time;
import java.text.ParseException;
import java.util.*;

public class Main {

   public static void main(String[] args) throws ParseException, IOException, ClassNotFoundException {




        //test1();
        //  test2();
        //test3();
        //test4();
        //test5();
        test6();  //<F P=105>
        //  test7();
       //  String s="advantaged: FBIS4-42692 +1, FBIS4-44418 +1, FBIS4-44474 +1,";
       // String[]s1=s.split(",");
       // System.out.println(s1.length);
      // test8();
      // test9();
      // test10();





    }

    private static void test10() throws IOException, ClassNotFoundException {
        FileInputStream fis;


                fis = new FileInputStream("C:\\Users\\gil nadiri\\Desktop\\dest\\cities");

            ObjectInputStream ois = new ObjectInputStream(fis);
            HashMap<String,City> cities=(HashMap) ois.readObject();
            ois.close();
            fis.close();
            for(Map.Entry<String,City> entry: cities.entrySet())
                for(Map.Entry<String,String> entry2 : entry.getValue().getLocation().entrySet())
                    System.out.println(entry2.getKey());


    }

    private static void test9() throws IOException {
        Indexer indexer=new Indexer(false,"C:\\Users\\gil nadiri\\Desktop\\אחזור עבודה 1\\corpus","C:\\Users\\gil nadiri\\Desktop\\dest");
        ReadFile corpus = new ReadFile("C:\\Users\\gil nadiri\\Desktop\\אחזור עבודה 1\\corpus");
        HashSet<String> langueges=new HashSet<>();
        indexer.cities_index=corpus.readAllCities("C:\\Users\\gil nadiri\\Desktop\\אחזור עבודה 1\\corpus",langueges);
        int i=0;

    }


    private static void test6() throws IOException {

        Indexer indexer = new Indexer(false, "C:\\Users\\gil nadiri\\Desktop\\אחזור עבודה 1\\corpus", "C:\\Users\\gil nadiri\\Desktop\\dest");
        long startTime = System.nanoTime();
        indexer.iniateIndex();
        long finishTime 	= System.nanoTime();
        System.out.println("Time for all:  " + (finishTime - startTime)/1000000.0 + " ms");

//        Map<String,Documentt> map=indexer.documents;
//        File f = new File("C:\\Users\\gil nadiri\\Desktop\\קבצים עדכניים" + "\\" + "hara");
//        BufferedWriter hara = new BufferedWriter(new FileWriter(f));
//        for(Map.Entry<String,Documentt> entry : map.entrySet() )
//            hara.write(entry.getValue().Doc_Name+"\n");





        }




    private static void test5() throws IOException {
    //    Indexer indexer=new Indexer(false);
     //   indexer.setNUM(46);
     //   indexer.merge_All_tmp_postings_files();
     //   int freq=indexer.dictionary.get("BUZZACOTT").getTotal_frequncy_in_the_corpus();
      //  System.out.println(freq);
    }

    private static void test4() throws IOException {
      //  Indexer indexer=new Indexer(false);
        //indexer.iniateIndex("C:\\Users\\gil nadiri\\Desktop\\אחזור עבודה 1\\corpus");

        //indexer.setNUM(46);
        //long startTime = System.nanoTime();
       // indexer.iniateIndex("C:\\Users\\gil nadiri\\Desktop\\אחזור עבודה 1\\files");
        //indexer.merge_All_tmp_postings_files();
        //long finishTime 	= System.nanoTime();

        //System.out.println("Time for all:  " + (finishTime - startTime)/1000000.0 + " ms");
        //System.out.println("*************************");
       // for (Map.Entry <String, Documentt> entry: indexer.documents.entrySet()) {
         //   System.out.println(entry.getValue().getDoc_Name()+"-> "+entry.getValue().getDoc_City());
        }
        //indexer.documents.get("FBIS3-83").getDoc_Name() ;
   // }





}
