package com.SAMProject.CarSharing;

import com.SAMProject.CarSharing.persistence.entity.StatusDetails;
import com.SAMProject.CarSharing.persistence.entity.Vehicle;
import com.SAMProject.CarSharing.persistence.jpa.VehicleRepositoryJakarta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class VehicleRepositoryTests {

    @Autowired
    private VehicleRepositoryJakarta vehicleRepository;
    @Test
    public void testSaveAndFindVehicle() {
        //setting up statusDetails..
        StatusDetails statusDetails = new StatusDetails();
        statusDetails.setOccupyState(StatusDetails.OccupyState.FREE);

        Vehicle vehicle = new Vehicle("Alte Karre", "Irgendwas", statusDetails);
        vehicle = vehicleRepository.save(vehicle);

        Vehicle foundVehicle = vehicleRepository.findById(vehicle.getId()).orElse(null);

        assertNotNull(foundVehicle, "The vehicle should be found in the database.");
        assertEquals("Alte Karre", foundVehicle.getName(), "Vehicle name muss uebereinstimmen");
        assertEquals("Irgendwas", foundVehicle.getDescription(), "Beschreibung passt ueberein.");
        assertEquals(StatusDetails.OccupyState.FREE, foundVehicle.getStatusDetails().getOccupyState(), "StatusDetails occupyState stimmt ueberein.");
    }
}