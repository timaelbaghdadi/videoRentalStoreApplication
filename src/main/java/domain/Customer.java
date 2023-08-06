package domain;

import java.sql.*;

public class Customer {
    private final String customerId;
    private final String name;
    private final String surname;
    private int bonus;

    public Customer(String name, String surname, String customerId, Integer bonusPoints) {
        this.name = name;
        this.surname = surname;
        this.customerId = customerId;
        this.bonus = bonusPoints;
    }

    public int getBonus() {
        String sql = "Select bonus_points from customer_information" +
                " where customer_id = '" + this.customerId + "' ";
        readDataFromDB(sql);
        return this.bonus;
    }

    public void addBonus(int bonus) {
        this.bonus += bonus;
        String sql = "Update  customer_information " +
                " SET bonus_points = " + this.bonus +
                " where customer_id = '" + this.customerId + "' "; // we assume that a book can not be rented twice by the same customer and the same time.
        updateDB(sql);

    }

    public String getName() {

        return this.name;
    }

    public String getSurname() {

        return this.surname;
    }

    public String getCustomerId() {

        return this.customerId;
    }

    private void updateDB(String sql) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:h2:/Users/fatimaelbaghdadi/IdeaProjects/videoRentalStoreApplication/src/rentalDB")) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void readDataFromDB(String sql) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:h2:/Users/fatimaelbaghdadi/IdeaProjects/videoRentalStoreApplication/src/rentalDB")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                this.bonus = Integer.valueOf(resultSet.getString("bonus_points"));
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
