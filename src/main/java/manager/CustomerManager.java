package manager;

import domain.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerManager {

    private final List<Customer> customerList;

    public CustomerManager() {
        this.customerList = new ArrayList<Customer>();
        readDataFromDb();
    }

    public void addCustomer(@org.jetbrains.annotations.NotNull Customer customer) {
        String sql = "INSERT INTO customer_information (customer_id, name, surname, bonus_points) " +
                "VALUES ('" + customer.getCustomerId() + "' , '" + customer.getName() + "' , '" + customer.getSurname() + "' , '" + customer.getBonus() + "') ";

        updateDB(sql);
        readDataFromDb();
    }

    public boolean isCustomerRegistered(String customerID) {
        for (Customer customer : this.customerList) {
            if (customer.getCustomerId().equals(customerID)) {
                return true;
            }
        }
        return false;
    }

    public void readDataFromDb() {
        String sql = "Select * from customer_information";
        try (Connection connection = DriverManager.getConnection("jdbc:h2:/Users/fatimaelbaghdadi/IdeaProjects/videoRentalStoreApplication/src/rentalDB")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);


            while (resultSet.next()) {
                this.customerList.add(
                        new Customer(resultSet.getString("name"),
                                resultSet.getString("surname"),
                                resultSet.getString("customer_id"),
                                Integer.valueOf(resultSet.getString("bonus_points"))
                        ));
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDB(String sql) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:/Users/fatimaelbaghdadi/IdeaProjects/videoRentalStoreApplication/src/rentalDB")) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
