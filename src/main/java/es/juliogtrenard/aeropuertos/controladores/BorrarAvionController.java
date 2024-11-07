package es.juliogtrenard.aeropuertos.controladores;

import es.juliogtrenard.aeropuertos.dao.DaoAeropuerto;
import es.juliogtrenard.aeropuertos.dao.DaoAvion;
import es.juliogtrenard.aeropuertos.modelos.Aeropuerto;
import es.juliogtrenard.aeropuertos.modelos.Avion;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador de la ventana de borrar aviones
 */
public class BorrarAvionController implements Initializable {
    /**
     * ComboBox de aeropuertos
     */
    @FXML
    private ComboBox<Aeropuerto> cbAeropuerto;

    /**
     * ComboBox de aviones
     */
    @FXML
    private ComboBox<Avion> cbAvion;

    /**
     * Se ejecuta cuando se carga la ventana
     *
     * @param url la url
     * @param resourceBundle los recursos
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Aeropuerto> aeropuertos = DaoAeropuerto.cargarListado();
        cbAeropuerto.setItems(aeropuertos);
        cbAeropuerto.getSelectionModel().select(0);
        cbAeropuerto.valueProperty().addListener((_, _, newValue) -> cambioAeropuerto(newValue));
        cambioAeropuerto(cbAeropuerto.getSelectionModel().getSelectedItem());
    }

    /**
     * Carga los aviones de un aeropuerto cuando este se cambia
     *
     * @param aeropuerto nuevo aeropuerto seleccionado
     */
    public void cambioAeropuerto(Aeropuerto aeropuerto) {
        if (aeropuerto != null) {
            ObservableList<Avion> aviones = DaoAvion.cargarListado(aeropuerto);
            cbAvion.setItems(aviones);
            cbAvion.getSelectionModel().select(0);
        }
    }

    /**
     * Cierra la ventana
     */
    @FXML
    void cancelar() {
        Stage stage = (Stage)cbAeropuerto.getScene().getWindow();
        stage.close();
    }

    /**
     * Elimina el avión
     */
    @FXML
    void guardar() {
        Avion avion = cbAvion.getSelectionModel().getSelectedItem();
        boolean resultado = DaoAvion.eliminar(avion);
        if (resultado) {
            confirmacion("Avión eliminado correctamente");
            Stage stage = (Stage)cbAeropuerto.getScene().getWindow();
            stage.close();
        } else {
            alerta("Ha habido un error eliminado el avión. Por favor inténtelo de nuevo");
        }
    }

    /**
     * Función que muestra un mensaje de alerta al usuario
     *
     * @param texto contenido de la alerta
     */
    public void alerta(String texto) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle("ERROR");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }

    /**
     * Función que muestra un mensaje de confirmación al usuario
     *
     * @param texto contenido del mensaje
     */
    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }

}
