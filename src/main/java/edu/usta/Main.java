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
            System.out.println("🔍 Buscando archivo FXML...");

            // Cargar el archivo FXML desde views/MainView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));

            System.out.println("📂 Cargando FXML...");
            Parent root = loader.load();

            System.out.println("✅ FXML cargado exitosamente!");

            // Configurar la escena
            Scene scene = new Scene(root, 600, 700);

            // Configurar el stage
            primaryStage.setTitle("Equipment Manager - Formulario");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            System.out.println("✅ Aplicación JavaFX iniciada correctamente!");

        } catch (Exception e) {
            System.err.println("❌ ERROR al cargar la aplicación:");
            System.err.println("Detalles del error:");
            e.printStackTrace();

            // Información adicional para debugging
            System.err.println("\n📋 Verifica:");
            System.err.println("1. Que MainView.fxml esté en: src/main/resources/views/MainView.fxml");
            System.err.println("2. Que MainController.java esté en: src/main/java/edu/usta/ui/MainController.java");
            System.err.println("3. Que el package del controller sea: package edu.usta.ui;");
        }
    }

    public static void main(String[] args) {
        System.out.println("🚀 Iniciando aplicación JavaFX...");
        System.out.println("📍 Working directory: " + System.getProperty("user.dir"));
        launch(args);
    }
}