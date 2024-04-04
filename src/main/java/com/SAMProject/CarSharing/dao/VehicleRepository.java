package com.SAMProject.CarSharing.dao;


import com.SAMProject.CarSharing.Entity.User;
import com.SAMProject.CarSharing.Entity.Vehicle;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VehicleRepository {
    List<Vehicle> vehicleList = new ArrayList<>();
    private int id;



    public int findIndexById(int id) {
        for (int i = 0; i < vehicleList.size(); i++) {
            if (vehicleList.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }
    public Vehicle save(Vehicle vehicle) {
        if (vehicle.getId() == 0) {
            vehicle.setId(++id);
            vehicleList.add(vehicle);
            return vehicle;
        } else {
            int index = findIndexById(vehicle.getId());
            if (index != -1) {
                vehicleList.set(index, vehicle);
                return vehicle;
            }
            return null;

        }
    }

    public List<Vehicle> allVehicles() {
        return new ArrayList<>(vehicleList);
    }

    public void deleteVehicle(int id) {
        Vehicle removingVehicle = findById(id);
        if (removingVehicle != null) {
            vehicleList.remove(removingVehicle);
        }
    }

    public Vehicle findById(int id) {
        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getId() == id) {
                return vehicle;
            }
        }
        return null;
    }
}
