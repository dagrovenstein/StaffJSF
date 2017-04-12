package jsfexercise;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author Daniel
 */
@Named(value = "staffInformation")
@SessionScoped

public class Exercise38_19JSFBean implements Serializable {

    private String id;
    private String lastName;
    private String firstName;
    private String mi;
    private String address;
    private String telephone;
    private String city;
    private String state;
    private String status;

    private PreparedStatement pstmt;

    public Exercise38_19JSFBean() {
        initializeJdbc();
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMi() {
        return mi;
    }

    public void setMi(String mi) {
        this.mi = mi;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void onInsert() {
        if (id.isEmpty()) {
            status = "ID is empty.";
        } else {
            try {
                ResultSet rs = pstmt.executeQuery("select * from staff where id = " + id + ";");
                if (rs.next()) {
                    status = "ID already exists, can not have duplicate key.";
                } else {
                    storeStaff(id, lastName, firstName, mi,
                            address, city, state, telephone);
                    status = "New record inserted into database.";
                }
            } catch (SQLException ex) {
                status = "Error: " + ex.getMessage();
            }
        }
    }

    private void storeStaff(String id, String lastName, String firstName,
            String mi, String address,
            String city, String state, String telephone) throws SQLException {
        pstmt.setString(1, id);
        pstmt.setString(2, lastName);
        pstmt.setString(3, firstName);
        pstmt.setString(4, mi);
        pstmt.setString(5, address);
        pstmt.setString(6, city);
        pstmt.setString(7, state);
        pstmt.setString(8, telephone);
        pstmt.executeUpdate();
    }

    public void onUpdate() {
        if (id.isEmpty()) {
            status = "ID is empty.";
        } else {
            try {
                if (!lastName.isEmpty() || !firstName.isEmpty() || !mi.isEmpty() || !address.isEmpty()
                        || !city.isEmpty() || !state.isEmpty() || !telephone.isEmpty()) {
                    updateStaff(id, lastName, firstName, mi,
                            address, city, state, telephone);
                    status = "ID: " + id + " was successfully updated!";
                }
            } catch (SQLException ex) {
                status = "Error: " + ex.getMessage();
            }
        }
    }

    private void updateStaff(String id, String lastName, String firstName, String mi,
            String address, String city, String state, String telephone) throws SQLException {
        if (!lastName.isEmpty()) {
            pstmt.executeUpdate("Update Staff set lastname = '" + lastName + "' where id = " + id + ";");
        }
        if (!firstName.isEmpty()) {
            pstmt.executeUpdate("Update Staff set firstname = '" + firstName + "' where id = " + id + ";");
        }
        if (!mi.isEmpty()) {
            pstmt.executeUpdate("Update Staff set mi = '" + mi + "' where id = " + id + ";");
        }
        if (!address.isEmpty()) {
            pstmt.executeUpdate("Update Staff set address = '" + address + "' where id = " + id + ";");
        }
        if (!city.isEmpty()) {
            pstmt.executeUpdate("Update Staff set city = '" + city + "' where id = " + id + ";");
        }
        if (!state.isEmpty()) {
            pstmt.executeUpdate("Update Staff set state = '" + state + "' where id = " + id + ";");
        }
        if (!telephone.isEmpty()) {
            pstmt.executeUpdate("Update Staff set telephone = '" + telephone + "' where id = " + id + ";");
        }
    }

    public void onView() {
        if (id.isEmpty()) {
            status = "ID is empty.";
        } else {
            try {
                ResultSet rs = pstmt.executeQuery("select * from staff where id = " + id + ";");
                if (!rs.isBeforeFirst()) {
                    status = "ID not in database.";
                } else {
                    viewStaff(id, rs);
                    status = "Viewing ID: " + id;
                }
            } catch (SQLException ex) {
                status = "SQL Exception.";
            }
        }
    }

    private void viewStaff(String id, ResultSet rs) throws SQLException {
        while (rs.next()) {
            lastName = rs.getString(2);
            firstName = rs.getString(3);
            mi = rs.getString(4);
            address = rs.getString(5);
            city = rs.getString(6);
            state = rs.getString(7);
            telephone = rs.getString(8);
        }
    }

    public void onClear() {
        id = "";
        lastName = "";
        firstName = "";
        mi = "";
        address = "";
        city = "";
        state = "";
        telephone = "";
        status = "";
    }

    public String responseString() {
        return status;
    }

    private void initializeJdbc() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/ex37_13?autoreconnect=true", "scott", "tiger");

            System.out.println("Database connected");

            pstmt = conn.prepareStatement("insert into staff "
                    + "(id, lastName, firstName, mi, address, city, "
                    + "state, telephone) values (?, ?, ?, ?, ?, ?, ?, ?)");
        } catch (Exception ex) {
            status = "Databsae connection failed.";
        }
    }
}
