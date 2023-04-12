package com.doyoupass.doyoupass;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class VerifTools {

    Tools tools;
    Document noteHmtl;
    Document fiche;
    static String absences;
    static List<Notees> grds = new ArrayList<>() ;
    static double moyGene;


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

        absences = hSum+"h"+mSum+"m";

        System.out.println(absences);

        if(hSum>=100){
            return false;
        }else{
            return true;
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
        Elements notes = noteHmtl.getElementsByClass("info");
        Element stgTr = notes.last();
        String note = stgTr.child(3).text();

        if(note.equals("")){
            return "En attente";
        }else if(note.contains("ABSENT") | note.contains("EXEMPTE")){
            return "En attente";
        }else if(Double.parseDouble(note)>= 12){
            return "OK!";
        }else{
            return "NO!";
        }


    }

    public double moyG(){
        List<Double> notestb;
        String matiere;
        String coef;
        List<Element> array = new ArrayList<>();

        int counter = 0;
        double sum = 0;
        double moyG = 0;

        Elements tr = noteHmtl.getElementsByTag("tr");

        for (Element ele:tr) {
            if(ele.hasClass("info") | ele.hasClass("note_devoir"))
                array.add(ele);
        }

        long nbele = array.stream().count();

        for (int i=0; i<nbele;i++){
            notestb = new ArrayList<>();
            Element act = array.get(i);
            if(act.hasClass("info")){
                for(int j = i+1;j<nbele;j++){
                    if(array.get(j).hasClass("note_devoir")){
                        String note = array.get(j).child(3).text();
                        if(note.contains("EXEMPTE") | note.contains("ABSENT")){
                            continue;
                        }
                        notestb.add(Double.parseDouble(note));

                    }else{
                        break;
                    }
                }
                Notees subject = new Notees(act.child(0).text(),Double.parseDouble(act.child(1).text()),notestb);
                grds.add(subject);
            }
        }


        for (Notees subj:grds) {
            if(subj.moyenneSubj()==null){
            }else{
                counter += subj.coef;
                sum += subj.moyenneSubj();
            }
        }
        return (Math.round((sum/counter)*100.0)/100.0);
    }


}
