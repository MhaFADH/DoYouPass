package com.doyoupass.doyoupass;

import java.util.ArrayList;
import java.util.List;

public class Notees {
    public Double coef;
    public String matiere;
    public List<Double> notes;

    public Notees(String matiere, Double coef, List<Double>notes){
        this.coef = coef;
        this.matiere = matiere;
        this.notes = notes;
    }

    public Double moyenneSubj(){
        int counter = 0;
        double sum = 0;
        double moy = 0;
        if(notes.isEmpty()){
            return null;
        }else{
            for (Double note:notes) {
                sum += (note*coef);
                counter ++;
            }
            moy = sum/counter;
        }
        return moy;
    }

    public Double getCoef() {
        return coef;
    }

    public void setCoef(Double coef) {
        this.coef = coef;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public String getNotes() {
        String sNotes = "";
        if(notes.isEmpty()){
            return "";
        }else if(notes.size()<2){
            return notes.get(0)+"";
        }else{
            for (Double note:notes) {
                sNotes += note+", ";
            }
            return sNotes;
        }

    }

    public void setNotes(List<Double> notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Notees{" +
                "coef='" + coef + '\'' +
                ", matiere='" + matiere + '\'' +
                ", notes=" + notes +
                '}';
    }
}
