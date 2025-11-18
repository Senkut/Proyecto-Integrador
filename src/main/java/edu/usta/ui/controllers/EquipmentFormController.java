package edu.usta.ui.controllers;

import edu.usta.application.usecases.GenericUseCases;
import edu.usta.domain.entities.Equipment;
import edu.usta.domain.entities.Provider;
import edu.usta.domain.enums.EquipmentStatus;
import edu.usta.domain.enums.EquipmentType;
import edu.usta.domain.repositories.JDBCEquipmentRepository;
import edu.usta.infrastructure.db.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EquipmentFormController {

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtBrand;
    @FXML
    private TextField txtModel;

    @FXML
    private ComboBox<EquipmentType> cmbType;
    @FXML
    private ComboBox<EquipmentStatus> cmbStatus;

    @FXML
    private TextField txtProviderId;
    @FXML
    private TextField txtProviderName;
    @FXML
    private TextField txtProviderEmail;
    @FXML
    private TextField txtProviderNit;

    @FXML
    private TextField txtImageUrl;
    @FXML
    private Button btnSave;

    private GenericUseCases<Equipment> useCases;

    @FXML
    public void initialize() {
        JDBCEquipmentRepository repo = new JDBCEquipmentRepository(DatabaseConnection.getInstance());

        useCases = new GenericUseCases<>(repo);

        cmbType.getItems().addAll(EquipmentType.values());
        cmbStatus.getItems().addAll(EquipmentStatus.values());

        btnSave.setOnAction(e -> saveEquipment());
    }

    private void saveEquipment() {
        try {
            Provider provider = new Provider(
                    txtProviderId.getText(),
                    txtProviderName.getText(),
                    txtProviderNit.getText(),
                    txtProviderEmail.getText());

            Equipment equipment = new Equipment(
                    txtName.getText(),
                    txtBrand.getText(),
                    txtModel.getText(),
                    cmbType.getValue(),
                    cmbStatus.getValue(),
                    provider,
                    txtImageUrl.getText());

            Equipment saved = useCases.create(equipment);

            showAlert("Éxito",
                    "Equipo registrado con ID: " + saved.getId());

        } catch (Exception ex) {
            showAlert("Error", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
