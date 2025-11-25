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

public class ProviderController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField companyField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    // Patrón para validar email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @FXML
    private void saveProvider(ActionEvent event) {
        // Validar que los campos no estén vacíos
        if (nameField.getText().isEmpty() || companyField.getText().isEmpty() ||
                emailField.getText().isEmpty() || phoneField.getText().isEmpty()) {

            showAlert(Alert.AlertType.WARNING, "Campos Vacíos",
                    "Por favor complete todos los campos");
            return;
        }

        // Validar formato de email
        String email = emailField.getText();
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showAlert(Alert.AlertType.WARNING, "Email Inválido",
                    "Por favor ingrese un correo electrónico válido");
            return;
        }

        // Validar que el teléfono solo contenga números
        String telefono = phoneField.getText();
        if (!telefono.matches("\\d+")) {
            showAlert(Alert.AlertType.WARNING, "Teléfono Inválido",
                    "El teléfono debe contener solo números");
            return;
        }

        // Aquí iría la lógica para guardar en base de datos
        String nombre = nameField.getText();
        String empresa = companyField.getText();

        System.out.println("Proveedor Guardado:");
        System.out.println("Nombre: " + nombre);
        System.out.println("Empresa: " + empresa);
        System.out.println("Correo: " + email);
        System.out.println("Teléfono: " + telefono);

        showAlert(Alert.AlertType.INFORMATION, "Éxito",
                "Proveedor registrado correctamente");

        clearFields();
    }

    @FXML
    private void cancel(ActionEvent event) {
        returnToMainMenu(event);
    }

    private void clearFields() {
        nameField.clear();
        companyField.clear();
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