package edu.usta.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class BiomedicalController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField manufacturerField;

    @FXML
    private DatePicker fabDateField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private void saveBio(ActionEvent event) {

        // Validar campos vacíos
        if (nameField.getText().isEmpty() ||
                manufacturerField.getText().isEmpty() ||
                fabDateField.getValue() == null ||
                descriptionField.getText().isEmpty()) {

            showAlert(Alert.AlertType.WARNING, "Campos Vacíos",
                    "Por favor complete todos los campos.");
            return;
        }

        // Validar que la fecha no sea futura
        LocalDate fecha = fabDateField.getValue();
        if (fecha.isAfter(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Fecha Inválida",
                    "La fecha de fabricación no puede ser futura.");
            return;
        }

        // Datos
        String nombre = nameField.getText();
        String fabricante = manufacturerField.getText();
        String description = descriptionField.getText();

        System.out.println("Equipo Biomédico Guardado:");
        System.out.println("Nombre: " + nombre);
        System.out.println("Fabricante: " + fabricante);
        System.out.println("Fecha Fabricación: " + fecha);
        System.out.println("Descripción: " + description);

        // Reemplázalo por tu repositorio/database
        // biomedicalRepository.save(...)

        showAlert(Alert.AlertType.INFORMATION, "Éxito",
                "Equipo biomédico registrado correctamente.");

        clearFields();
    }

    @FXML
    private void cancel(ActionEvent event) {
        returnToMainMenu(event);
    }

    private void clearFields() {
        nameField.clear();
        manufacturerField.clear();
        fabDateField.setValue(null);
        descriptionField.clear();
    }

    private void returnToMainMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Sistema de Registros");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "No se pudo cargar la vista principal.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
