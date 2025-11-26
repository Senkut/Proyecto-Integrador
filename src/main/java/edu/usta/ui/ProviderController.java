package edu.usta.ui;

import edu.usta.domain.entities.Provider;
import edu.usta.domain.repositories.JDBCProviderRepository;
import edu.usta.infrastructure.db.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class ProviderController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField taxIdField;
    @FXML
    private TextField emailField;

    // Agregar el repositorio
    private final JDBCProviderRepository repository = new JDBCProviderRepository(DatabaseConnection.getInstance());

    @FXML
    public void saveProvider(ActionEvent event) {
        // Validar que los campos no estén vacíos
        if (nameField.getText().isEmpty() ||
                taxIdField.getText().isEmpty() ||
                emailField.getText().isEmpty()) {

            showAlert(Alert.AlertType.WARNING, "Campos incompletos",
                    "Debe llenar todos los campos.");
            return;
        }

        try {
            // Crear el objeto Provider
            Provider provider = new Provider(
                    nameField.getText(),
                    taxIdField.getText(),
                    emailField.getText());

            // Guardar en la base de datos
            repository.create(provider);

            showAlert(Alert.AlertType.INFORMATION, "Éxito",
                    "El proveedor ha sido registrado correctamente.");

            // Limpiar los campos
            clearForm(event);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Ocurrió un error al guardar el proveedor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void clearForm(ActionEvent event) {
        nameField.clear();
        taxIdField.clear();
        emailField.clear();
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar para mostrar alertas
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}