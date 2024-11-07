package es.juliogtrenard.aeropuertos.controladores;

import es.juliogtrenard.aeropuertos.dao.DaoAeropuerto;
import es.juliogtrenard.aeropuertos.dao.DaoAeropuertoPrivado;
import es.juliogtrenard.aeropuertos.dao.DaoAeropuertoPublico;
import es.juliogtrenard.aeropuertos.dao.DaoDireccion;
import es.juliogtrenard.aeropuertos.modelos.Aeropuerto;
import es.juliogtrenard.aeropuertos.modelos.AeropuertoPrivado;
import es.juliogtrenard.aeropuertos.modelos.AeropuertoPublico;
import es.juliogtrenard.aeropuertos.modelos.Direccion;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador de la ventana datos de aeropuerto
 */
public class DatosAeropuertoController implements Initializable {
    /**
     * Aeropuerto
     */
    private Object aeropuerto;

    /**
     * Objeto aeropuerto
     */
    private Aeropuerto aeropuerto_obj;

    /**
     * Label para introducir un parametro
     */
    @FXML
    private Label lblParam1;

    /**
     * Label para introducir un parametro
     */
    @FXML
    private Label lblParam2;

    /**
     * RadioButton privado
     */
    @FXML
    private RadioButton rbPrivado;

    /**
     * RadioButton publico
     */
    @FXML
    private RadioButton rbPublico;

    /**
     * ToggleGroup para el tipo de aeropuerto
     */
    @FXML
    private ToggleGroup rbTipo;

    /**
     * Campo de texto para introducir año de inauguracion
     */
    @FXML
    private TextField txtAnioInauguracion;

    /**
     * Campo de texto para introducir calle
     */
    @FXML
    private TextField txtCalle;

    /**
     * Campo de texto para introducir capacidad
     */
    @FXML
    private TextField txtCapacidad;

    /**
     * Campo de texto para introducir ciudad
     */
    @FXML
    private TextField txtCiudad;

    /**
     * Campo de texto para introducir nombre
     */
    @FXML
    private TextField txtNombre;

    /**
     * Campo de texto para introducir numero
     */
    @FXML
    private TextField txtNumero;

    /**
     * Campo de texto para introducir pais
     */
    @FXML
    private TextField txtPais;

    /**
     * Campo de texto
     */
    @FXML
    private TextField txtParam1;

    /**
     * Campo de texto
     */
    @FXML
    private TextField txtParam2;

    /**
     * Constructor que define el aeropuerto a editar (si aplicable)
     *
     * @param aeropuerto a editar
     */
    public DatosAeropuertoController(Object aeropuerto) {
        this.aeropuerto = aeropuerto;
    }

    /**
     * Constructor para añadir un nuevo aeropuerto
     */
    public DatosAeropuertoController() {
        this.aeropuerto = null;
    }

    /**
     * Setter para el aeropuerto a editar (si aplicable)
     *
     * @param aeropuerto a editar
     */
    public void setAeropuerto(Object aeropuerto) {
        this.aeropuerto = aeropuerto;
    }

    /**
     * Función que se ejecuta cuando se inicia la ventana
     *
     * @param url la url
     * @param resourceBundle los recursos
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rbTipo.selectedToggleProperty().addListener(this::cambioTipo);
        if (aeropuerto == null) {
            // Null -> Añadir Aeropuerto
            System.out.println("Null");
        } else {
            // Not Null -> Editar Aeropuerto
            System.out.println("Not Null");
            Aeropuerto airport;
            if (aeropuerto instanceof AeropuertoPublico aeropuertoPublico) {
                airport = aeropuertoPublico.getAeropuerto();
                txtParam1.setText(aeropuertoPublico.getFinanciacion().toString());
                txtParam2.setText(aeropuertoPublico.getNum_trabajadores() + "");
            } else {
                AeropuertoPrivado aeropuertoPrivado = (AeropuertoPrivado) aeropuerto;
                airport = aeropuertoPrivado.getAeropuerto();
                rbTipo.selectToggle(rbPrivado);
                txtParam1.setText(aeropuertoPrivado.getNumero_socios() + "");
            }
            this.aeropuerto_obj = airport;
            rbPublico.setDisable(true);
            rbPrivado.setDisable(true);

            txtNombre.setText(airport.getNombre());
            txtPais.setText(airport.getDireccion().getPais());
            txtCiudad.setText(airport.getDireccion().getCiudad());
            txtCalle.setText(airport.getDireccion().getCalle());
            txtNumero.setText(airport.getDireccion().getNumero() + "");
            txtAnioInauguracion.setText(airport.getAnio_inauguracion() + "");
            txtCapacidad.setText(airport.getCapacidad() + "");
        }
    }

    /**
     * Valida y procesa los datos del aeropuerto
     *
     * @param event el evento
     */
    @FXML
    void guardar(ActionEvent event) {
        String error = "";
        if (txtNombre.getText().isEmpty()) {
            error = "Campo nombre no puede estar vacío";
        }
        if (txtPais.getText().isEmpty()) {
            if (!error.isEmpty()) {
                error += "\n";
            }
            error += "Campo país no puede estar vacío";
        }
        if (txtCiudad.getText().isEmpty()) {
            if (!error.isEmpty()) {
                error += "\n";
            }
            error += "Campo ciudad no puede estar vacío";
        }
        if (txtCalle.getText().isEmpty()) {
            if (!error.isEmpty()) {
                error += "\n";
            }
            error += "Campo calle no puede estar vacío";
        }
        if (txtNumero.getText().isEmpty()) {
            if (!error.isEmpty()) {
                error += "\n";
            }
            error += "Campo número no puede estar vacío";
        } else {
            try {
                int numero = Integer.parseInt(txtNumero.getText());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                if (!error.isEmpty()) {
                    error += "\n";
                }
                error += "Campo número tiene que ser numérico";
            }
        }
        if (txtAnioInauguracion.getText().isEmpty()) {
            if (!error.isEmpty()) {
                error += "\n";
            }
            error += "Campo año de inauguración no puede estar vacío";
        } else {
            try {
                int anio_inauguracion = Integer.parseInt(txtAnioInauguracion.getText());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                if (!error.isEmpty()) {
                    error += "\n";
                }
                error += "Campo año de inauguración tiene que ser numérico";
            }
        }
        if (txtCapacidad.getText().isEmpty()) {
            if (!error.isEmpty()) {
                error += "\n";
            }
            error += "Campo capacidad no puede estar vacío";
        } else {
            try {
                int capacidad = Integer.parseInt(txtCapacidad.getText());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                if (!error.isEmpty()) {
                    error += "\n";
                }
                error += "Campo capacidad tiene que ser numérico";
            }
        }
        if (rbPublico.isSelected()) {
            // Aeropuerto Público
            if (txtParam1.getText().isEmpty()) {
                if (!error.isEmpty()) {
                    error += "\n";
                }
                error += "Campo financiación no puede estar vacío";
            } else {
                if (!txtParam1.getText().matches("^-?[0-9]+([\\.,][0-9]+)?$")) {
                    if (!error.isEmpty()) {
                        error += "\n";
                    }
                    error += "Campo financiación tiene que ser decimal";
                }
            }
            if (txtParam2.getText().isEmpty()) {
                if (!error.isEmpty()) {
                    error += "\n";
                }
                error += "Campo número de trabajadores no puede estar vacío";
            } else {
                try {
                    int capacidad = Integer.parseInt(txtParam2.getText());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    if (!error.isEmpty()) {
                        error += "\n";
                    }
                    error += "Campo número de trabajadores tiene que ser numérico";
                }
            }
        } else {
            // Aeropuerto Privado
            if (txtParam1.getText().isEmpty()) {
                if (!error.isEmpty()) {
                    error += "\n";
                }
                error += "Campo número de socios no puede estar vacío";
            } else {
                try {
                    int capacidad = Integer.parseInt(txtParam1.getText());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    if (!error.isEmpty()) {
                        error += "\n";
                    }
                    error += "Campo número de socios tiene que ser numérico";
                }
            }
        }
        // Fin verificación de datos
        if (!error.isEmpty()) {
            alerta(error);
        } else {
            // Correcto
            boolean resultado;
            if (this.aeropuerto == null) {
                resultado = crearAeropuerto();
            } else {
                resultado = modificarAeropuerto();
            }
            if (resultado) {
                Stage stage = (Stage)txtNombre.getScene().getWindow();
                stage.close();
            }
        }
    }

    /**
     * Crea un aeropuerto nuevo en la base de datos
     *
     * @return true/false
     */
    public boolean crearAeropuerto() {
        Direccion direccion = new Direccion();
        direccion.setPais(txtPais.getText());
        direccion.setCiudad(txtCiudad.getText());
        direccion.setCalle(txtCalle.getText());
        direccion.setNumero(Integer.parseInt(txtNumero.getText()));
        int id_direccion = DaoDireccion.insertar(direccion);
        if (id_direccion == -1) {
            alerta("Ha habido un error almacenando los datos. Por favor vuelva a intentarlo");
            return false;
        } else {
            direccion.setId(id_direccion);
            Aeropuerto airport = new Aeropuerto();
            airport.setNombre(txtNombre.getText());
            airport.setDireccion(direccion);
            airport.setAnio_inauguracion(Integer.parseInt(txtAnioInauguracion.getText()));
            airport.setCapacidad(Integer.parseInt(txtCapacidad.getText()));
            airport.setImagen(null);
            int id_aeropuerto = DaoAeropuerto.insertar(airport);
            if (id_aeropuerto == -1) {
                alerta("Ha habido un error almacenando los datos. Por favor vuelva a intentarlo");
                return false;
            } else {
                airport.setId(id_aeropuerto);
                if (rbPublico.isSelected()) {
                    // Aeropuerto Público
                    AeropuertoPublico aeropuertoPublico = new AeropuertoPublico(airport, new BigDecimal(txtParam1.getText()),Integer.parseInt(txtParam2.getText()));
                    if (!DaoAeropuertoPublico.insertar(aeropuertoPublico)) {
                        alerta("Ha habido un error almacenando los datos. Por favor vuelva a intentarlo");
                        return false;
                    }
                } else {
                    // Aeropuerto Privado
                    AeropuertoPrivado aeropuertoPrivado = new AeropuertoPrivado(airport,Integer.parseInt(txtParam1.getText()));
                    if (!DaoAeropuertoPrivado.insertar(aeropuertoPrivado)) {
                        alerta("Ha habido un error almacenando los datos. Por favor vuelva a intentarlo");
                        return false;
                    }
                }
                confirmacion("Aeropuerto creado correctamente");
                return true;
            }
        }
    }

    /**
     * Modifica un aeropuerto en la base de datos
     *
     * @return true/false
     */
    public boolean modificarAeropuerto() {
        Aeropuerto airport = new Aeropuerto();
        Direccion direccion = new Direccion();
        direccion.setId(aeropuerto_obj.getDireccion().getId());
        direccion.setPais(txtPais.getText());
        direccion.setCiudad(txtCiudad.getText());
        direccion.setCalle(txtCalle.getText());
        direccion.setNumero(Integer.parseInt(txtNumero.getText()));
        if (!DaoDireccion.modificar(this.aeropuerto_obj.getDireccion(),direccion)) {
            alerta("Ha habido un error almacenando los datos. Por favor vuelva a intentarlo");
            return false;
        } else {
            airport.setDireccion(direccion);
            airport.setNombre(txtNombre.getText());
            airport.setAnio_inauguracion(Integer.parseInt(txtAnioInauguracion.getText()));
            airport.setCapacidad(Integer.parseInt(txtCapacidad.getText()));
            airport.setImagen(null);
            if (!DaoAeropuerto.modificar(aeropuerto_obj,airport)) {
                alerta("Ha habido un error almacenando los datos. Por favor vuelva a intentarlo");
                return false;
            } else {
                if (this.aeropuerto instanceof AeropuertoPublico aeropuertoPublico) {
                    // Aeropuerto Público
                    AeropuertoPublico newAirport = new AeropuertoPublico(airport,new BigDecimal(txtParam1.getText()),Integer.parseInt(txtParam2.getText()));
                    if (!DaoAeropuertoPublico.modificar(aeropuertoPublico,newAirport)) {
                        alerta("Ha habido un error almacenando los datos. Por favor vuelva a intentarlo");
                        return false;
                    }
                } else {
                    // Aeropuerto Privado
                    AeropuertoPrivado aeropuertoPrivado = (AeropuertoPrivado) this.aeropuerto;
                    AeropuertoPrivado newAirport = new AeropuertoPrivado(airport,Integer.parseInt(txtParam1.getText()));
                    if (!DaoAeropuertoPrivado.modificar(aeropuertoPrivado,newAirport)) {
                        alerta("Ha habido un error almacenando los datos. Por favor vuelva a intentarlo");
                        return false;
                    }
                }
                confirmacion("Aeropuerto actualizado correctamente");
                return true;
            }
        }
    }

    /**
     * Se ejecuta cuando se cambia los RadioButtons
     *
     * @param newBtn nuevo botón
     */
    public void cambioTipo(ObservableValue<? extends Toggle> observableValue, Toggle oldBtn, Toggle newBtn) {
        if (newBtn.equals(rbPublico)) {
            // Aeropuerto Público
            lblParam1.setText("Financiación:");
            lblParam2.setText("Número de trabajadores:");
            txtParam2.setVisible(true);
        } else {
            // Aeropuerto Privado
            lblParam1.setText("Número de socios:");
            lblParam2.setText(null);
            txtParam2.setVisible(false);
        }
    }

    /**
     * Función que se ejecuta cuando se pulsa el botón "Cancelar". Cierra la ventana
     */
    @FXML
    void cancelar() {
        Stage stage = (Stage)txtNombre.getScene().getWindow();
        stage.close();
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
