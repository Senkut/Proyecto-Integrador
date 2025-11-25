package edu.usta.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Pattern;

public class PersonController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    // Patrón para validar email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @FXML
    private void savePerson(ActionEvent event) {

        // Validar vacíos
        if (nameField.getText().isEmpty() ||
                lastNameField.getText().isEmpty() ||
                emailField.getText().isEmpty() ||
                phoneField.getText().isEmpty()) {

            showAlert(Alert.AlertType.WARNING, "Campos Vacíos",
                    "Por favor complete todos los campos.");
            return;
        }

        // Validar email
        String email = emailField.getText();
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showAlert(Alert.AlertType.WARNING, "Correo Inválido",
                    "Por favor ingrese un correo electrónico válido.");
            return;
        }

        // Validar teléfono solo números
        String phone = phoneField.getText();
        if (!phone.matches("\\d+")) {
            showAlert(Alert.AlertType.WARNING, "Teléfono Inválido",
                    "El teléfono debe contener solo números.");
            return;
        }

        // Datos de salida (puedes reemplazar por persistencia real)
        String nombre = nameField.getText();
        String apellido = lastNameField.getText();

        System.out.println("Persona Registrada:");
        System.out.println("Nombre: " + nombre);
        System.out.println("Apellido: " + apellido);
        System.out.println("Correo: " + email);
        System.out.println("Teléfono: " + phone);

        showAlert(Alert.AlertType.INFORMATION, "Éxito",
                "Persona registrada correctamente.");

        clearFields();
    }

    @FXML
    private void cancel(ActionEvent event) {
        returnToMainMenu(event);
    }

    private void clearFields() {
        nameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneField.clear();
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
