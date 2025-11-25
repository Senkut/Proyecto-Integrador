package edu.usta;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views/MainView.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            primaryStage.setTitle("Sistema de Registros");
            primaryStage.setScene(scene);

            // ✔️ Permitir que la ventana crezca automáticamente
            primaryStage.setResizable(true);

            // ✔️ Mostrar en pantalla completa (sin fullscreen)
            primaryStage.setMaximized(true);

            primaryStage.show();

        } catch (Exception e) {
            System.out.println("❌ Error cargando la vista MainView.fxml");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}