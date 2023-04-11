package com.doyoupass.doyoupass;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.doyoupass.doyoupass.LoginController.cookie;

public class VerifTools {

    Tools tools;
    Document noteHmtl;
    Document fiche;
    String regex = "\\d+";
    static String absences;

    public VerifTools() throws IOException {
        this.tools = new Tools();
        this.noteHmtl = tools.getHtml("https://www.pepal.eu/?my=notes");
        this.fiche = tools.getHtml("https://www.pepal.eu/?my=file");

    }

    public boolean isUnderAHundred() throws IOException, ParseException {
        int hSum = 0;
        int mSum = 0;
        Elements tdClass = fiche.getElementsByTag("tBody");
        Elements tds = tdClass.first().children();
        Element td = tdClass.first();
        String abs = td.child(0).child(1).text();
        String ret = td.child(1).child(1).text();
        String [] absSplit = abs.split("h");
        String [] retSplit = ret.split("h");
        hSum += Integer.parseInt(absSplit[0])+Integer.parseInt(retSplit[0]);
        mSum += Integer.parseInt(absSplit[1])+Integer.parseInt(retSplit[1]);
        if(mSum > 59){
            int counter = 0;
            for(int i=0;mSum>59;i++){
                counter++;
                mSum -= 60;
            }
            hSum += counter;
        }

        absences = hSum+"h"+mSum;

        System.out.println(absences);

        if(hSum>=100){
            return true;
        }else{
            return false;
        }
    }
    public boolean isUnderFive(){

        Elements trClass = noteHmtl.getElementsByClass("note_devoir");

        for(Element ele:trClass){
            String str = ele.child(3).text();

            if(ele.child(0).text().contains("Respect des Normes")){
            } else if(str.contains("ABSENT") | str.contains("EXEMPTE")){}else{
                if(Double.parseDouble(str)<5){
                    System.out.println(Double.parseDouble(str));
                    System.out.println("true");
                    return true;
                };
            }

        }
        System.out.println("false");
        return false;
    }

    public String stageState(){
        return "";
    }


}
