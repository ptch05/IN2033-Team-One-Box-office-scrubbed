package com.teamoneboxoffice.services.implementations.DAOs;

import com.teamoneboxoffice.entities.Customer;
import com.teamoneboxoffice.services.implementations.databaseImpl.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CustomerDAO {
    private final Database db;

    public CustomerDAO() {
        this.db = new Database();
    }


    /**
     * Creates a new customer in the database.
     *
     * @param customer The Customer object containing customer information.
     * @return true if the customer was created successfully, false otherwise.
     */
    public boolean create(Customer customer) {
        String sql = "INSERT INTO Customer (Customer_ID, Opt_IN, Payment_Type, Gender, " +
                "Postal_Code, Email_Address, Phone_Number) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getCustomerID());
            stmt.setBoolean(2, customer.isOptIN());
            stmt.setString(3, customer.getPaymentType());
            stmt.setString(4, customer.getGender());
            stmt.setString(5, customer.getPostalCode());
            stmt.setString(6, customer.getEmailAddress());
            stmt.setString(7, customer.getPhoneNumber());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error creating customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param customerId The ID of the customer to retrieve.
     * @return The Customer object if found, null otherwise.
     */
    public Customer getById(String customerId) {
        String sql = "SELECT * FROM Customer WHERE Customer_ID = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving customer: " + e.getMessage());
        }

        return null;
    }

    /**
     * Retrieves a customer by their email address.
     *
     * @param email The email address of the customer to retrieve.
     * @return The Customer object if found, null otherwise.
     */
    public Customer getByEmail(String email) {
        String sql = "SELECT * FROM Customer WHERE Email_Address = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving customer by email: " + e.getMessage());
        }

        return null;
    }

    /**
     * Updates an existing customer in the database.
     *
     * @param customer The Customer object containing updated information.
     * @return true if the customer was updated successfully, false otherwise.
     */
    public boolean update(Customer customer) {
        String sql = "UPDATE Customer SET Opt_IN = ?, Payment_Type = ?, Gender = ?, " +
                "Postal_Code = ?, Email_Address = ?, Phone_Number = ? WHERE Customer_ID = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, customer.isOptIN());
            stmt.setString(2, customer.getPaymentType());
            stmt.setString(3, customer.getGender());
            stmt.setString(4, customer.getPostalCode());
            stmt.setString(5, customer.getEmailAddress());
            stmt.setString(6, customer.getPhoneNumber());
            stmt.setString(7, customer.getCustomerID());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a customer from the database.
     *
     * @param customerId The ID of the customer to delete.
     * @return true if the customer was deleted successfully, false otherwise.
     */
    public boolean delete(String customerId) {
        String sql = "DELETE FROM Customer WHERE Customer_ID = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all customers from the database.
     *
     * @return A list of Customer objects.
     */
    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all customers: " + e.getMessage());
        }

        return customers;
    }

    /**
     * Maps a ResultSet to a Customer object.
     *
     * @param rs The ResultSet containing customer data.
     * @return A Customer object.
     * @throws SQLException If an SQL error occurs.
     */
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        String customerId = rs.getString("Customer_ID");
        boolean optIn = rs.getBoolean("Opt_IN");
        String paymentType = rs.getString("Payment_Type");
        String gender = rs.getString("Gender");
        String postalCode = rs.getString("Postal_Code");
        String emailAddress = rs.getString("Email_Address");
        String phoneNumber = rs.getString("Phone_Number");

        return new Customer(customerId, optIn, paymentType, gender, postalCode, emailAddress, phoneNumber);
    }
}