package com.doyoupass.doyoupass;
import javafx.scene.control.TableColumn;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
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


    public void orgaNotes(TableView tableauNotes, TableColumn mat, TableColumn coef, TableColumn note, List<Notees> grds) throws IOException {

        mat.setCellValueFactory(new PropertyValueFactory<>("matiere"));

        coef.setCellValueFactory(new PropertyValueFactory<>("coef"));

        note.setCellValueFactory(new PropertyValueFactory<>("notes"));


        for (Notees subj:grds) {
            tableauNotes.getItems().add(subj);
        }

    }

    Runnable keepSession = new Runnable() {
        public void run() {
            try {
                Connection.Response page = Jsoup.connect("https://www.pepal.eu/interaction.php")
                        .method(Connection.Method.POST)
                        .cookies(cookie)
                        .execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    };


}
