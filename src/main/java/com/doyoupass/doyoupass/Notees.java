package com.doyoupass.doyoupass;

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




    @Override
    public String toString() {
        return "Notees{" +
                "coef='" + coef + '\'' +
                ", matiere='" + matiere + '\'' +
                ", notes=" + notes +
                '}';
    }
}
