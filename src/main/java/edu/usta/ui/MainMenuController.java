package edu.usta.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private void goTech(ActionEvent event) {
        loadView(event, "/views/TechEquipment.fxml", "Registro de Equipo Tecnológico");
    }

    @FXML
    private void goBio(ActionEvent event) {
        loadView(event, "/views/BiomedicalEquipment.fxml", "Registro de Equipo Biomédico");
    }

    @FXML
    private void goPerson(ActionEvent event) {
        loadView(event, "/views/Person.fxml", "Registro de Persona");
    }

    @FXML
    private void goProvider(ActionEvent event) {
        loadView(event, "/views/Provider.fxml", "Registro de Proveedor");
    }

    /**
     * Método auxiliar para cargar una nueva vista
     */
    private void loadView(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            if (loader.getLocation() == null) {
                throw new IOException("❌ Ruta FXML inválida: " + fxmlPath);
            }

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            System.err.println("\n❌ ERROR AL CARGAR LA VISTA");
            System.err.println("FXML solicitado: " + fxmlPath);
            System.err.println("Causa: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
