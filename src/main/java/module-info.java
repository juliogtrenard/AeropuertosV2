module es.juliogtrenard.aeropuertos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens es.juliogtrenard.aeropuertos to javafx.fxml;
    exports es.juliogtrenard.aeropuertos;
    exports es.juliogtrenard.aeropuertos.controladores;
    opens es.juliogtrenard.aeropuertos.controladores to javafx.fxml;
    exports es.juliogtrenard.aeropuertos.modelos;
    opens es.juliogtrenard.aeropuertos.modelos to javafx.fxml;
}