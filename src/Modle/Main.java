//package Modle;
//
//import java.io.*;
//import java.sql.Time;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class Main {
////////////////////
//    public static void main(String[] args) throws ParseException, IOException, ClassNotFoundException {
//
//
//
//        //String line="ksnjgvkl <F P=104>  Johannesburg SABC TV 1 Network </F>";
//        //String[]s=line.split("<F y=104>");
//
//        //test1();
//        //  test2();
//        //test3();
//        //test4();
//        //test5();
//        test6();
//        //  test7();
//       //  String s="advantaged: FBIS4-42692 +1, FBIS4-44418 +1, FBIS4-44474 +1,";
//       // String[]s1=s.split(",");
//       // System.out.println(s1.length);
//      // test8();
//
//
//
//
//
//    }
//
//    private static void test8() throws IOException, ClassNotFoundException {
//        Map<String,String> map=new HashMap<>();
//        map.put("gil","GIL");
//        map.put("hodaya","HODAYA");
//        map.put("b","B");
//        FileOutputStream fos =new FileOutputStream("resources"+ "\\" +"map" );
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//        oos.writeObject(map);
//        oos.close();
//        fos.close();
//
//        HashMap<String, String> map2 = null;
//        FileInputStream fis = new FileInputStream("resources"+ "\\" +"map");
//        ObjectInputStream ois = new ObjectInputStream(fis);
//        map2 = (HashMap) ois.readObject();
//        ois.close();
//        fis.close();
//
//        for(Map.Entry<String,String> entry: map2.entrySet())
//            System.out.println(entry.getKey()+"---->"+entry.getValue());
//    }
//
//   private static void test7() throws IOException {
//        File f = new File("resources" + "\\" + "gillllllllllllllllllllllllllllllll");
//        BufferedWriter final_posting = new BufferedWriter(new FileWriter(f));
//        String s1="''a: FBIS4-44378 +3,FBYS3-4444 +3,";
//        String s2="''b: FBIS4-44378 +3,";
//        String s3="''c: D2 +1,D3+7,";
//        String s4="''d: FBIS4-44378 +3,FBYS3-4444 +3,";
//        String s5="''e: FBIS4-44378 +3,FBYS3-4444 +3,";
//        String s6="''f: FBIS4-44378 +3,FBYS3-4444 +3,";
//        String s7="''g: FBIS4-44378 +3,FBYS3-4444 +3,";
//        String s8="''h: FBIS4-44378 +3,FBYS3-4444 +3,";
//        String s9="''i: FBIS4-44378 +3,FBYS3-4444 +3,";
//        String s10="''j'': FBIS4-44378 +3,FBYS3-4444 +3,";
//
//
//        String[] strings=new String[10];
//        strings[0]=s1;
//        strings[1]=s2;
//        strings[2]=s3;
//        strings[3]=s4;
//        strings[4]=s5;
//        strings[5]=s6;
//        strings[6]=s7;
//        strings[7]=s8;
//        strings[8]=s9;
//        strings[9]=s10;
//
//
//        final_posting.write(s1+ "\n");
//        final_posting.write(s2+ "\n");
//        final_posting.write(s3+ "\n");
//        final_posting.write(s4+ "\n");
//        final_posting.write(s5+ "\n");
//        final_posting.write(s6+ "\n");
//        final_posting.write(s7+ "\n");
//        final_posting.write(s8+ "\n");
//        final_posting.write(s9+ "\n");
//        final_posting.write(s10+ "\n");
//        final_posting.flush();
//        final_posting.close();
//
//        long pointer1=0;
//        long pointer2=pointer1+s1.length()+1;
//        long pointer3=pointer2+s2.length()+1;
//        long pointer4=pointer3+s3.length()+1;
//        long pointer5=pointer4+s4.length()+1;
//        long pointer6=pointer5+s5.length()+1;
//        long pointer7=pointer6+s6.length()+1;
//        long pointer8=pointer7+s7.length()+1;
//        long pointer9=pointer8+s8.length()+1;
//        long pointer10=pointer9+s9.length()+1;
//
//
//        RandomAccessFile raf = new RandomAccessFile("resources" + "\\" + "gillllllllllllllllllllllllllllllll","rw");
//        raf.seek(pointer1);
//        System.out.println("a----->"+raf.readLine());
//
//        raf.seek(pointer2);
//        System.out.println("b----->"+raf.readLine());
//
//        raf.seek(pointer3);
//        System.out.println("c----->"+raf.readLine());
//
//        raf.seek(pointer4);
//        System.out.println("d----->"+raf.readLine());
//
//        raf.seek(pointer5);
//        System.out.println("e----->"+raf.readLine());
//
//        raf.seek(pointer6);
//        System.out.println("f----->"+raf.readLine());
//
//        raf.seek(pointer7);
//        System.out.println("g----->"+raf.readLine());
//
//        raf.seek(pointer8);
//        System.out.println("h----->"+raf.readLine());
//
//        raf.seek(pointer9);
//        System.out.println("i----->"+raf.readLine());
//
//        raf.seek(pointer10);
//        System.out.println("j----->"+raf.readLine());
//
//
//
//    }
//
//
//
//    private static void test6() throws IOException {
//        Indexer indexer=new Indexer(false,"C:\\Users\\gil nadiri\\Desktop\\אחזור עבודה 1\\corpus","resources");
//        indexer.setNUM(46);
//        indexer.merge_All_tmp_postings_files();
//        long startTime = System.nanoTime();
//        List<Term> list=indexer.showDic();
//        long finishTime 	= System.nanoTime();
//        System.out.println("Time for sorting:  " + (finishTime - startTime)/1000000.0 + " ms");
//        String dicToShow="";
//        for(Term t:list)
//            dicToShow+=t.getTerm()+"---->"+t.getDf()+"\n";
//        System.out.println(dicToShow);
///*
//        BufferedReader finall = new BufferedReader(new FileReader(new File("resources" + "\\" + "final")));
//        long pointer=0;
//        for(int i=1;i<=146787;i++)
//        {
//            String line=finall.readLine();
//            pointer=pointer+line.length()+1;
//        }
//        System.out.println(pointer);
//
//
//
//        RandomAccessFile raf = new RandomAccessFile("resources" + "\\" + "final","rw");
//        //long pointer=indexer.dictionary.get("$1-8-million").getPointerToPostings();
//        raf.seek(pointer);
//        System.out.println("$1-8-million--------->"+raf.readLine());
//
//         pointer=indexer.dictionary.get("$108.58-billion").getPointerToPostings();
//        raf.seek(pointer);
//        System.out.println("$108.58-billion--------->"+raf.readLine());
//
//
//        pointer=indexer.dictionary.get("$120-a-week").getPointerToPostings();
//        raf.seek(pointer);
//        System.out.println("$120-a-week--------->"+raf.readLine());
//
//
//
//
//        pointer=indexer.dictionary.get("'co-exists").getPointerToPostings();
//        raf.seek(pointer);
//        System.out.println("'co-exists--------->"+raf.readLine());
//
//
//        pointer=indexer.dictionary.get("'Coalbed").getPointerToPostings();
//        raf.seek(pointer);
//        System.out.println("'Coalbed--------->"+raf.readLine());
//
//
//        pointer=indexer.dictionary.get("'quasi").getPointerToPostings();
//        raf.seek(pointer);
//        System.out.println("'quasi--------->"+raf.readLine());
//
//        pointer=indexer.dictionary.get("2.24919 K").getPointerToPostings();
//        raf.seek(pointer);
//        System.out.println("2.24919 K:--------->"+raf.readLine());
//
//
//*/
//    }
//
//    private static void test5() throws IOException {
//    //    Indexer indexer=new Indexer(false);
//     //   indexer.setNUM(46);
//     //   indexer.merge_All_tmp_postings_files();
//     //   int freq=indexer.dictionary.get("BUZZACOTT").getTotal_frequncy_in_the_corpus();
//      //  System.out.println(freq);
//    }
//
//    private static void test4() throws IOException {
//      //  Indexer indexer=new Indexer(false);
//        //indexer.iniateIndex("C:\\Users\\gil nadiri\\Desktop\\אחזור עבודה 1\\corpus");
//
//        //indexer.setNUM(46);
//        //long startTime = System.nanoTime();
//       // indexer.iniateIndex("C:\\Users\\gil nadiri\\Desktop\\אחזור עבודה 1\\files");
//        //indexer.merge_All_tmp_postings_files();
//        //long finishTime 	= System.nanoTime();
//
//        //System.out.println("Time for all:  " + (finishTime - startTime)/1000000.0 + " ms");
//        //System.out.println("*************************");
//       // for (Map.Entry <String, Documentt> entry: indexer.documents.entrySet()) {
//         //   System.out.println(entry.getValue().getDoc_Name()+"-> "+entry.getValue().getDoc_City());
//        }
//        //indexer.documents.get("FBIS3-83").getDoc_Name() ;
//   // }
//
//    private static void test3() throws IOException {
//     //   Indexer indexer=new Indexer(false);
//      //  indexer.merge_two_files(1,2);
//    }
//
//    private static void test2() throws IOException, ParseException {
//     //   Indexer indexer=new Indexer(false);
//        long startTime = System.nanoTime();
//       // indexer.iniateIndex("C:\\Users\\gil nadiri\\Desktop\\אחזור עבודה 1\\corpus");
//        long finishTime 	= System.nanoTime();
//        System.out.println("Time for merge:  " + (finishTime - startTime)/1000000.0 + " ms");
//
//    }
//
//
//    private static void test1() throws IOException, ParseException {
//        HashMap<Documentt,String> docs1=new HashMap<>();
//        HashMap<Documentt,String> docs2=new HashMap<>();
//        HashMap<Documentt,String> docs3=new HashMap<>();
//        HashMap<Documentt,String> docs4=new HashMap<>();
//
//        Documentt doc_1=new Documentt("DOC_1",0,0,"",0);
//        String str1="t";
//
//        Documentt doc_2=new Documentt("DOC_2",0,0,"",0);
//        String str2="a";
//
//        Documentt doc_3=new Documentt("DOC_3",0,0,"",0);
//        String str3="GIL BANANA";
//
//        Documentt doc_4=new Documentt("DOC_4",0,0,"",0);
//        String str4="b";
//
//        Documentt doc_5=new Documentt("DOC_5",0,0,"",0);
//        String str5="gil BANANA";
//
//
//        docs1.put(doc_1,str1);
//        docs1.put(doc_2,str2);
//        docs1.put(doc_3,str3);
//        docs1.put(doc_4,str4);
//        docs1.put(doc_5,str5);
//
//
//
//        Documentt doc_6=new Documentt("DOC_6",0,0,"",0);
//        String str6="Apple";
//
//        Documentt doc_7=new Documentt("DOC_7",0,0,"",0);
//        String str7="apple hola al hodaya";
//
//        Documentt doc_8=new Documentt("DOC_8",0,0,"",0);
//        String str8="love basketball";
//
//        Documentt doc_9=new Documentt("DOC_9",0,0,"",0);
//        String str9="hey Computer";
//
//        Documentt doc_10=new Documentt("DOC_10",0,0,"",0);
//        String str10="1990 apple";
//
//
//        docs2.put(doc_6,str6);
//        docs2.put(doc_7,str7);
//        docs2.put(doc_8,str8);
//        docs2.put(doc_9,str9);
//        docs2.put(doc_10,str10);
//
//
//
//        Documentt doc_11=new Documentt("DOC_11",0,0,"",0);
//        String str11="bananza";
//
//        Documentt doc_12=new Documentt("DOC_12",0,0,"",0);
//        String str12="gil and hodaya";
//
//
//        Documentt doc_13=new Documentt("DOC_13",0,0,"",0);
//        String str13="love table";
//
//        Documentt doc_14=new Documentt("DOC_14",0,0,"",0);
//        String str14="book computer";
//
//        Documentt doc_15=new Documentt("DOC_15",0,0,"",0);
//        String str15="1993 mother";
//
//
//        docs3.put(doc_11,str11);
//        docs3.put(doc_12,str12);
//        docs3.put(doc_13,str13);
//        docs3.put(doc_14,str14);
//        docs3.put(doc_15,str15);
//
//
//        Documentt doc_16=new Documentt("DOC_16",0,0,"",0);
//        String str16="father";
//
//        Documentt doc_17=new Documentt("DOC_17",0,0,"",0);
//        String str17="mouse ale al hodaya";
//
//
//        Documentt doc_18=new Documentt("DOC_18",0,0,"",0);
//        String str18="love basketball";
//
//        Documentt doc_19=new Documentt("DOC_19",0,0,"",0);
//        String str19="hey computer";
//
//        Documentt doc_20=new Documentt("DOC_20",0,0,"",0);
//        String str20="1990 apple";
//
//
//        docs4.put(doc_16,str16);
//        docs4.put(doc_17,str17);
//        docs4.put(doc_18,str18);
//        docs4.put(doc_19,str19);
//        docs4.put(doc_20,str20);
//
//        List<Map<Documentt,String>> list=new ArrayList<>();
//        list.add(docs1);
//        list.add(docs2);
//        list.add(docs3);
//        list.add(docs4);
//
//       // Indexer indexer=new Indexer(false);
//        //indexer.BuildTempPostings(list);
//
//       // Indexer indexer=new Indexer(false);
//       // indexer.BuildTempPostings(docs1);
//       // indexer.BuildTempPostings(docs2);
//       // indexer.BuildTempPostings(docs3);
//       // indexer.BuildTempPostings(docs4);
//
//        //indexer.merge_All_tmp_postings_files();
//        System.out.println("gfgfd");
//    }
//
//}
