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
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador de la ventana de activar o desactivar aviones
 */
public class ActivarDesactivarAvionController implements Initializable {
    /**
     * ComboBox de aeropuerto
     */
    @FXML
    private ComboBox<Aeropuerto> cbAeropuerto;

    /**
     * ComboBox de aviones
     */
    @FXML
    private ComboBox<Avion> cbAvion;

    /**
     * RadioButton de activado
     */
    @FXML
    private RadioButton rbActivado;

    /**
     * RadioButton de desactivado
     */
    @FXML
    private RadioButton rbDesactivado;

    /**
     * Función que se ejecuta cuando se carga la ventana
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
        cbAvion.valueProperty().addListener((_, _, newValue) -> cambioAvion(newValue));
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
     * Cambia el estado de los radiobuttons cuando un avión es seleccionado
     *
     * @param avion nuevo avión seleccionado
     */
    public void cambioAvion(Avion avion) {
        if (avion != null) {
            boolean activado = avion.isActivado();
            if (activado) {
                rbActivado.setSelected(true);
                rbDesactivado.setSelected(false);
            } else {
                rbActivado.setSelected(false);
                rbDesactivado.setSelected(true);
            }
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
     * Actualiza el avión
     */
    @FXML
    void guardar() {
        boolean activado = rbActivado.isSelected();
        Avion avion = cbAvion.getSelectionModel().getSelectedItem();
        Avion avionNuevo = new Avion(avion.getId(),avion.getModelo(),avion.getNumero_asientos(),avion.getVelocidad_maxima(),activado,avion.getAeropuerto());
        boolean resultado = DaoAvion.modificar(avion, avionNuevo);
        if (resultado) {
            confirmacion("Avión modificado correctamente");
            Stage stage = (Stage)cbAeropuerto.getScene().getWindow();
            stage.close();
        } else {
            alerta("Ha habido un error actualizando el avión. Por favor intentalo de nuevo");
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
