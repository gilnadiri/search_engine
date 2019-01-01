package Modle;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Searcher {

    private Ranker ranker;
    private HashMap<String, City> cities_index;

    public Searcher(String postingindisk, boolean stem) {
        this.cities_index = new HashMap<>();
        Load_cities_from_Disk(postingindisk);
        this.ranker = new Ranker(postingindisk, false, cities_index);
    }

    public ArrayList<Term> showDic() {

        return ranker.showDic();
    }

    public ArrayList<String> getCitie() {
        ArrayList<String> ans = new ArrayList<>();
        for (Map.Entry<String, City> entry : cities_index.entrySet()) {
            ans.add(entry.getKey());
        }
        return ans;
    }


    public ArrayList<Map.Entry<Documentt, Double>> Search_single_query(String query, boolean stem, boolean semantic_treatment, ArrayList<String> cities_limitation, String corpuspath,String destanation_of_results_file,boolean original_single) {

        boolean exist_cities_limitation = false;
        if (cities_limitation.size() > 0)
            exist_cities_limitation = true;
        HashSet<String> fitToLimitation = new HashSet<>();
        if (exist_cities_limitation)
            fitToLimitation = citiesLimitation(cities_limitation);



        Map<String, TokenInfo> parsedQuery = new Parse(corpuspath).getParsing(query, "query", stem);
        ArrayList<String> parsed_query = new ArrayList<>();
        for (Map.Entry<String, TokenInfo> entry : parsedQuery.entrySet())
            parsed_query.add(entry.getKey());

            if(semantic_treatment)
                parsed_query=semantic(parsed_query);

        ArrayList<Map.Entry<Documentt, Double>> results = ranker.Rank(parsed_query, exist_cities_limitation, fitToLimitation);
        if(original_single)
            save_results_single_query_to_disk(destanation_of_results_file,results);
        return results;

    }

    private void save_results_single_query_to_disk(String destanation_of_results_file,ArrayList<Map.Entry<Documentt,Double>> results) {
        FileWriter fos = null;

        try {
            fos = new FileWriter(destanation_of_results_file + "\\" + "results.txt");
            PrintWriter pw = new PrintWriter(fos);

           for(int i=0;i<results.size();i++)
           {
               String line="111" + " 0 " + results.get(i).getKey().Doc_Name +  " 1 42.38 mt" + "\n";
               pw.write(line);
           }

            pw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public ArrayList<String> semantic(ArrayList<String> original_query)  {

        ArrayList<String> semantic_words=new ArrayList<>();
            for(String word:original_query) {
                URL url = null;

                try {
                    url = new URL("https://api.datamuse.com/words?ml=" + word);
                    URLConnection urlc = null;
                    urlc = url.openConnection();
                    urlc.setDoOutput(true);
                    urlc.setAllowUserInteraction(false);
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
                    String curLine = null;
                    curLine = br.readLine();
                    if (curLine != null && !curLine.equals("")) {
                        String[] firstLineArr = curLine.split("\"word\":\"");
                        for (String s : firstLineArr) {
                            int i = 0;
                            String curWord = "";
                            while (s != null && i < s.length() && s.charAt(i) != '\"') {
                                if (Character.isLetter(s.charAt(i)))
                                    curWord = curWord + s.charAt(i);
                                i++;
                            }
                            if (curWord != null && curWord.length() > 0) {
                                semantic_words.add(curWord);
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                }
            }
                    for (String sema : semantic_words)
                        original_query.add(sema);
            return original_query;
        }


    public LinkedHashMap<String, ArrayList<Map.Entry<Documentt, Double>>> Search_files_quries(String query_file_path, boolean stem, boolean semantic, ArrayList<String> cities_limitation, String corpuspath, String destanation_of_results_file) {
        LinkedHashMap<String, ArrayList<Map.Entry<Documentt, Double>>> res = new LinkedHashMap<>();

        ArrayList<String> quries = extract_quries_from_file(query_file_path);


        for (int i = 0; i < quries.size(); i++) {
            String[] s = quries.get(i).split("~");
            String id_query = s[0];
            String query = s[1];

            ArrayList<Map.Entry<Documentt, Double>> rank_for_query = Search_single_query(query, stem, semantic, cities_limitation, corpuspath,destanation_of_results_file,false);
            res.put(id_query, rank_for_query);
        }

        write_Results_file_quries_TO_Disk(res, destanation_of_results_file);
        return res;


    }

    private void write_Results_file_quries_TO_Disk(LinkedHashMap<String, ArrayList<Map.Entry<Documentt, Double>>> res, String dest_of_result_file) {

        FileWriter fos = null;

        try {
            fos = new FileWriter(dest_of_result_file + "\\" + "results.txt");
            PrintWriter pw = new PrintWriter(fos);

        for (Map.Entry<String, ArrayList<Map.Entry<Documentt, Double>>> entry1 : res.entrySet()) {
            String queryId = entry1.getKey();
            ArrayList<Map.Entry<Documentt, Double>> docs_for_query = entry1.getValue();
            for (Map.Entry<Documentt, Double> doc : docs_for_query) {
                String line = queryId + " 0 " + doc.getKey().Doc_Name + " 1 42.38 mt" + "\n";
                pw.write(line);
            }
        }
        pw.close();
        fos.close();
    } catch (IOException e) {
            e.printStackTrace();
        }


    }



    private ArrayList<String> extract_quries_from_file(String queries_file) {
        ArrayList<String> queries=new ArrayList<>();
        try {
            String fille = new String (Files.readAllBytes(Paths.get(queries_file)));
            String file=fille.replace("\n","");
            String [] s1= file.split("<top>");
            for(String top : s1){
                if(top.equals(""))
                    continue;
                String[] s2=top.split("<title>");
                String line_number=s2[0];
                String id_query=extract_id_query(line_number);
                String s3[]=s2[1].split("<desc>");
                String title=s3[0];
                String description=descrition(s3[1]);
                queries.add(id_query+"~"+title+description);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return queries;


    }

    private String descrition(String s) {
          String []desc =s.split("<narr>");
          String description=desc[0];
          description=remove_words(description);
          return description;
    }

    private String remove_words(String description) {
    String ans=" ";
    String[] words=description.split(" ");
         for(String w:words)
             if( !w.equals("Description:") && !w.equals("information") && !w.equals("available") && !w.equals("Identify") && !w.equals("documents") && !w.equals("discuss") )
                 ans=ans+" ";
         return ans;


    }

    private void Load_cities_from_Disk(String posting_in_disk) {
        FileInputStream fis;

        try {
            fis = new FileInputStream( posting_in_disk+ "\\" + "cities");
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(fis);

                this.cities_index=(HashMap) ois.readObject();
                ois.close();
                fis.close();


            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private String extract_id_query(String line_number) {
        String[] id=line_number.split(":");
        id[1]=id[1].replace(" ","");
        return id[1];
    }


    private HashSet<String> citiesLimitation(ArrayList<String> cities_limitation) {
        HashSet<String> fitToLimitation=new HashSet<>();
        for(String city : cities_limitation){
            HashMap<String,String> alldocs_in_the_city=cities_index.get(city).getLocation();
            for(Map.Entry<String,String> entry : alldocs_in_the_city.entrySet())
                fitToLimitation.add(entry.getKey());
        }
        return fitToLimitation;

    }


}