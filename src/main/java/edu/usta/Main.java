package edu.usta;

import java.util.Optional;
import java.util.UUID;

import edu.usta.application.usecases.GenericUseCases;
import edu.usta.domain.entities.Equipment;
import edu.usta.domain.entities.Provider;
import edu.usta.domain.enums.EquipmentStatus;
import edu.usta.domain.enums.EquipmentType;
import edu.usta.domain.repositories.JDBCEquipmentRepository;
import edu.usta.infrastructure.config.UUIDGenerator;
import edu.usta.infrastructure.db.DatabaseConnection;

public class Main {
    public static void main(String[] args) {

        JDBCEquipmentRepository equipmentRepository = new JDBCEquipmentRepository(DatabaseConnection.getInstance());

        GenericUseCases<Equipment> useCases = new GenericUseCases<>(equipmentRepository);

        Equipment equipment = createEquipment(useCases);

        findEquipmentById(useCases, UUIDGenerator.parseUUID(equipment.getId()));
    }

    public static Equipment createEquipment(GenericUseCases<Equipment> useCase) {

        Provider provider001 = new Provider(
                "531d6c84-8485-4363-80ae-712a6daec2b9",
                "Medical SAS",
                "NIT-900123456789",
                "contact@meditech.com");

        Equipment equipment001 = new Equipment(
                "Activo001",
                "Dell",
                "OptiPlex",
                EquipmentType.OFFICE,
                EquipmentStatus.IN_USE,
                provider001,
                "https://m.media-amazon.com/images/I/610FOSnO-fL.jpg");

        Equipment saved = useCase.create(equipment001);
        System.out.println(">>> Equipo Creado");
        return saved;
    }

    public static void findEquipmentById(GenericUseCases<Equipment> useCase, UUID id) {

        Optional<Equipment> equipmentFound = useCase.findById(id);

        if (equipmentFound.isPresent()) {
            System.out.println(">>> Se ha encontrado un registro");
            System.out.println(equipmentFound.get());
        } else {
            System.out.println(">>> No se ha encontrado el registro");
        }
    }
}
