package com.doyoupass.doyoupass;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.doyoupass.doyoupass.LoginController.*;
import static com.doyoupass.doyoupass.VerifTools.*;


public class MainScreenController implements Initializable {
    Tools tools = new Tools();
    VerifTools vTools = new VerifTools();
    public final String msg = "Vous avez déjà été noté présent, ou alors l'appel a été clôturé" ;
    public final String msg1 = "l'appel a été clôturé" ;
    public final String msg2 = "l'appel n'a pas été encore ouvert" ;
    public final String msg3 = "Pas de cours prévu pour aujourd'hui" ;
    public static float sumNotes;
    public static float moyenne;
    public static HashMap<String,List<Double>> moyList;

    private Main mainapp;


    @FXML
    private Button buttonPres;
    @FXML
    private TextField absField;
    @FXML
    private TextField moyField;
    @FXML
    private TableView tableauNotes;
    @FXML
    private TableColumn mat;
    @FXML
    private TableColumn coef;
    @FXML
    private TableColumn note;
    @FXML
    private Pane homePane;
    @FXML
    private Pane passPane;
    @FXML
    private Pane evolPane;
    @FXML
    private TextField rapportStg;
    @FXML
    private TextField underFive;
    @FXML
    private TextField higherTen;
    @FXML
    private TextField underHundred;
    @FXML
    private TextField isPassing;
    @FXML
    private LineChart<?,?> lineCh;
    @FXML
    private NumberAxis y;
    @FXML
    private CategoryAxis x;
    @FXML
    private ImageView yes5;
    @FXML
    private ImageView no5;
    @FXML
    private ImageView yes10;
    @FXML
    private ImageView no10;
    @FXML
    private ImageView yes100;
    @FXML
    private ImageView no100;
    @FXML
    private ImageView yes12;
    @FXML
    private ImageView no12;
    @FXML
    private ImageView pend12;
    @FXML
    private ImageView yesRes;
    @FXML
    private ImageView noRes;
    @FXML
    private ImageView pendRes;


    public MainScreenController() throws IOException {
    }
    public void homeB(ActionEvent e) throws IOException {
        homePane.setVisible(true);
        passPane.setVisible(false);
        evolPane.setVisible(false);

    }
    public void evolB(ActionEvent e) throws IOException {
        homePane.setVisible(false);
        passPane.setVisible(false);
        evolPane.setVisible(true);

    }
    public void passB(ActionEvent e) throws IOException {
        homePane.setVisible(false);
        passPane.setVisible(true);
        evolPane.setVisible(false);

    }



    public void pres(ActionEvent e) throws IOException {

        absField.setText(absences);

        Document presPage;

        try {
            presPage = tools.getHtml("https://www.pepal.eu/presences");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Elements success = presPage.getElementsByClass("alert alert-success");

        Elements hrefPres = presPage.getElementsByClass("btn btn-primary");
        Element []hrefLinks = new Element[hrefPres.size()];

        for (int i = 0;i<=hrefPres.size()-1;i++){
            hrefLinks[i] = hrefPres.get(i);
        }

        Elements noCours = presPage.getElementsByClass("alert alert-danger");
        String matin = "";
        String aprem = "";

        if(noCours.text().contains("Pas de cours de prévu")){
        }else{
            matin = hrefLinks[0].attr("href").replace("/presences/s/","");
            aprem = hrefLinks[1].attr("href").replace("/presences/s/","");
        }


        HashMap<String,String> setPresToken = new HashMap<>();
        setPresToken.put("act","set_present");

        HashMap<String,String> matinMap = new HashMap<>();
        matinMap.put("seance_pk",matin);

        HashMap<String,String> apremMap = new HashMap<>();
        apremMap.put("seance_pk",aprem);

        Document matinPres;
        Document apremPres;
        Document pasCours;

        try {
            matinPres = tools.getHtml("https://www.pepal.eu/presences/s/"+matin);
            apremPres = tools.getHtml("https://www.pepal.eu/presences/s/"+aprem);
            pasCours = tools.getHtml("https://www.pepal.eu/presences");

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Elements matinResult = matinPres.getElementsByClass("alert alert-success");
        Elements matinResult1 = matinPres.getElementsByClass("alert alert-danger");

        Elements apremResult = apremPres.getElementsByClass("alert alert-success");
        Elements apremResult1 = apremPres.getElementsByClass("alert alert-danger");

        Elements pacC = pasCours.getElementsByClass("alert alert-danger");

        SimpleDateFormat sdf =new SimpleDateFormat("HH:mm");

        Date limitTime;
        try {
            limitTime = sdf.parse("12:00");
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        Date currentDate = new Date();
        if(currentDate.getHours() > limitTime.getHours()){
            if(apremResult.text().contains("Vous avez été noté présent le")){
                alert(msg);
            }else if(apremResult1.text().contains("L'appel est clôturé")){
                alert(msg1);
            }else if(apremResult1.text().contains(" L'appel n'est pas encore ouvert")){
                alert(msg2);
            }else if(pacC.text().contains("Pas de cours de prévu")){
                alert(msg3);
            }else{
                try {
                    Connection.Response prespge = Jsoup.connect("https://www.pepal.eu/student/upload.php")
                            .method(Connection.Method.POST)
                            .cookies(cookie)
                            .data(setPresToken)
                            .data(apremMap)
                            .execute();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }else{
            if(matinResult.text().contains("Vous avez été noté présent le")){
                alert(msg);
            }else if(matinResult1.text().contains("L'appel est clôturé")){
                alert(msg1);
            }else if(matinResult1.text().contains(" L'appel n'est pas encore ouvert")){
                alert(msg2);
            }else if(pacC.text().contains("Pas de cours de prévu")){
                alert(msg3);
            }else{
                try {
                    Connection.Response prespge = Jsoup.connect("https://www.pepal.eu/student/upload.php")
                            .method(Connection.Method.POST)
                            .cookies(cookie)
                            .data(setPresToken)
                            .data(matinMap)
                            .execute();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }


    }

    public void alert(String msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Validation failed");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            vTools.isUnderAHundred();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        moyGene = vTools.moyG();
        moyField.setText(moyGene+"");
        absField.setText(absences);

        try {
            tools.orgaNotes(tableauNotes,mat,coef,note,grds);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(tools.keepSession, 0, 60, TimeUnit.SECONDS);

        int eta = 0;
        String exception = "";
        String stgRes = vTools.stageState();

        switch (stgRes){
            case "OK!":
                yes12.setVisible(true);
                eta++;
                break;
            case "NO!":
                no12.setVisible(true);
                break;
            default:
                pend12.setVisible(true);
                exception = "Exc";
                break;
        }

        if(vTools.isUnderFive()){
            no5.setVisible(true);
        }else {
            yes5.setVisible(true);
            eta++;
        }

        if(vTools.moyG()>=10){
            yes10.setVisible(true);
            eta++;
        }else{
            no10.setVisible(true);
        }

        try {
            if(vTools.isUnderAHundred()){
                yes100.setVisible(true);
                eta++;
            }else{
                no100.setVisible(true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (eta == 4){
            yesRes.setVisible(true);
        }else if (moyGene<10 | vTools.isUnderFive()){
            noRes.setVisible(true);
        } else if (exception.contains("Exc")) {
            pendRes.setVisible(true);
        }else{
            noRes.setVisible(true);
        }

        String usernumber = username+"NB";

        moyList = new HashMap<>();

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://38.242.229.23:3306/doyoupass","root","password");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM willyoupass where name='%s'",username));

            while(resultSet.next()){
                if(moyList.containsKey(username)){
                    moyList.get(username).add(resultSet.getDouble(2));

                }else{
                    List<Double> init = new ArrayList<>();
                    init.add(resultSet.getDouble(2));
                    moyList.put(resultSet.getString(1),init);
                }
            }

            ResultSet resultSet1 = statement.executeQuery(String.format("SELECT * FROM willyoupass where name='%s'",usernumber));

            HashMap<String,Double> oldNbr = new HashMap<>();

            while(resultSet1.next()){
                oldNbr.put(resultSet1.getString(1),resultSet1.getDouble(2));
            }

            String imp = "INSERT INTO willyoupass VALUES (?,?)" ;
            PreparedStatement prest = connection.prepareStatement(imp);

            if(moyList.containsKey(username) && oldNbr.containsKey(usernumber)){

                if(oldNbr.get(usernumber) != nbele){
                    String imp1 = "UPDATE willyoupass SET moy=? WHERE name=?";
                    PreparedStatement prep = connection.prepareStatement(imp1);
                    prep.setDouble(1,nbele);
                    prep.setString(2,usernumber);
                    prep.executeUpdate();
                    prest.setString(1,username);
                    prest.setDouble(2,moyGene);
                    prest.executeUpdate();
                    System.out.println("test");

                }

                XYChart.Series series = new XYChart.Series();
                int counter = 1;
                series.setName("Moyenne générale");
                for (Double moy:moyList.get(username)) {
                    series.getData().add(new XYChart.Data(counter+"",moy));
                    counter++;
                };

                lineCh.getData().addAll(series);

            }else if (moyList.isEmpty() && oldNbr.isEmpty()){
                prest.setString(1,username);
                prest.setDouble(2,moyGene);
                prest.executeUpdate();
                prest.setString(1,usernumber);
                prest.setDouble(2,nbele);
                prest.executeUpdate();


            }else if(moyList.containsKey(username)){
                prest.setString(1,usernumber);
                prest.setDouble(2,nbele);
                prest.executeUpdate();
            }else if(oldNbr.containsKey(usernumber)){
                prest.setString(1,username);
                prest.setDouble(2,moyGene);
                prest.executeUpdate();
            }

            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }






    }
    public void setMainApp(Main mainapp) {
        this.mainapp = mainapp;
    }
}
