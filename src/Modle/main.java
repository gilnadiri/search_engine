package Modle;

import java.io.*;
import java.util.*;

public class main {
    public static void main(String[] args) throws IOException {



       // test3();

        //test4();
        //searcher.Search_files_quries("C:\\Users\\gil nadiri\\Desktop\\dest\\queries.txt",false,false,new ArrayList<String>(),"C:\\Users\\gil nadiri\\Desktop\\אחזור עבודה\\corpus");
       // ArrayList<Map.Entry<Documentt,Double>> myscores=searcher.Search_single_query("Falkland petroleum exploration",false,false,new ArrayList<String>(),"C:\\Users\\gil nadiri\\Desktop\\אחזור עבודה\\corpus");
        //ArrayList<String> judjescore=checkfromfile();
        //relevant_i_back_from_all_the_relevant(myscores,judjescore);

    }



//    private static void test3() {
//        Searcher searcher=new Searcher("C:\\Users\\hoday\\Desktop\\d",true);
//        searcher.Search_files_quries("C:\\Users\\hoday\\Desktop\\d\\queries.txt",true,false,new ArrayList<String>(),"C:\\Users\\hoday\\hodaya\\שנה ג\\אחזור\\corpus","C:\\Users\\hoday\\Desktop\\results",true);
//        //searcher.Search_single_query("Falkland petroleum exploration",false,true,new ArrayList<String>(),"C:\\Users\\hoday\\hodaya\\שנה ג\\אחזור\\corpus","C:\\Users\\hoday\\Desktop\\results",true,false);
//        int i=0;
//    }


    private static void relevant_i_back_from_all_the_relevant(ArrayList<Map.Entry<Documentt, Double>> myscores, ArrayList<String> judjescore) {
        ArrayList<String> my=new ArrayList<>();
        for(int i=0;i<myscores.size();i++)
            my.add(myscores.get(i).getKey().Doc_Name);
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
        if(s[3].equals("1") && s[0].equals("351"))
            return true;
        else return false;
    }


    public static void test1(){
        Indexer test=new Indexer(false,"C:\\Users\\hoday\\hodaya\\שנה ג\\אחזור\\testCorpus","C:\\Users\\hoday\\hodaya\\שנה ג\\אחזור\\dest");
        test.iniateIndex();

    }

    private static void test2() {
        HashMap<String,Documentt> documents=new HashMap<>();
        Documentt d1=new Documentt("d1",-1,-1,"mishmar",9);
        ArrayList<String> yeshooyot=new ArrayList<>();
        yeshooyot.add("HADAR+5");
        yeshooyot.add("A+3");
        yeshooyot.add("B+1");
        d1.setYeshooyot(yeshooyot);
        Documentt d2=new Documentt("d2",4,4,"p",7);
        ArrayList<String> yeshooyot2=new ArrayList<>();
        yeshooyot2.add("T+10");
        yeshooyot2.add("F+2");
        yeshooyot2.add("G+1");
        d2.setYeshooyot(yeshooyot2);
        documents.put("d1",d1);
        documents.put("d2",d2);

        FileWriter fos = null;
        try {


            fos = new FileWriter("C:\\Users\\gil nadiri\\Desktop\\dest\\New folder" + "\\" +"docs" );
            PrintWriter pw = new PrintWriter(fos);
            String line;
            for(Map.Entry<String,Documentt> entry: documents.entrySet()) {
                line = entry.getValue().toString();
                pw.println(line);

            }
            pw.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




        try {
            BufferedReader br;


            br=new BufferedReader(new FileReader(new File("C:\\Users\\gil nadiri\\Desktop\\dest\\New folder" + "\\" + "docs")));

            String line;
            while ( (line = br.readLine()) != null )
            {
                String[] s=line.split("#");
                String y=s[5];
                String[] s1 = y.split(",");
                ArrayList<String> yeshooyott=new ArrayList<>();
                for(int i=0;i<s1.length;i++)
                    yeshooyott.add(s1[i]);
                Documentt documett=new Documentt(s[0],Integer.valueOf(s[1]),Integer.valueOf(s[3]),s[4],Integer.valueOf(s[2]));
                documett.setYeshooyot(yeshooyott);
                documents.put(documett.Doc_Name,documett);
            }
            int i=0;


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}