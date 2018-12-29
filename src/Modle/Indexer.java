package Modle;
import java.io.*;
import java.util.*;

public class Indexer {


    private String CorpusPath;
    private String Posting_And_dictionary_path_in_disk;
    private boolean wantToStemm=false;
    private Stemmer stemmer;
    private int PostingNum;                  //help veraible for building the temporary postings
    private int N;                          //number of documents in the corpus
    public Map<String,Documentt> documents; //information on every doc
    public Map<String,Term> dictionary;     //inverted index of terms
    private Parse parser;
    public HashMap<String,City> cities_index;    //inverted index of cities
    private HashSet <String> languages;



    public Indexer(boolean wantToStemm, String corpusPath, String Posting_And_dictionary_path_in_disk)  {
        this.documents = new HashMap<>();
        dictionary=new HashMap<>();
        cities_index=new HashMap<>();
        parser=new Parse(corpusPath);
        this.wantToStemm = wantToStemm;
        PostingNum = 0;
        this.CorpusPath=corpusPath;
        this.Posting_And_dictionary_path_in_disk=Posting_And_dictionary_path_in_disk;
        this.languages=new HashSet<>();

    }

    /**
     * the main function-read files,pars, build temporary postings files,and merge them.
     */
    public   void iniateIndex()  {
        try {

            ReadFile corpus = new ReadFile(CorpusPath);
            //initiate the city index by tags
            cities_index=corpus.readAllCities(CorpusPath,languages);
            parser.setCities(cities_index);

            File Main = new File(CorpusPath);
            File[] DirsAndSW = Main.listFiles();
            //build all the tmp posting files-1 temporary posting file per 40 files
            int coun=10;
            List<Map<Documentt,String>> fortyFiles=new ArrayList<>();


            File[] Dirs=ignoreStopWords(DirsAndSW);

            boolean left=true;
            for (int i = 0; i < Dirs.length; i++) {

                File temp = Dirs[i];
                File[] currDir = temp.listFiles();
                File currFile = currDir[0];//always one file in a dir
                if(Dirs.length-i<coun&&left){
                    left=false;
                    coun=Dirs.length-i;
                }
                coun--;
                fortyFiles.add(corpus.readSingleFile(currFile));
                ;
                if(coun==0){
                    coun=10;
                    BuildTempPostings(fortyFiles);
                    fortyFiles.clear();
                }
            }
            //finished build the temporary posting files

            this.N=documents.size();  //update number of docs in the corpus

            merge_All_tmp_postings_files(); //merge all the temporary posting files into one finel posting
            delete_all_temporary_posting_files();  //delete the temporary files

            cities_index=parser.getCities();   //update the cities_index

            save_Dictionary_in_disk();
            save_cities_index_in_disk();
            save_Docs_map_in_disk();
        }
        catch (IOException e)
        {e.printStackTrace();}
    }





    /**
     * this func take the text of document, parsed it and build tmp posting file for 40 files from the corpus
     * @param listtttt-list of maps-each map contains documents and their text
     */
    public void BuildTempPostings(List<Map<Documentt,String>> listtttt) {

        try {
            PostingNum++;
            Map<String, String> temporary_posting = new HashMap<>();
            for(Map<Documentt,String> documents_To_parse: listtttt) {
                for (Map.Entry<Documentt, String> entry : documents_To_parse.entrySet()) {
                    Map<String, TokenInfo> terms_after_parser = parser.getParsing(entry.getValue(),entry.getKey().Doc_Name,wantToStemm);

                    //update info on document, and add it to the docs dictionary
                    Documentt tmp = entry.getKey();
                    int[] details=find_max_tf_AND_DocLength(terms_after_parser);
                    tmp.setDoc_uniqe_words(terms_after_parser.size());
                    tmp.setDoc_max_tf(details[0]);
                    tmp.setDocLength(details[1]);
                    tmp.setYeshooyot(top_five_yeshooyot(terms_after_parser));
                    documents.put(tmp.getDoc_Name(),tmp);


                    for (Map.Entry<String, TokenInfo> entry_of_parsedTerms : terms_after_parser.entrySet()) {
                        String term = entry_of_parsedTerms.getKey();
                        String postOfterm = "";


                        String term_lower = term.toLowerCase();
                        if (temporary_posting.containsKey(term_lower)) {
                            postOfterm = temporary_posting.get(term_lower)+tmp.Doc_Name+"+"+entry_of_parsedTerms.getValue().frequentInDoc+"~"+entry_of_parsedTerms.getValue().atStart+",";  //if the term exist-just add the new info on the new doc and tf and st start
                            temporary_posting.put(term_lower, postOfterm);
                            continue;
                        } else {

                            String term_upper = term.toUpperCase();
                            if (temporary_posting.containsKey(term_upper) && term.charAt(0) >= 'a' && term.charAt(0) <= 'z')  //change in the dictionary yo llower case and teshasher
                            {
                                String[] s = temporary_posting.get(term_upper).split(":");
                                postOfterm = term_lower + ":"+s[1]+tmp.Doc_Name+"+"+entry_of_parsedTerms.getValue().frequentInDoc+"~"+entry_of_parsedTerms.getValue().atStart+",";
                                temporary_posting.remove(term_upper);
                                temporary_posting.put(term_lower, postOfterm);
                                continue;
                            } else if (temporary_posting.containsKey(term_upper) && term.charAt(0) >= 'A' && term.charAt(0) <= 'Z') {
                                postOfterm = temporary_posting.get(term_upper)+tmp.Doc_Name+"+"+entry_of_parsedTerms.getValue().frequentInDoc+"~"+entry_of_parsedTerms.getValue().atStart+",";
                                temporary_posting.put(term_upper, postOfterm);
                                continue;

                            }


                        }
                        if (!(temporary_posting.containsKey(term))) {
                            postOfterm = term + ":" + tmp.Doc_Name + "+" + entry_of_parsedTerms.getValue().frequentInDoc+"~"+entry_of_parsedTerms.getValue().atStart+",";
                            temporary_posting.put(term, postOfterm);
                            continue;
                        }


                    }
                }
            }

            //sort the posting by alphbet-ignoring upper lower cases
            List<String> list = new ArrayList<>(temporary_posting.values());
            list.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    String s1 = o1.substring(0, o1.indexOf(':'));
                    String s2 = o2.substring(0, o2.indexOf(':'));
                    return s1.compareToIgnoreCase(s2);
                }
            });

            //writing the temporary posting to a temporary posting file
            FileWriter fileWriter;
            if(wantToStemm)
                fileWriter = new FileWriter(Posting_And_dictionary_path_in_disk + "\\" + PostingNum+"stem");
            else
                fileWriter = new FileWriter(Posting_And_dictionary_path_in_disk + "\\" + PostingNum);
            PrintWriter temporaryFile = new PrintWriter(fileWriter);
            for (int i = 0; i < list.size(); i++)
                temporaryFile.println(list.get(i));

            temporaryFile.flush();
            temporaryFile.close();


        }
        catch (IOException ex){
        }


    }

    private ArrayList<String> top_five_yeshooyot(Map<String, TokenInfo> terms_after_parser) {
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
        for(int i=0;i<5 && i<list.size();i++)
            top_5.add(list.get(i).getKey()+"+"+list.get(i).getValue());
        return top_5;
    }





    /**
     * merge all the temporary files to final posting file
     */
    public void merge_All_tmp_postings_files() throws IOException {
        File f;
        if(wantToStemm)
            f = new File(Posting_And_dictionary_path_in_disk + "\\" + "final_posting stem");
        else
            f = new File(Posting_And_dictionary_path_in_disk + "\\" + "final_posting");
        BufferedWriter final_posting = new BufferedWriter(new FileWriter(f));



        BufferedReader[] buffers_readers = new BufferedReader[PostingNum + 1];
        if(wantToStemm)
            for (int i = 1; i <= PostingNum; i++)
                buffers_readers[i] = new BufferedReader(new FileReader(new File(Posting_And_dictionary_path_in_disk + "\\" + i+"stem")));

        else
            for (int i = 1; i <= PostingNum; i++)
                buffers_readers[i] = new BufferedReader(new FileReader(new File(Posting_And_dictionary_path_in_disk + "\\" + i)));


        Queue<String> queue = new PriorityQueue<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String s1 = o1.substring(0, o1.indexOf(':'));
                String s2 = o2.substring(0, o2.indexOf(':'));
                return s1.compareToIgnoreCase(s2);
            }
        });

        for (int i = 1; i <= PostingNum; i++)
            queue.add(buffers_readers[i].readLine() + "&" + i);

        long pointer_to_final_posting=0;

        while (queue.size() != 0) {
            //get next line (united if there are some lines of the same term)
            String curLine = queue.poll();
            String[] s = curLine.split("&");
            curLine = s[0];
            int i = Integer.valueOf(s[1]);
            String str = buffers_readers[i].readLine();
            if (str != null)
                queue.add(str + "&" + i);

            boolean continu = true;
            while (continu) {
                if (queue.size() == 0)
                    break;
                String check = queue.poll();
                if (is_The_Same_Term(curLine, check)) {
                    String[] s2 = check.split("&");
                    curLine = merge_lines(curLine, s2[0]);
                    String toadd = buffers_readers[Integer.valueOf(s2[1])].readLine();

                    if (toadd != null)
                        queue.add(toadd + "&" + Integer.valueOf(s2[1]));

                } else {
                    continu = false;
                    queue.add(check);
                }

            }

            pointer_to_final_posting=insert_to_dictionary(pointer_to_final_posting,curLine);
            final_posting.write(curLine + "\n");


        }


        final_posting.flush();
        final_posting.close();
        for(int i=1;i<buffers_readers.length;i++)
            buffers_readers[i].close();

    }

    public void delete_all_temporary_posting_files() {


        if (wantToStemm)
            for (int i = 1; i <= PostingNum; i++) {
                File file = new File(Posting_And_dictionary_path_in_disk + "\\" + i + "stem");
                file.delete();


            }
        else
            for (int i = 1; i <= PostingNum; i++) {
                File file = new File(Posting_And_dictionary_path_in_disk + "\\" + i);
                file.delete();
            }
    }


    private int[] find_max_tf_AND_DocLength(Map<String, TokenInfo> terms_for_index) {
        int [] details=new int[2];
        int max=0;
        int doc_length=0;
        for (Map.Entry<String,TokenInfo> entry : terms_for_index.entrySet()) {
            doc_length=doc_length+entry.getValue().frequentInDoc;
            if(entry.getValue().frequentInDoc>max)
                max=entry.getValue().frequentInDoc;
        }
        details[0]=max;
        details[1]=doc_length;
        return details;

    }


    private long insert_to_dictionary(long pointer_to_final_posting,String curLine) {
        int df=curLine.split(",").length;
        String t=curLine.substring(0,curLine.indexOf(":"));
        int total_frequncy_in_corpus=compute_total_freq_in_corpus(curLine);
        Term term=new Term(t,pointer_to_final_posting,df,total_frequncy_in_corpus);
        dictionary.put(t,term);
        return pointer_to_final_posting=pointer_to_final_posting+curLine.getBytes().length+1;
    }

    private int compute_total_freq_in_corpus(String curLine) { //TODO מה קורה אם אחרי הפלוס יש מספר באורך 2 תווים
        int frequncy=0;
        for(int i=0;i<curLine.length();i++)
            if(curLine.charAt(i)=='+') {
                //String c=curLine.charAt(i+1)+"";
                String c=num(curLine,i);
                int f=Integer.valueOf(c);
                frequncy = frequncy + f;
            }
        return frequncy;
    }

    private String num(String curLine, int i) {
        String ans="";
        for(int x=i+1;x<curLine.length();x++){
            if(curLine.charAt(x)=='~')
                break;
            ans+=curLine.charAt(x);
        }
        return ans;
    }

    private File[] ignoreStopWords(File[] DirsAndSW) {
        int j=0;
        File[] Dirs=new File[DirsAndSW.length-1];
        for (int i = 0; i < DirsAndSW.length; i++) {
            if (!DirsAndSW[i].getName().equals("stop_words.txt")) {
                Dirs[j]=DirsAndSW[i];
                j++;
            }
        }
        return Dirs;
    }
    private String merge_lines(String curLine, String check) {
        String s1 = curLine.substring(0, curLine.indexOf(':'));
        String s2 = check.substring(0, check.indexOf(':'));
        if(Character.isLowerCase(s1.charAt(0)) || Character.isLowerCase(s2.charAt(0)) )
            if(Character.isLowerCase(s1.charAt(0)))
                return curLine+check.substring(check.indexOf(':')+1);
            else
                return  check+curLine.substring(curLine.indexOf(':')+1);

        else
            return curLine+check.substring(check.indexOf(':')+1);
    }

    private boolean is_The_Same_Term(String curLine, String check) {
        String s1 = curLine.substring(0, curLine.indexOf(':'));
        String s2 = check.substring(0, check.indexOf(':'));
        if(s1.compareToIgnoreCase(s2)==0)
            return true;
        else
            return false;
    }

    private String mergeToPostingOfSameTerms(String str1, String str2) {
        str2=str2.substring(str2.indexOf(":")+1, str2.length()   );
        return str1+str2;
    }

    public void setWantToStemm(boolean wantToStemm) {
        this.wantToStemm = wantToStemm;
    }

    public  void setNUM(int n){this.PostingNum=n;}

    public HashSet<String> getLanguages() {
        return languages;
    }

    public ArrayList<Term> showDic() {
       if(dictionary.size()==0)
           return null;

        ArrayList<Term> list = new ArrayList<>(dictionary.values());
        list.sort(new Comparator<Term>() {
            @Override
            public int compare(Term o1, Term o2) {
                String term1 = o1.getTerm();
                String term2 = o2.getTerm();
                return term1.compareToIgnoreCase(term2);
            }
        });

        return list;
    }

    public void reset(String destination) {
        //clean the disk
        delete_posting_file(destination);
        delete_dictionary(destination);
        delete_cities_index(destination);
        delete_docs_map(destination);

        //clean the RAM
        dictionary.clear();
        cities_index.clear();
        documents.clear();


    }


    //<editor-fold desc="delete from disk">
    private void delete_docs_map(String destination) {
        File docs_stem = new File(destination + "\\"+"docs stem");
        docs_stem.delete();

        File docs = new File(destination + "\\"+"docs");
        docs.delete();
    }

    private void delete_cities_index(String destination) {
        File cities_index_stem = new File(destination + "\\"+"cities stem");
        cities_index_stem.delete();

        File cities_index = new File(destination + "\\"+"cities");
        cities_index.delete();

    }

    private void delete_dictionary(String destination) {
        File dictionary_stem = new File(destination + "\\"+"dictionary stem" );
        dictionary_stem.delete();

        File dictionary_without_stem = new File(destination + "\\"+"dictionary" );
        dictionary_without_stem.delete();
    }

    private void delete_posting_file(String destination) {
        File posting_stem = new File(destination + "\\"+"final_posting stem" );
        posting_stem.delete();

        File posting_without_stem = new File(destination + "\\"+"final_posting" );
        posting_without_stem.delete();

    }
    //</editor-fold>


    public boolean loadDic(boolean wantToStem, String destination) {
        File f;
       if(wantToStem)
         f = new File(destination + "\\" + "dictionary stem");
       else
          f=new File(destination + "\\" + "dictionary") ;
       if(!f.exists()) {
            return false;
        }

        load_dictionary(wantToStem,destination);
        load_Documents (wantToStem,destination);
        return true;

    }


    public int numOfDocs() {
        return documents.size();
    }

    public int numOfTerms() {
        return dictionary.size();
    }

    public int numOfCities() {
        return cities_index.size();
    }





    private void save_cities_index_in_disk() {
        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(Posting_And_dictionary_path_in_disk + "\\" +"cities" );
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(cities_index);
            oos.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }







/*
    private void save_cities_index_in_disk() {
        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(Posting_And_dictionary_path_in_disk + "\\" +"cities" );
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(cities_index);
            oos.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

*/







    private void save_Dictionary_in_disk(){
        FileWriter fos = null;
        try {
            if(wantToStemm)
                fos = new FileWriter(Posting_And_dictionary_path_in_disk + "\\" +"dictionary stem" );
            else
                fos = new FileWriter(Posting_And_dictionary_path_in_disk + "\\" +"dictionary" );
            PrintWriter pw = new PrintWriter(fos);
            String line;
            for(Map.Entry<String,Term> entry: dictionary.entrySet()) {
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
    }
/*
    private void save_Dictionary_in_disk() {
        FileOutputStream fos = null;
        try {
            if(wantToStemm)
                fos = new FileOutputStream(Posting_And_dictionary_path_in_disk + "\\" +"dictionary stem" );
            else
                fos = new FileOutputStream(Posting_And_dictionary_path_in_disk + "\\" +"dictionary" );
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dictionary);
            oos.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
  */




    private void load_dictionary(boolean wantToStem, String destination){
        BufferedReader br;
        try {
            if(wantToStem)
             br = new BufferedReader(new FileReader(new File(destination + "\\" + "dictionary stem")));
            else
                br=new BufferedReader(new FileReader(new File(destination + "\\" + "dictionary")));

            String line;
             while ( (line = br.readLine()) != null )
             {
                 String[] s=line.split("#");
                 Term term=new Term(s[0],Long.parseLong(s[1]),Integer.valueOf(s[2]),Integer.valueOf(s[3]));
                 dictionary.put(term.getTerm(),term);
             }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
  /*
    private void load_dictionary(boolean wantToStem, String destination) {
        FileInputStream fis;
        try {
            if (wantToStem)
                fis = new FileInputStream(destination + "\\" + "dictionary stem");
            else
                fis = new FileInputStream(destination + "\\" + "dictionary");

            ObjectInputStream ois = new ObjectInputStream(fis);
            dictionary=(HashMap) ois.readObject();
            ois.close();
            fis.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
*/






    private void save_Docs_map_in_disk(){
        FileWriter fos = null;
        try {
            if(wantToStemm)
                fos = new FileWriter(Posting_And_dictionary_path_in_disk + "\\" +"docs stem" );
            else
                fos = new FileWriter(Posting_And_dictionary_path_in_disk + "\\" +"docs" );
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


}
    /*
       private void save_Docs_map_in_disk() {
           FileOutputStream fos = null;
           try {
               if(wantToStemm)
                   fos = new FileOutputStream(Posting_And_dictionary_path_in_disk + "\\" +"docs stem" );
               else
                   fos = new FileOutputStream(Posting_And_dictionary_path_in_disk + "\\" +"docs" );
               ObjectOutputStream oos = new ObjectOutputStream(fos);
               oos.writeObject(documents);
               oos.close();
               fos.close();

           } catch (FileNotFoundException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }

       }
   */





    private void load_Documents(boolean wantToStem, String destination) {
        try {
            BufferedReader br;
            if(wantToStem)
                 br = new BufferedReader(new FileReader(new File(destination + "\\" + "docs stem")));
            else
                br=new BufferedReader(new FileReader(new File(destination + "\\" + "docs")));

            String line;
            while ( (line = br.readLine()) != null )
            {
                ArrayList<String> yeshooyott = new ArrayList<>();
                String[] s=line.split("#");
                if(s.length==6) {
                    String yeshooyot = s[5];
                    String[] s1 = yeshooyot.split(",");

                    for (int i = 0; i < s1.length; i++)
                        yeshooyott.add(s1[i]);
                }
                Documentt documett=new Documentt(s[0],Integer.valueOf(s[1]),Integer.valueOf(s[3]),s[4],Integer.valueOf(s[2]));
                documett.setYeshooyot(yeshooyott);
                documents.put(documett.Doc_Name,documett);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
/*
    private void load_Documents(boolean wantToStem, String destination) {
        FileInputStream fis;
        try {
            if (wantToStem)
                fis = new FileInputStream(destination + "\\" + "docs stem");
            else
                fis = new FileInputStream(destination + "\\" + "docs");

            ObjectInputStream ois = new ObjectInputStream(fis);
            documents=(HashMap) ois.readObject();
            ois.close();
            fis.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
*/

}





































