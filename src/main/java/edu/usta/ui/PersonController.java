package edu.usta.ui;

import edu.usta.domain.entities.Person;
import edu.usta.domain.enums.Role;
import edu.usta.domain.repositories.JDBCPersonRepository;
import edu.usta.infrastructure.db.DatabaseConnection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class PersonController {

    @FXML
    private TextField fullnameField;
    @FXML
    private TextField documentField;
    @FXML
    private ComboBox<Role> roleField;

    private final JDBCPersonRepository repository = new JDBCPersonRepository(DatabaseConnection.getInstance());

    @FXML
    public void initialize() {
        roleField.getItems().addAll(Role.values());
    }

    @FXML
    private void savePerson(ActionEvent event) {

        if (fullnameField.getText().isEmpty() ||
                documentField.getText().isEmpty() ||
                roleField.getValue() == null) {

            showAlert(Alert.AlertType.WARNING, "Campos incompletos",
                    "Debe llenar todos los campos.");
            return;
        }

        Person person = new Person(
                fullnameField.getText(),
                documentField.getText(),
                roleField.getValue());

        repository.create(person);

        showAlert(Alert.AlertType.INFORMATION, "Éxito",
                "La persona ha sido registrada correctamente.");

        clearFields();
    }

    @FXML
    private void cancel(ActionEvent event) {
        clearFields();
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
            System.out.println("❌ ERROR: No se pudo cargar la vista MainView.fxml");
        }
    }

    private void clearFields() {
        fullnameField.clear();
        documentField.clear();
        roleField.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}