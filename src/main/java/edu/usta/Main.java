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

        GenericUseCases<Equipment> useCases = new GenericUseCases<Equipment>(equipmentRepository);

        Equipment equipment = createEquipment(equipmentRepository, useCases);

        findEquipmentById(equipmentRepository, useCases, UUIDGenerator.parseUUID(equipment.getId()));
    }

    public static Equipment createEquipment(
            JDBCEquipmentRepository repo,
            GenericUseCases<Equipment> useCase) {
        Provider provider001 = new Provider(
                "a4309f63-34ae-4a1b-b5a2-78e353497b10",
                "Medical SAS", "NIT-900123456789",
                "contact@meditech.com");

        Equipment equipment001 = new Equipment(
                "Activo001", "Dell", "OptiPlex",
                EquipmentType.OFFICE, EquipmentStatus.IN_USE, provider001,
                "https://m.media-amazon.com/images/I/610FOSnO-fL.jpg");

        Equipment dbEquipment001 = useCase.create(equipment001);
        System.out.println(">>> Equipo Creado");
        return dbEquipment001;
    }

    public static void findEquipmentById(JDBCEquipmentRepository repo, GenericUseCases<Equipment> useCase, UUID id) {
        Optional<Equipment> equipmentFound = useCase.findById(id);

        if (equipmentFound != null && equipmentFound.isPresent()) {
            System.out.println(">>> Se ha encontrado un registro igual en la base datos");
            System.out.println(equipmentFound);
        } else {
            System.out.println(">>> No se ha encontrado el registro buscado");
        }
    }
}

// Cambios:
// 1. En la entidad, borrar el id generado.
// 2. En el JDBC, en las sentencias SQL añadir tipado para los enums
// 3. En el método create, recibir el id retornado como UUID y luego usar
// toString
// 4. En el método map transformar el resultado de los enums de SQL a enums de
// JAVA