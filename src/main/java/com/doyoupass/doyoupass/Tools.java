package com.doyoupass.doyoupass;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.doyoupass.doyoupass.LoginController.*;

public class Tools {

    public HashMap<String,String> connectPepal(String username,String password) {
        try {

            HashMap<String,String> formData = new HashMap<>();

            formData.put("login", username);
            formData.put("pass", password);

            Connection.Response response = Jsoup.connect("https://www.pepal.eu/include/php/ident.php")
                    .data(formData)
                    .method(Connection.Method.POST)
                    .execute();

            HashMap<String,String> cookie = new HashMap<>(response.cookies());

            Elements doc = response.parse().getElementsByTag("p");


            if (doc.text().contains("Redirection dans : ")) {
                return cookie;
            } else {
                return null;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Document getHtml(String url) throws IOException {
        Connection.Response page = Jsoup.connect(url)
                .method(Connection.Method.GET)
                .cookies(cookie)
                .execute();

        return page.parse();

    }


    public void orgaNotes(TextArea noteField, TextField moyField, float sumNotes, float moyenne) throws IOException {
        Document noteHtml = null;
        sumNotes = 0;
        moyenne = 0;

        List<String> tabloNotes = new ArrayList<>();

        noteHtml = getHtml("https://www.pepal.eu/?my=notes");


        Elements trClass = noteHtml.getElementsByClass("note_devoir");

        for(Element ele:trClass){
            String note = ele.child(3).text();
            tabloNotes.add(ele.child(0).text().replace(" PUBLIE","")+":  "+ note);
        }

        moyenne = sumNotes / tabloNotes.size();


        for(String ele:tabloNotes){

        }


    }

    Runnable keepSession = new Runnable() {
        public void run() {
            try {
                Connection.Response page = Jsoup.connect("https://www.pepal.eu/interaction.php")
                        .method(Connection.Method.POST)
                        .cookies(cookie)
                        .execute();
                System.out.println("tried");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    };


}
