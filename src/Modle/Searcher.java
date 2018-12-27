package Modle;

import java.util.*;


public class Searcher {

    private Ranker ranker;

    public Searcher (String postingindisk){
        this.ranker=new Ranker(postingindisk);
    }

   public ArrayList<Map.Entry<String,Double>> Search_query(String query,boolean stem,boolean semantic_treatment ,ArrayList<String> cities_limitation,String corpuspath )
   {
       boolean exist_cities_limitation=false;
       if(cities_limitation.size()>0)
           exist_cities_limitation=true;
       ArrayList<String> fitToLimitation;
       if(exist_cities_limitation)
            fitToLimitation=citiesLimitation(cities_limitation);

       Map<String,TokenInfo> parsedQuery=new Parse(corpuspath).getParsing(query,"query",stem);
       ArrayList<String> parsed_query=new ArrayList<>();
       for(Map.Entry<String,TokenInfo> entry: parsedQuery.entrySet())
           parsed_query.add(entry.getKey());

       ArrayList<Map.Entry<String,Double>> results=ranker.Rank(parsed_query,false,new ArrayList<String>());
       return results;


   }

    private ArrayList<String> citiesLimitation(ArrayList<String> cities_limitation) {//TODO להוציא את המסמכים שעומדים בהגסלת הערים
        return null;
    }


}
