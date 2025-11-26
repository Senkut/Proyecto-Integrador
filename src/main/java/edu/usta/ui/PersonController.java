package edu.usta.ui;

import java.io.IOException;

import edu.usta.domain.entities.Person;
import edu.usta.domain.enums.Role;
import edu.usta.domain.repositories.JDBCPersonRepository;
import edu.usta.infrastructure.db.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class PersonController {

    @FXML
    private TextField fullnameField;

    @FXML
    private TextField documentField;

    @FXML
    private ComboBox<Role> roleField;

    private JDBCPersonRepository repository;

    public PersonController() {
        // Constructor vacío requerido por JavaFX
    }

    public void initialize() {
        // Inicializar repositorio
        repository = new JDBCPersonRepository(DatabaseConnection.getInstance());

        // Cargar roles en el ComboBox
        roleField.getItems().addAll(Role.values());
    }

    // ------------------------
    // GUARDAR
    // ------------------------
    @FXML
    private void savePerson() {
        try {
            // Validaciones
            if (!validateFields()) {
                return;
            }

            Person person = new Person(
                    fullnameField.getText().trim(),
                    documentField.getText().trim(),
                    roleField.getValue());

            Person saved = repository.create(person);

            // Alerta de éxito
            showInfo("✓ Registro Exitoso",
                    "La persona se ha registrado correctamente.\n\n" +
                            "ID generado: " + saved.getId());

            clearFields();

        } catch (Exception e) {
            // Alerta de error
            showError("✗ Error al Guardar",
                    "No se pudo registrar la persona.\n\n" +
                            "Detalle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------
    // CANCELAR
    // ------------------------
    @FXML
    private void cancel() {
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
        if (fullnameField.getText().trim().isEmpty()) {
            showWarning("Campo requerido", "El campo Nombre Completo es obligatorio");
            return false;
        }
        if (documentField.getText().trim().isEmpty()) {
            showWarning("Campo requerido", "El campo Documento es obligatorio");
            return false;
        }
        if (roleField.getValue() == null) {
            showWarning("Campo requerido", "Debe seleccionar un Rol");
            return false;
        }
        return true;
    }

    private void clearFields() {
        fullnameField.clear();
        documentField.clear();
        roleField.getSelectionModel().clearSelection();
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