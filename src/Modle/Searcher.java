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
    private Parse parser;

    public Searcher(String postingindisk, boolean stem) {
        this.cities_index = new HashMap<>();
        Load_cities_from_Disk(postingindisk);
        this.ranker = new Ranker(postingindisk, stem, cities_index);
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

    /**
     * return the most 50 relevant documents sorted by rank.
     */
    public ArrayList<Map.Entry<Documentt, Double>> Search_single_query(String query, boolean stem, boolean semantic_treatment, ArrayList<String> cities_limitation, String corpuspath,String destanation_of_results_file,boolean original_single,boolean saveRes) {

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
        ArrayList<String> addToQuery=new ArrayList<>();
        if(semantic_treatment) {
            addToQuery = semantic(parsed_query);
            parsed_query.addAll(addToQuery);
        }

        ArrayList<Map.Entry<Documentt, Double>> results = ranker.Rank(parsed_query, exist_cities_limitation, fitToLimitation,stem);
        if(original_single&&saveRes)
            save_results_single_query_to_disk(destanation_of_results_file,results);
        return results;

    }

    /**
     * save the result in disk, in the format of trec evel
     * @param destanation_of_results_file
     * @param results
     */
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

    /**
     * for each word in the query-get the most close word in semantic and add it to the query
     * @param parsedQuery
     * @return
     */
    public ArrayList<String> semantic(ArrayList<String> parsedQuery)  {
        ArrayList<String> synanems=new ArrayList<>();
        String http="https://api.datamuse.com/words?ml=";//append the word to find syn
        for (int i = 0; i <parsedQuery.size() ; i++) {
            try {
                String semant=parsedQuery.get(i);
                URL url = new URL( http + semant);
                URLConnection urlConnection = url.openConnection();
                InputStream inputStream=urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String toRead = bufferedReader.readLine();
                if (legal(toRead)) {
                    String removedBegining=toRead.substring(10,toRead.length());
                            String [] arr=removedBegining.split("\"");
                            String toAdd = arr[0];
                            if(toAdd.contains(" ")) {//the syn is 2 words
                                String[] twoWordsSyn = toAdd.split(" ");
                                if (twoWordsSyn.length > 1) {
                                    if (canAdd(twoWordsSyn[0]))
                                        synanems.add(twoWordsSyn[0]);
                                    if (canAdd(twoWordsSyn[1]))
                                        synanems.add(twoWordsSyn[1]);
                                    continue;
                                }
                                toAdd=toAdd.replace(" ","");
                            }
                        if(canAdd(toAdd)){
                            synanems.add(toAdd);
                        }
                }
            } catch (IOException e) {
                int x=0;//may throw exception with no good wifi
            }
        }
        return synanems;
    }
    /**
     *  check if word given from API is legal to add
     */
    private boolean canAdd(String toAdd) {
        if(toAdd!=null&&toAdd.length()!=0&&!toAdd.equals(" "))
            return true;
        return false;
    }

    /**
     *  check if line from API is legal to read
     */
    private boolean legal(String toRead) {
        if(toRead!=null&&toRead.length()>10)
            return true;
        return false;
    }

    /**
     *  return the 50 most relevant documents, for each query, in the file queries
     */
    public LinkedHashMap<String, ArrayList<Map.Entry<Documentt, Double>>> Search_files_quries(String query_file_path, boolean stem, boolean semantic, ArrayList<String> cities_limitation, String corpuspath, String destanation_of_results_file,boolean saveRes) {

        LinkedHashMap<String, ArrayList<Map.Entry<Documentt, Double>>> res = new LinkedHashMap<>();

        ArrayList<String> quries = extract_quries_from_file(query_file_path);

        for (int i = 0; i < quries.size(); i++) {
            String[] s = quries.get(i).split("~");
            String id_query = s[0];
            String query = s[1];

            ArrayList<Map.Entry<Documentt, Double>> rank_for_query = Search_single_query(query, stem, semantic, cities_limitation, corpuspath,destanation_of_results_file,false,saveRes);
            res.put(id_query, rank_for_query);
        }

        if(saveRes)
            write_Results_file_quries_TO_Disk(res, destanation_of_results_file);

        return res;


    }

    /**
     * save the resuls to the disk , in the format of trec evel
     * @param res
     * @param dest_of_result_file
     */
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


    /**
     * return the qury from the tag <title> from the file
     * @param queries_file
     * @return
     */
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

    /**
     *extract the discription, from the tag
     */
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
                ans=(ans+w+" ");
        return ans.replace("\r"," ");


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