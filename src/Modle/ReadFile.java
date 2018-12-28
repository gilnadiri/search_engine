package Modle;
import javax.print.Doc;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ReadFile {

    private String CorpusPath;


    public ReadFile(String CorpusPath) {
        this.CorpusPath=CorpusPath;
    }

    public HashMap<String,City> readAllCities(String path, HashSet<String> langueges) throws IOException {
        File Main = new File(path);
        File[] Dirs = Main.listFiles();
        HashMap<String,City> allcities=new HashMap<>();
        for (int i = 0; i < Dirs.length; i++) {
            if(Dirs[i].getName().equals("stop_words.txt"))
                continue;
            File temp = Dirs[i];
            File[] currDir = temp.listFiles();
            File currFile = currDir[0];

            String fille = new String(Files.readAllBytes(Paths.get(currFile.toPath().toString())));
            String[] docs = fille.split("<DOC>\n");
            for (String currentDoc : docs) {
                if (currentDoc.equals("") || currentDoc.equals("\n"))
                    continue;
                String[] s1 = currentDoc.split("<DOCNO>");
                String[] s2 = s1[1].split("</DOCNO>");
                if(s2[0].charAt(0)==' ') s2[0]=s2[0].substring(1);
                if(s2[0].charAt(s2[0].length()-1)==' ') s2[0]=s2[0].substring(0,s2[0].length()-1);
                String[] text = s2[1].split("<TEXT>\n");
                if (text.length < 2)
                    continue;
                String[] s3 = text[0].split("<F P=104>");
                if (!(s3.length == 1 || s3[1].length() >= 4 && s3[1].substring(0, 4).equals("</F>"))){
                    String[] s4 = s3[1].split(" ");
                    for (int j = 0; j < s4.length; j++) {
                        if (!(s4[j].equals("")) && !(s4[j].toUpperCase().equals("THE"))&& !(s4[j].toUpperCase().equals("FOR"))) {
                            if (allcities.containsKey(s4[j].toUpperCase())){
                                allcities.get(s4[j].toUpperCase()).docExistANdAdd(s2[0],"title");
                                break;

                            }
                            allcities.put(s4[j].toUpperCase(), new City(s4[j].toUpperCase(), "", "", "",s2[0]));
                            break;
                        }
                    }
                }
                String lang=add_langues(text[1]);
                if(!(lang.equals("")))
                    langueges.add(lang);

            }
        }
        return allcities;
    }

    private String add_langues(String s) {
      String []s1=s.split("<F P=105>");
      if(s1.length!=1) {
          String[] s2 = s1[1].split("</F>");
          String ans="";
          String s3=s2[0];
          for(int i=0;i<s3.length();i++)
              if(s3.charAt(i)!=' ')
                  ans=ans+s3.charAt(i);
          return ans;
      }
      else return "";
    }

    public Map<Documentt, String> readSingleFile(File singleFile) throws IOException
    {
        Map<Documentt,String> map=new HashMap<>();
        String fille = new String ( Files.readAllBytes( Paths.get(singleFile.toPath().toString()) ) );
        String [] docs= fille.split("<DOC>\n");
        for (String currentDoc:docs){
            if(currentDoc.equals("") || currentDoc.equals("\n"))
                continue;
            String [] s1=currentDoc.split("<DOCNO>");
            String[] s2=s1[1].split("</DOCNO>");
            if(s2[0].charAt(0)==' ') s2[0]=s2[0].substring(1);
            if(s2[0].charAt(s2[0].length()-1)==' ') s2[0]=s2[0].substring(0,s2[0].length()-1);

            Documentt d=new Documentt(s2[0],-1,-1,"",-1);



            String []text=s2[1].split("<TEXT>\n");
            if(text.length<2)
                continue;
            String []s3=text[0].split("<F P=104>");
            if(s3.length==1 ||  s3[1].length()>=4 &&  s3[1].substring(0,4).equals("</F>") )
                d.setDoc_City("-1");
            else {
                String[] s4 = s3[1].split(" ");
                for (int i = 0; i < s4.length; i++)
                    if (!(s4[i].equals(""))) {
                        d.setDoc_City(s4[i].toUpperCase());
                        break;
                    }
            }
            String[] splitByEndText = text[1].split("</TEXT>\n");
            map.put(d,splitByEndText[0]);

        }
        return map;
    }
}