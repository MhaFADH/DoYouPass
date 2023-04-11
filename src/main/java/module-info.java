module com.doyoupass.doyoupass {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;


    opens com.doyoupass.doyoupass to javafx.fxml;
    exports com.doyoupass.doyoupass;
}