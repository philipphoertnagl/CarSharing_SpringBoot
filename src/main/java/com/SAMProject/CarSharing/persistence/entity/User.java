package com.SAMProject.CarSharing.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Username required")
    @Column(name = "username", nullable = false, unique = true)
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Column(name = "password", nullable = false)
    @Size(min = 5, max = 25, message = "Password must be between 5 and 25 characters")
    private String password;



    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "customer_details_id", referencedColumnName = "id", nullable = true)
    private CustomerDetails customerDetails;



    public enum Role {
        CUSTOMER, MANAGER;
    }

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                ", role=" + role +
                ", customerDetails=" + customerDetails +
                '}';
    }
}
