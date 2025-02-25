package com.nonstopio.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.nonstopio.client.PersonService;
import com.nonstopio.shared.Person;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PersonServiceImpl extends RemoteServiceServlet implements PersonService {
    private static final long serialVersionUID = 1L;

    // Database connection parameters
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/person_crud_db";
    private static final String USER = "anup";
    private static final String PASS = "anup";

    // SQL queries
    private static final String CREATE_PERSON = "INSERT INTO persons (first_name, last_name, email) VALUES (?, ?, ?)";
    private static final String UPDATE_PERSON = "UPDATE persons SET first_name = ?, last_name = ?, email = ? WHERE id = ?";
    private static final String DELETE_PERSON = "DELETE FROM persons WHERE id = ?";
    private static final String GET_PERSON = "SELECT * FROM persons WHERE id = ?";
    private static final String GET_ALL_PERSONS = "SELECT * FROM persons";

    // Initialize database connection
    private Connection getConnection() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("JDBC Driver not found", e);
        }
    }

    @Override
    public Person createPerson(Person person) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_PERSON, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, person.getFirstName());
            stmt.setString(2, person.getLastName());
            stmt.setString(3, person.getEmail());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating person failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    person.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating person failed, no ID obtained.");
                }
            }

            return person;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating person", e);
        }
    }

    @Override
    public Person updatePerson(Person person) {
        if (person.getId() == null) {
            throw new IllegalArgumentException("Person ID cannot be null");
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_PERSON)) {

            stmt.setString(1, person.getFirstName());
            stmt.setString(2, person.getLastName());
            stmt.setString(3, person.getEmail());
            stmt.setLong(4, person.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new IllegalArgumentException("Person not found with id: " + person.getId());
            }

            return person;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating person", e);
        }
    }

    @Override
    public void deletePerson(Long id) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_PERSON)) {

            stmt.setLong(1, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new IllegalArgumentException("Person not found with id: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting person", e);
        }
    }

    @Override
    public Person getPerson(Long id) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_PERSON)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPerson(rs);
                } else {
                    throw new IllegalArgumentException("Person not found with id: " + id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving person", e);
        }
    }

    @Override
    public List<Person> getAllPersons() {
        List<Person> personList = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL_PERSONS)) {

            while (rs.next()) {
                personList.add(mapResultSetToPerson(rs));
            }

            return personList;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all persons", e);
        }
    }

    private Person mapResultSetToPerson(ResultSet rs) throws SQLException {
        Person person = new Person();
        person.setId(rs.getLong("id"));
        person.setFirstName(rs.getString("first_name"));
        person.setLastName(rs.getString("last_name"));
        person.setEmail(rs.getString("email"));
        return person;
    }
}