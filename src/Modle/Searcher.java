package Modle;

import java.util.*;


public class Searcher {

    private Ranker ranker;

    public Searcher (String postingindisk){
        this.ranker=new Ranker(postingindisk);
    }

   public ArrayList<String> Search_query(String query,boolean stem,boolean semantic_treatment ,ArrayList<String> cities_limitation,String corpuspath )
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

       ArrayList<String> results=ranker.Rank(parsed_query,false,new ArrayList<String>());
       for(int i=0;i<results.size();i++)
           System.out.println(results.get(i));


return null;
   }

    private ArrayList<String> citiesLimitation(ArrayList<String> cities_limitation) {//TODO להוציא את המסמכים שעומדים בהגסלת הערים
        return null;
    }


}
