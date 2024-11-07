package es.juliogtrenard.aeropuertos.controladores;

import es.juliogtrenard.aeropuertos.dao.DaoUsuario;
import es.juliogtrenard.aeropuertos.modelos.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador de la ventana login
 */
public class LoginController implements Initializable {
    /**
     * Campo de texto para la contraseña
     */
    @FXML
    private PasswordField txtPassword;

    /**
     * Campo de texto para el usuario
     */
    @FXML
    private TextField txtUsuario;

    /**
     * Boton de login
     */
    @FXML
    private Button btnLogin;

    /**
     * Se ejecuta cuando se carga la ventana
     *
     * @param url la url
     * @param resourceBundle los recursos
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnLogin.setDefaultButton(true);
    }

    /**
     * Controlador para el botón de login. Valida que los campos de usuario
     * y contraseña tengan un valor no vacío. Si los campos son válidos,
     * intenta obtener el usuario de la base de datos y llama a
     * {@link #procesarLogin(Usuario)} si existe o a
     * {@link #manejarUsuarioInvalido()} si no existe.
     */
    @FXML
    void login() {
        String error = validarCampos();
        if (!error.isBlank()) {
            alerta(error);
        } else {
            Usuario user = obtenerUsuario();
            if (user == null) {
                manejarUsuarioInvalido();
            } else {
                procesarLogin(user);
            }
        }
    }

    /**
     * Valida que los campos de usuario y password tengan un valor no vacío
     *
     * @return Cadena con los errores de validación o cadena vacía si los campos son válidos
     */
    private String validarCampos() {
        String error = "";
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        if (usuario.isBlank()) {
            error = "El campo usuario no puede estar vacío";
        }
        if (password.isEmpty()) {
            if (!error.isBlank()) {
                error += "\n";
            }
            error += "El campo password no puede estar vacío";
        }
        return error;
    }

    /**
     * Obtiene el usuario del campo de texto y lo devuelve como objeto Usuario si existe en la base de datos
     *
     * @return Usuario o null si el usuario no existe en la base de datos
     */
    private Usuario obtenerUsuario() {
        String usuario = txtUsuario.getText();
        return DaoUsuario.getUsuario(usuario);
    }

    /**
     * Muestra un mensaje de error al usuario y limpia los campos de texto cuando
     * el usuario o la contraseña no son válidos.
     */
    private void manejarUsuarioInvalido() {
        alerta("Usuario no valido");
        txtUsuario.setText("");
        txtPassword.setText("");
    }

    /**
     * Comprueba si el password del usuario coincide con el password de la base de datos.
     * Si coincide, llama a {@link #loginCorrecto()} para mostrar la ventana principal.
     * Si no coincide, muestra un mensaje de error al usuario y borra el campo de texto
     * de la contraseña.
     *
     * @param user objeto Usuario que contiene el password del usuario de la base de datos
     */
    private void procesarLogin(Usuario user) {
        String password = txtPassword.getText();
        if (password.equals(user.getPassword())) {
            loginCorrecto();
        } else {
            alerta("Contraseña incorrecta");
            txtPassword.setText("");
        }
    }

    /**
     * Abre la ventana principal de la aplicación y cierra la ventana de login
     * si el usuario ha introducido correctamente su usuario y contraseña.
     */
    private void loginCorrecto() {
        System.out.println("Login correcto");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/es/juliogtrenard/aeropuertos/fxml/aeropuertos.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("AVIONES - AEROPUERTOS");
            stage.show();
            Stage actual = (Stage) txtUsuario.getScene().getWindow();
            actual.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            alerta("Error abriendo ventana, por favor inténtelo de nuevo");
        }
    }

    /**
     * Muestra un mensaje de alerta al usuario
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

}