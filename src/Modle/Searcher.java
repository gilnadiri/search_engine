package Modle;

import javafx.scene.Parent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Searcher {

    private Ranker ranker;
    private  HashMap<String,City> cities_index;

    public Searcher (String postingindisk){
        this.cities_index=new HashMap<>();
        Load_cities_from_Disk(postingindisk);

        this.ranker=new Ranker(postingindisk,false);


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


    public ArrayList<Map.Entry<String,Double>> Search_single_query(String query, boolean stem, boolean semantic_treatment , ArrayList<String> cities_limitation, String corpuspath )
   {
       boolean exist_cities_limitation=false;
       if(cities_limitation.size()>0)
           exist_cities_limitation=true;
       HashSet<String> fitToLimitation=new HashSet<>();
       if(exist_cities_limitation)
            fitToLimitation=citiesLimitation(cities_limitation);

       Map<String,TokenInfo> parsedQuery=new Parse(corpuspath).getParsing(query,"query",stem);
       ArrayList<String> parsed_query=new ArrayList<>();
       for(Map.Entry<String,TokenInfo> entry: parsedQuery.entrySet())
           parsed_query.add(entry.getKey());


       ArrayList<Map.Entry<String,Double>> results=ranker.Rank(parsed_query,exist_cities_limitation,fitToLimitation);
       return results;

   }

   public LinkedHashMap<String, ArrayList<Map.Entry<String,Double>> > Search_files_quries(String query_file_path,boolean stem,boolean semantic,ArrayList<String> cities_limitation,String corpuspath)
   {
       LinkedHashMap<String,ArrayList<Map.Entry<String,Double>>> res=new LinkedHashMap<>();


       ArrayList<String> quries=extract_quries_from_file(query_file_path);

       for(int i=0;i<quries.size();i++)
       {
           String[]s=quries.get(i).split("~");
           String id_query=s[0];
           String query=s[1];
           ArrayList<Map.Entry<String,Double>> rank_for_query=Search_single_query(query,stem,semantic,cities_limitation,corpuspath);
           res.put(id_query,rank_for_query);
       }

        return res;


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
                queries.add(id_query+"~"+title);
            }
            return queries;





        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
