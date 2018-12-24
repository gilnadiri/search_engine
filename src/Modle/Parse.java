package Modle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Parse {
    private String[] SplitDoc;
    private Map<String,TokenInfo> Tokens;
    private Map <String,String> months;
    private Set<String> stop_words;
    private HashMap <String,City> cities;
    private Cities allJson;
    private boolean wantToSteam;
    private Stemmer stemmer;



    public Parse(String courpus_path) {
        Tokens = new HashMap<>();
        cities=new HashMap<>();
        stop_words=new HashSet<>();
        allJson=new Cities();
        try {
            File file = new File(courpus_path + "\\" + "stop_words.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str = "";
            while ((str = br.readLine()) != null) {
                stop_words.add(str);
            }
            createMonths();
        }
        catch (IOException e){}
    }
    public HashMap<String,City> getCities(){
        return cities;
    }


    public void setCities(HashMap<String,City> cities){
        this.cities=cities;
    }

    private void createMonths() {
        months=new HashMap<>();

        months.put("January", "01");
        months.put("JANUARY", "01");
        months.put("JAN", "01");
        months.put("Jan", "01");
        months.put("February", "02");
        months.put("FEBRUARY", "02");
        months.put("FEB", "02");
        months.put("Feb", "02");
        months.put("March", "03");
        months.put("MARCH","03");
        months.put("MAR","03");
        months.put("Mar", "03");
        months.put("April", "04");
        months.put("APRIL", "04");
        months.put("APR", "04");
        months.put("Apr", "04");
        months.put("MAY", "05");
        months.put("May", "05");
        months.put("may", "05");
        months.put("June", "06");
        months.put("JUNE", "06");
        months.put("JUN", "06");
        months.put("Jun", "06");
        months.put("July", "07");
        months.put("JULY", "07");
        months.put("JU", "07");
        months.put("Jul", "07");
        months.put("August", "08");
        months.put("AUGUST", "08");
        months.put("AUG", "08");
        months.put("Aug", "08");
        months.put("September", "09");
        months.put("SEPTEMBER", "09");
        months.put("SEP", "09");
        months.put("Sep", "09");
        months.put("October", "10");
        months.put("OCTOBER", "10");
        months.put("OCT", "10");
        months.put("Oct", "10");
        months.put("November", "11");
        months.put("NOVEMBER", "11");
        months.put("NOV", "11");
        months.put("Nov", "11");
        months.put("December", "12");
        months.put("DECEMBER", "12");
        months.put("DEC", "12");
        months.put("Dec", "12");

    }

    private String[] seperatesToWords (String text){
        String[] arr = text.split("[ \n\t\"*&<>(){}+#@?!:`;^|=~]");
        return arr;
    }

    private boolean IsNumber (String str)
    {
        //check negative number
        if(str.length()>0&&str.charAt(0)=='-')
            str=str.substring(1,str.length());
        //check esroni number
        int dot=0;
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)=='.') dot++;
        }
        if(dot>1) return false;
        str=str.replace(".","");
        if(str.length()==0||str.equals(""))
            return false;
        if((str.length()>1&&str.charAt(str.length()-1)=='.')) return false;
        for(int i=0;i<str.length();i++){
            if(!Character.isDigit(str.charAt(i)))
                return false;
        }
        return true;
    }

    private boolean RemainEmpty(String s) {
        if(s.equals(""))
            return true;
            //all the single symbol will not enter.
            //all the single chars are stop words
        else if(s.length()==1&&!Character.isDigit(s.charAt(0)))
            return true;

        return false;
    }

    public Map<String,TokenInfo> getParsing(String text,String DocNo,boolean wantToSteam) {
        this.wantToSteam=wantToSteam;
        if(wantToSteam)
            stemmer=new Stemmer();

        Tokens.clear();
        this.SplitDoc = seperatesToWords(text);
        for (int i = 0; i < SplitDoc.length; i++) {
            if(SplitDoc[i].equals(""))continue;


            if(SplitDoc[i].contains(","))
                SplitDoc[i]=SplitDoc[i].replace(",","");
            if(SplitDoc[i].contains("["))
                SplitDoc[i]=SplitDoc[i].replace("[","");
            if(SplitDoc[i].contains("]"))
                SplitDoc[i]=SplitDoc[i].replace("]","");
            if(SplitDoc[i].contains("..."))
                SplitDoc[i]=SplitDoc[i].replace("...","");
            if(SplitDoc[i].length()>1&&SplitDoc[i].charAt(0)=='/')
                SplitDoc[i]=SplitDoc[i].substring(1,SplitDoc[i].length());

            LastChar(i);
            FirstChar(i);
            if(RemainEmpty(SplitDoc[i])) continue;

            String token0=SplitDoc[i];
            String token1="";
            String token2="";
            String token3="";
            if(i+1<SplitDoc.length) {
                LastChar(i+1);
                FirstChar(i+1);
                token1 = SplitDoc[i + 1];
            }
            if(i+2<SplitDoc.length) {
                LastChar(i+2);
                FirstChar(i+2);
                token2 = SplitDoc[i + 2];
            }
            if(i+3<SplitDoc.length) {
                LastChar(i+3);
                FirstChar(i+3);
                token3 = SplitDoc[i + 3];
            }

            if(token0.equals(".")||token0.equals("-")||token0.equals("--"))
                continue;

            token0=removeAll__shapes(token0);
            if(token0.equals(""))continue;



            //month->number
            if(months.containsKey(token0)) {
                if ((!token1.equals("")) && OnlyDigits(token1)) {
                    //day or year
                    i+=idDate(token0,token1,i);
                    continue;
                }

                else
                    justAdd(token0,i);
                continue;
            }

            if (IsNumber(token0)) {
                float valueOfT0 = Float.parseFloat(token0);
                //precent case
                if(token1.equals("percent")||token1.equals("percentage")){
                    add_To_tokens_map(token0+"%",i);
                    i++;
                    continue;
                }
                if(token1.equals("Dollars")){
                    if(token0.contains(".")) {
                        add_To_tokens_map(token0 + " Dollars",i);
                        i++;
                        continue;
                    }
                    if(valueOfT0<1000000) {
                        add_To_tokens_map(token0 + " Dollars",i);
                        i++;
                        continue;
                    }
                    else {
                        String tmp=String.valueOf(valueOfT0/1000000);
                        justAdd(tmp.substring(0,tmp.length()-2)+" M Dollars",i);
                        i++;
                        continue;
                    }
                }

                else if ((token1.equals("billion") || token1.equals("million") || token1.equals("trillion")) && token2.equals("U.S.") && token3.equals("dollars")) {
                    int kofel=1;
                    if(token1.equals("billion"))
                        kofel=1000;
                    else if(token1.equals("trillion"))
                        kofel=1000000;
                    String tmp=String.valueOf(valueOfT0*kofel);
                    justAdd(tmp.substring(0,tmp.length()-2)+" M Dollars",i);
                    i=i+3;
                    continue;
                } else if (token1.contains("-")) {
                    i += num_word_Makaf_word(token0,i);
                    continue;
                } else if (token1.equals("Thousand")) {
                    add_To_tokens_map(numberFormat(token0 + "000"),i);
                    i++;
                    continue;
                } else if (token1.equals("Million")) {
                    add_To_tokens_map(token0 + "M",i);
                    i++;
                    continue;
                } else if (token1.equals("Billion")) {
                    add_To_tokens_map(token0 + "B",i);
                    i++;
                    continue;
                } else if (token1.equals("Trillion")) {
                    add_To_tokens_map(token0 + "00B",i);
                    i++;
                    continue;
                } else if (months.containsKey(token1)) {  //14 May
                    if(OnlyDigits(token0))
                        i+=idDate(token1,token0,i);
                    else justAdd(token0,i);
                    continue;
                } else if (token1.equals("m") && token2.equals("Dollars")) {
                    add_To_tokens_map(token0 + " M" + " Dollars",i);
                    i += 2;
                    continue;
                } else if (token1.equals("bn") &&token2.equals("Dollars")) {
                    add_To_tokens_map(token0 + "000" + " M Dollars",i);
                    i += 2;
                    continue;
                } else if (token1.contains("/") && token2.equals("Dollars")) {
                    if (IsFraction(token1)) {
                        add_To_tokens_map(token0 + " " + token1 + " Dollars",i);
                        i += 2;
                        continue;
                    }
                } else if (token1.contains("/")) {
                    if (IsFraction(token1)) {
                        add_To_tokens_map(token0 + " " + token1,i);
                        i++;
                        continue;
                    }
                } else {   //regular number alone
                    add_To_tokens_map(numberFormat(token0),i);
                    continue;
                }
            }


            //check between phrase
            if ((token0.equals("Between") || token0.equals("between")) && i + 3 < SplitDoc.length && IsNumber(token1) && token2.equals("and") && IsNumber(token3)) {
                betweenHandle(i);
                i = i + 3;
                continue;
            }

            if (A__B_Shape(token0)) {//////our role
                String[] toadd = token0.split("--");
                justAdd(toadd[0],i);
                justAdd(toadd[1],i);
                continue;
            }


            if (token0.contains("-")) {
                i += makaf_Handle(i);
                continue;


            }

            else if (token0.length() - 1 > 0 && token0.charAt(token0.length() - 1) == '%') {
                if (token0.length() >= 2 && IsNumber(token0.substring(0, token0.length() - 2))) {
                    add_To_tokens_map(token0.replace("%", "") + "%",i);
                    continue;
                }

            } else if (token0.charAt(0) == '$') {
                String str = token0;
                str = str.replace("$", "");
                float value = 0;
                if (IsNumber(str)) {
                    value = Float.parseFloat(str);
                } else{
                    if(str.length()>1)
                        justAdd(str,i);
                    continue;
                }
                if (i + 1 < SplitDoc.length && (token1.equals("million")||token1.equals("Million"))) {
                    add_To_tokens_map(str + " M Dollars",i);
                    i++;
                    continue;
                } else if (i + 1 < SplitDoc.length && (token1.equals("billion")||token1.equals("Billion"))) {
                    String Tmp=String.valueOf(value * 1000);
                    add_To_tokens_map(Tmp.substring(0,Tmp.length()-2) + " M Dollars",i);
                    i++;
                    continue;
                } else if (value < 1000000&&value>0) {
                    add_To_tokens_map(str + " Dollars",i);
                    continue;
                }
                else {
                    String Tmp=String.valueOf(value / 1000000);
                    add_To_tokens_map( Tmp.substring(0,Tmp.length()-2)+ " M Dollars",i);
                    continue;
                }
            }
            if(cities.containsKey(token0.toUpperCase())) {
                updateCities(token0,DocNo,i);
                continue;
            }
            AddAndRemoveStopWords(token0,i);


        }

        return Tokens;
    }

    private void updateCities(String token0,String docNo,int i) {
        if(!cities.get(token0.toUpperCase()).isUpdate()) {
            String[] details = allJson.getDetails(token0);//make sure the letters are good
            cities.get(token0.toUpperCase()).setCountry(details[0]);
            cities.get(token0.toUpperCase()).setCurrency(details[2]);
            cities.get(token0.toUpperCase()).setPopulation(KMB(details[1]));
            cities.get(token0.toUpperCase()).setUpdate(true);
        }
        //check if this city already exist in this doc-add 1 to location
        cities.get(token0.toUpperCase()).docExistANdAdd(docNo,i+"");

        add_To_tokens_map(token0.toUpperCase(),i);
    }

    private String KMB(String detail) {
        if(detail.equals(""))
            return detail;
        return numberFormat(detail);
    }

    private String removeAll__shapes(String token0) {
        if (token0.length()>2&&token0.charAt(0)=='-'&&(!Character.isDigit(token0.charAt(1))))
            token0 = token0.substring(1, token0.length());
        if(token0.length()>2&&token0.charAt(0)=='-'&&token0.charAt(1)=='-')
            token0=token0.substring(2,token0.length());
        if(token0.length()>3&&token0.charAt(0)=='-'&&token0.charAt(1)=='-'&&token0.charAt(2)=='\'')
            token0=token0.substring(3,token0.length());

        return token0;
    }

    private boolean IsFraction(String possibleFraction) {
        String [] arr=possibleFraction.split("/");
        if(arr.length==2&&IsNumber(arr[0])&&IsNumber(arr[1]))
            return true;
        return false;
    }


    private int num_word_Makaf_word(String token,int i) {
        int num_Return = 0;
        String[] arr = SplitDoc[i + 1].split("-");
        if(arr.length==2) {
            String w1 = "";
            String w2 = arr[1];
            String firstNum = token;
            if (arr[0].equals("Thousand")) {
                firstNum = firstNum + " K";
                num_Return++;
            } else if (arr[0].equals("Million")) {
                firstNum += " M";
                num_Return++;

            } else if (arr[0].equals("Billion")) {
                firstNum += "B";
                num_Return++;

            } else if (arr[0].equals("Trillion")) {
                firstNum += "00B";
                num_Return++;

            }
            w1 = firstNum;
            if(IsFraction(arr[0])){//30 3/4-word
                w1+=" "+arr[0];
                num_Return++;

            }
            if ((IsNumber(arr[1])))  //55 million-60/ 55 million-60 million/
            {
                if (i + 2 < SplitDoc.length && SplitDoc[i + 2].equals("Thousand")) {
                    num_Return++;
                    w2 = w2 + " K";
                } else if (i + 2 < SplitDoc.length && SplitDoc[i + 2].equals("Million")) {
                    num_Return++;
                    w2 = w2 + " M";
                } else if (i + 2 < SplitDoc.length && SplitDoc[i + 2].equals("Billion")) {
                    num_Return++;
                    w2 = w2 + " B";
                } else if (i + 2 < SplitDoc.length && SplitDoc[i + 2].equals("Trillion")) {
                    num_Return++;
                    w2 = w2 + " 00B";
                }

                if(i + 2 < SplitDoc.length && IsFraction(SplitDoc[i+2])){
                    w2=w2+" "+SplitDoc[i+2];
                    num_Return++;
                }
            }
            add_To_tokens_map(w1 + "-" + w2,i);
        }
        return num_Return;
    }
    private void betweenHandle(int i) {
        String num1=SplitDoc[i+1];
        String num2=SplitDoc[i+3];
        add_To_tokens_map("between "+num1+" and "+num2,i);

    }

    private int makaf_Handle(int i) {
        int returnValue=0;
        String[] arr=SplitDoc[i].split("-");
        if(arr.length==3) {              //word-word-word
            justAdd(arr[0] + "-" + arr[1] + "-" + arr[2],i);
        }
        else if(arr.length==2) {                         //w-w / w-n / n-w / n-n
            String w1=""; String w2="";
            if(IsNumber(arr[0]))
                w1=numberFormat(arr[0]);
            else
                w1=arr[0];
            if(IsNumber(arr[1])) {
                if (i + 1 < SplitDoc.length && SplitDoc[i + 1].equals("Thousand")) {
                    arr[1] = arr[1] + "000";
                    returnValue++;
                    w2 = numberFormat(arr[1]);
                } else if (i + 1 < SplitDoc.length && SplitDoc[i + 1].equals("Million")) {
                    arr[1] = arr[1] + "000000";
                    returnValue++;
                    w2 = numberFormat(arr[1]);
                } else if (i + 1 < SplitDoc.length && SplitDoc[i + 1].equals("Billion")) {
                    arr[1] = arr[1] + "000000000";
                    returnValue++;
                    w2 = numberFormat(arr[1]);
                } else if (i + 1 < SplitDoc.length && SplitDoc[i + 1].equals("Trillion")) {
                    arr[1] = arr[1] + "000000000000";
                    returnValue++;
                    w2 = numberFormat(arr[1]);
                } else if (i + 1 < SplitDoc.length && IsFraction(SplitDoc[i + 1])) {
                    w2 = arr[1] + " " + SplitDoc[i + 1];
                    returnValue++;
                }
                else w2=arr[1];
            }
            //our role
            else if(arr[1].equals("percent")||arr[1].equals("percentage")){
                justAdd(w1+"%",i);
                returnValue++;
                return returnValue;
            }

            else
                w2=arr[1];
            justAdd(w1+"-"+w2,i);
        }
        return returnValue;
    }
    private boolean A__B_Shape(String str){
        String [] toadd=str.split("--");
        if(toadd.length!=2)
            return false;
        if((toadd[0].equals("")||toadd[1].equals("")))
            return false;
        return true;
    }
    private void LastChar(int i) {
        if(SplitDoc[i].length()>1) {
            boolean moreToRemove=true;
            while (moreToRemove&&SplitDoc[i].length()>1){
                if(SplitDoc[i].equals("U.S."))
                    moreToRemove=false;
                if(SplitDoc[i].charAt(SplitDoc[i].length() - 1)!='-'&&SplitDoc[i].charAt(SplitDoc[i].length() - 1)!='\''&&SplitDoc[i].charAt(SplitDoc[i].length() - 1)!='.')
                    moreToRemove=false;
                else
                    SplitDoc[i] = SplitDoc[i].substring(0, SplitDoc[i].length() - 1);
            }
        }
    }

    private void FirstChar(int i){
        if(SplitDoc[i].length()>1){
            boolean moreToRemove=true;
            while (moreToRemove){
                if(SplitDoc[i].charAt(0)!='\''&&SplitDoc[i].charAt(0)!='.')
                    moreToRemove=false;
                else
                    SplitDoc[i]=SplitDoc[i].substring(1,SplitDoc[i].length());
            }
        }
    }

    private int idDate(String token0, String token1,int i) {
        String dayYear = "";
        if (token1.length() == 1) {
            dayYear = "0" + token1;
            justAdd(months.get(token0) + "-" + dayYear,i);
            return 1;
        }
        if (token1.length() == 2) {
            dayYear = token1;
            justAdd(months.get(token0) + "-" + token1,i);
            return 1;
        }
        if (token1.length() == 4) {
            dayYear = token1;
            justAdd(token1 + "-" + months.get(token0),i);
            return 1;
        }
        return 0;
    }

    private boolean OnlyDigits(String token1) {
        for (int i = 0; i < token1.length(); i++) {
            if(!Character.isDigit(token1.charAt(i)))
                return false;
        }
        return true;
    }

    private void justAdd(String toAdd,int i) {
        if(toAdd.length()>2&&toAdd.charAt(0)=='-'&&(toAdd.charAt(1)=='\''||toAdd.charAt(1)=='-'||toAdd.charAt(1)=='/'||toAdd.charAt(1)=='%'||toAdd.charAt(1)=='$')) {
            toAdd = toAdd.substring(2, toAdd.length());
            if(Character.isUpperCase(toAdd.charAt(0)))
                toAdd=toAdd.toUpperCase();
        }
        if(toAdd.length()>1&&toAdd.charAt(0)=='\'') {
            toAdd = toAdd.substring(1, toAdd.length());
            if(Character.isUpperCase(toAdd.charAt(0)))
                toAdd=toAdd.toUpperCase();
        }
        if(Character.isUpperCase(toAdd.charAt(0)))
            toAdd=toAdd.toUpperCase();
        add_To_tokens_map(toAdd,i);
    }

    private void add_To_tokens_map(String add,int i) {
        try {
            if (add.equals("")) return;
            if (wantToSteam) {//user chosen stem option
                Stemmer stm = new Stemmer();
                char[] w = new char[50];
                int ch = add.charAt(0);
                if (Character.isLetter((char) ch)) {
                    int j = 0;
                    while (j < add.length()) {
                        ch = add.charAt(j);
                        ch = Character.toLowerCase((char) ch);
                        w[j] = (char) ch;
                        if (j < 50) j++;
                    }
                    for (int k = 0; k < add.length(); k++)
                        stm.add(w[k]);

                    stm.stem();
                    add = stm.toString();
                }
            }

            if (Tokens.containsKey(add))
               Tokens.get(add).setFrequentInDoc(Tokens.get(add).frequentInDoc+1);
            else
                Tokens.put(add,new TokenInfo(1,atStart(i)));
        }
        catch (IndexOutOfBoundsException e){

        }
    }

    private boolean atStart(int i) {
        if(i<SplitDoc.length/5)
            return true;
        return false;
    }


    private void AddAndRemoveStopWords(String toAdd,int i) {
        if(IsStopWord(toAdd)) return;
        if(IsSymbol(toAdd)) return;
        if(Character.isUpperCase(toAdd.charAt(0)))
            toAdd=toAdd.toUpperCase();
        add_To_tokens_map(toAdd,i);
    }

    private boolean IsSymbol(String toAdd) {
        if(toAdd.contains("%")||toAdd.contains("$")||toAdd.contains("-")||toAdd.contains(".")||toAdd.contains(","))
            return true;
        return false;
    }

    private boolean IsStopWord(String toAdd) {
        String StopToBe=toAdd.toLowerCase();
        if(stop_words.contains(StopToBe))
            return true;
        return false;
    }

    public String numberFormat(String number) {
        String str = number.replace(",", "");
        str = str.replace("/", "");
        String ans="";
        if (isNumeric(str)) {
            float value = Float.parseFloat(str);
            value=(int)Math.round(value * 100)/(float)100;
            if (value < 1000)
                return number;
            else if (value >= 1000 && value < 1000000) {
                value = value / 1000;
                value=(int)Math.round(value * 100)/(float)100;
                ans = String.valueOf(value);
                ans=ans + " K";
                return ans;
            } else if (value >= 1000000 && value < 1000000000) {
                value=value / 1000000;
                value=(int)Math.round(value * 100)/(float)100;
                ans = String.valueOf(value);
                ans = ans + " M";
                return ans;
            }
            value=value / 1000000000;
            value=(int)Math.round(value * 100)/(float)100;
            ans=String.valueOf(value);

            return ans + " B";

        }
        return "";
    }


    public boolean isNumeric(String str)
    {
        if(str.charAt(0)=='-')
            str=str.replace("-","");
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)&&(c!='.')) return false;
        }
        return true;
    }



}