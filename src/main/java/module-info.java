module com.doyoupass.doyoupass {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;
    requires java.sql;


    opens com.doyoupass.doyoupass to javafx.fxml;
    exports com.doyoupass.doyoupass;
}