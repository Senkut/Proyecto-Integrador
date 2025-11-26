package edu.usta.ui;

import edu.usta.domain.entities.Provider;
import edu.usta.domain.entities.TechEquipment;
import edu.usta.domain.enums.EquipmentStatus;
import edu.usta.domain.enums.EquipmentType;
import edu.usta.domain.repositories.JDBCTechEquipmentRepository;
import edu.usta.domain.repositories.JDBCProviderRepository;
import edu.usta.infrastructure.db.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TechEquipmentController implements Initializable {

    @FXML
    private TextField serialField;
    @FXML
    private TextField brandField;
    @FXML
    private TextField modelField;
    @FXML
    private ComboBox<EquipmentType> typeBox;
    @FXML
    private ComboBox<EquipmentStatus> stateBox;
    @FXML
    private ComboBox<Provider> providerBox;
    @FXML
    private TextField osField;
    @FXML
    private TextField ramField;
    @FXML
    private TextField imageField;

    private JDBCTechEquipmentRepository techRepo;
    private JDBCProviderRepository providerRepo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseConnection db = DatabaseConnection.getInstance();

        techRepo = new JDBCTechEquipmentRepository(db);
        providerRepo = new JDBCProviderRepository(db);

        typeBox.getItems().addAll(EquipmentType.values());
        stateBox.getItems().addAll(EquipmentStatus.values());

        List<Provider> providers = providerRepo.findAll();
        providerBox.getItems().addAll(providers);

        // AGREGAR ESTO: Configurar cómo se muestra el Provider en el ComboBox
        providerBox.setConverter(new javafx.util.StringConverter<Provider>() {
            @Override
            public String toString(Provider provider) {
                return provider != null ? provider.getName() : "";
            }

            @Override
            public Provider fromString(String string) {
                return providerBox.getItems().stream()
                        .filter(p -> p.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    @FXML
    public void cancel(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        serialField.clear();
        brandField.clear();
        modelField.clear();
        osField.clear();
        ramField.clear();
        imageField.clear();

        typeBox.setValue(null);
        stateBox.setValue(null);
        providerBox.setValue(null);
    }

    @FXML
    public void saveTech(ActionEvent event) {
        try {

            if (serialField.getText().isEmpty() ||
                    brandField.getText().isEmpty() ||
                    modelField.getText().isEmpty() ||
                    typeBox.getValue() == null ||
                    stateBox.getValue() == null ||
                    providerBox.getValue() == null ||
                    osField.getText().isEmpty() ||
                    ramField.getText().isEmpty()) {

                showAlert(Alert.AlertType.ERROR, "Campos incompletos",
                        "Por favor complete todos los campos obligatorios.");
                return;
            }

            TechEquipment tech = new TechEquipment(
                    serialField.getText(),
                    brandField.getText(),
                    modelField.getText(),
                    typeBox.getValue(),
                    stateBox.getValue(),
                    providerBox.getValue(),
                    imageField.getText(),
                    osField.getText(),
                    Integer.parseInt(ramField.getText()));

            techRepo.create(tech);

            showAlert(Alert.AlertType.INFORMATION, "Registro completado", "Equipo guardado correctamente.");
            clearFields();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error al guardar", e.getMessage());
        }
    }

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

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(title);
        alert.setContentText(msg);

        // Estilo moderno
        DialogPane pane = alert.getDialogPane();
        pane.setStyle("-fx-background-color: #ffffff; -fx-font-size: 15px;");
        pane.getStylesheets().add(
                getClass().getResource("/css/alert.css").toExternalForm());
        alert.showAndWait();
    }
}
