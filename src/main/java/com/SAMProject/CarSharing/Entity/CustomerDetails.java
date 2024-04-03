package com.SAMProject.CarSharing.Entity;

public class CustomerDetails {
    private String firstName;
    private String surname;
    private int age;
    private String drivingLicense;
    private int ccNumber;


    public CustomerDetails(String firstName, String surname, int age, String drivingLicense, int ccNumber) {
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;
        this.drivingLicense = drivingLicense;
        this.ccNumber = ccNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public int getCcNumber() {
        return ccNumber;
    }

    public void setCcNumber(int ccNumber) {
        this.ccNumber = ccNumber;
    }

    @Override
    public String toString() {
        return "CustomerDetails{" +
                "firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", drivingLicense='" + drivingLicense + '\'' +
                ", ccNumber=" + ccNumber +
                '}';
    }
}
