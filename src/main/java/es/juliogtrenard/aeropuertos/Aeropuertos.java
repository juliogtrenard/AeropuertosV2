package es.juliogtrenard.aeropuertos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


/**
 * Aplicacion de gestion de aeropuertos.
 */
public class Aeropuertos extends Application {
    /**
     * Inicia la aplicacion
     * @param stage Stage principal
     * @throws IOException Excepción de entrada/salida
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Aeropuertos.class.getResource("/es/juliogtrenard/aeropuertos/fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("AVIONES - LOGIN");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/es/juliogtrenard/aeropuertos/img/avion.png"))));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Lanza la aplicacion
     * @param args Argumentos de linea de comando
     */
    public static void main(String[] args) {
        launch();
    }
}