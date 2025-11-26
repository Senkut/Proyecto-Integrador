package edu.usta.ui;

import java.io.IOException;

import edu.usta.domain.entities.Provider;
import edu.usta.domain.repositories.JDBCProviderRepository;
import edu.usta.infrastructure.db.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ProviderController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField taxIdField;

    @FXML
    private TextField emailField;

    private JDBCProviderRepository repository;

    public ProviderController() {
        // Constructor vacío requerido por JavaFX
    }

    public void initialize() {
        // Inicializar repositorio
        repository = new JDBCProviderRepository(DatabaseConnection.getInstance());
    }

    // ------------------------
    // GUARDAR
    // ------------------------
    @FXML
    private void saveProvider() {
        try {
            // Validaciones
            if (!validateFields()) {
                return;
            }

            // Validar formato de email
            if (!isValidEmail(emailField.getText().trim())) {
                showWarning("Email inválido", "Por favor ingrese un email válido");
                return;
            }

            Provider provider = new Provider(
                    nameField.getText().trim(),
                    taxIdField.getText().trim(),
                    emailField.getText().trim());

            Provider saved = repository.create(provider);

            // Alerta de exito
            showInfo("✓ Registro Exitoso",
                    "El proveedor se ha registrado correctamente.\n\n" +
                            "ID generado: " + saved.getId());

            clearFields();

        } catch (Exception e) {
            // Alerta de error
            showError("✗ Error al Guardar",
                    "No se pudo registrar el proveedor.\n\n" +
                            "Detalle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------
    // CANCELAR / LIMPIAR
    // ------------------------
    @FXML
    private void clearForm() {
        clearFields();
        showInfo("Cancelado", "Los campos han sido limpiados");
    }

    // ------------------------
    // VOLVER AL MENÚ PRINCIPAL
    // ------------------------
    @FXML
    private void goToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/MainView.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                getClass().getResource("/css/mainmenu.css").toExternalForm());

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    // ------------------------
    // MÉTODOS AUXILIARES
    // ------------------------
    private boolean validateFields() {
        if (nameField.getText().trim().isEmpty()) {
            showWarning("Campo requerido", "El campo Nombre es obligatorio");
            return false;
        }
        if (taxIdField.getText().trim().isEmpty()) {
            showWarning("Campo requerido", "El campo Tax ID es obligatorio");
            return false;
        }
        if (emailField.getText().trim().isEmpty()) {
            showWarning("Campo requerido", "El campo Email es obligatorio");
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        // Validación básica de email
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    private void clearFields() {
        nameField.clear();
        taxIdField.clear();
        emailField.clear();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}