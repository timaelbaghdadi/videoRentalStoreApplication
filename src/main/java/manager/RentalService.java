package manager;

import domain.Customer;
import domain.Film;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.HashMap;
import java.util.Set;

public class RentalService {
    private final String url = "jdbc:h2:/Users/fatimaelbaghdadi/IdeaProjects/videoRentalStoreApplication/src/rentalDB";
    private final HashMap<Customer,
            HashMap<Film, Integer>> filmsAndDurationPerCustomer = new HashMap<>();
    private final int premiumPrice;
    private final int basicPrice;
    private final int daysFlatFeeRegular;
    private final int daysFlatFeeOld;
    FilmManager filmManager;
    CustomerManager customerManager;

    // HashMap that contains per each customer the films rented and duration
    public RentalService() {
        this.premiumPrice = 40; // Default values given by the statement
        this.basicPrice = 30;
        this.daysFlatFeeRegular = 3;
        this.daysFlatFeeOld = 5;
        this.filmManager = new FilmManager(true);
        this.customerManager = new CustomerManager();
    }

    public void addFilmsPerCustomer(@NotNull Customer customer, Film film, int rentedDays) {
        String sqlRentInformation = "Select * from rent_information where customer_id = '" +
                customer.getCustomerId() + "' and is_returned is false";
        readDataFromDb(sqlRentInformation, customer);
        Integer costFilm = calculateExpectedCostPerFilm(film, rentedDays);

        String sql = "INSERT INTO rent_information (film_name, customer_id, rented_days, expected_cost, is_returned," +
                "days_delayed, extra_surcharge, rent_activation_time) " +
                "VALUES ('" + film.getNameofFilm() + "' , '" + customer.getCustomerId() +
                "' , '" + rentedDays + "' , '" + costFilm + "' , '" + false + "' , '" + 0 + "' , '" + 0 + "', '" + java.time.LocalDateTime.now() + "') ";

        updateDB(sql); // add rent of customer-film in rent_information table
        readDataFromDb(sqlRentInformation, customer); // show films that customer_id rented
        film.updateAvailability();

        if (film.getTypeOfFilm().equals("new")) {
            customer.addBonus(2);
        } else {
            customer.addBonus(1);
        }

    }

    public void removeFilmPerCustomer(@NotNull Customer customer, Film film, int delayedDays) {
        String sqlRentInformation = "Select * from rent_information where customer_id = '" +
                customer.getCustomerId() + "' and is_returned is false";
        readDataFromDb(sqlRentInformation, customer);
        Integer costDelayedFilm = calculateExpectedCostPerFilm(film, delayedDays);

        String sql = "Update  rent_information " +
                "SET is_returned = true, days_delayed = " + delayedDays +
                ", extra_surcharge = " + costDelayedFilm +
                " where film_name = '" + film.getNameofFilm() + "' and customer_id = '" +
                customer.getCustomerId() + "'"; // we assume that a book can not be rented twice by the same customer andn the same time.

        updateDB(sql); // update status of rent as completed as is_returned is not null anymore
        film.updateAvailability(); // film is available to be rent again
        readDataFromDb(sqlRentInformation, customer); // show films that customer_id rented
    }

    public Set<Customer> getRegisteredCustomers() {
        return this.filmsAndDurationPerCustomer.keySet();
    }

    public String getFilmsRentedPerCustomer(@NotNull Customer customer) {
        String rentedFilmsPerCustomer = "";
        String sql = "Select film_name from rent_information where customer_id = '" +
                customer.getCustomerId() + "' and is_returned is false";
        try (Connection connection = DriverManager.getConnection(
                "jdbc:h2:/Users/fatimaelbaghdadi/IdeaProjects/videoRentalStoreApplication/src/rentalDB")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                rentedFilmsPerCustomer = rentedFilmsPerCustomer + resultSet.getString("film_name") + "\n";
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentedFilmsPerCustomer;

    }

    // Calculate expected total cost for the set of films rented by customer
    public int calculateTotalCost(Customer customer, Boolean is_returned) {
        Integer totalCost = 0;
        String sql = "";

        if (!is_returned) {
            sql = "Select sum(expected_cost) as total_cost from rent_information where customer_id = '" +
                    customer.getCustomerId() + "' and is_returned is false";
        } else {
            sql = "Select sum(extra_surcharge) as total_cost from rent_information " +
                    "inner join (select customer_id, film_name, max(rent_activation_time) as recent_rent from rent_information where customer_id =  '" +
                    customer.getCustomerId() + "' group by customer_id, film_name ) as rr on rr.customer_id = rent_information.customer_id and rr.film_name = rent_information.film_name and rr.recent_rent <= rent_information.rent_activation_time " +
                    " where rent_information.customer_id = '" +
                    customer.getCustomerId() + "' and is_returned is true";
        }
        try (Connection connection = DriverManager.getConnection(
                "jdbc:h2:/Users/fatimaelbaghdadi/IdeaProjects/videoRentalStoreApplication/src/rentalDB")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                totalCost += Integer.parseInt(resultSet.getString("total_cost"));
                System.out.println("totalCost");
                System.out.println(totalCost);
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCost;
    }

    // Calculate cost per film given the type of film and duration -> internal method
    private int calculateExpectedCostPerFilm(@NotNull Film film, int durationOfRenting) {
        if (film.getTypeOfFilm().contains("new")) {
            return durationOfRenting * this.premiumPrice;
        } else if (film.getTypeOfFilm().contains("regular")) {
            return calculateIntermediateCost(durationOfRenting,
                    this.daysFlatFeeRegular, this.basicPrice);
        } else if (film.getTypeOfFilm().contains("old")) {
            return calculateIntermediateCost(durationOfRenting,
                    this.daysFlatFeeOld, this.basicPrice);
        }
        return -1; // Case when the film type doesn't fit any option above
    }

    // Intermediate function that helps us to calculate fees when we
    // have flat fee for x days -> internal method
    private int calculateIntermediateCost(int durationOfRenting, int daysFlatFee, int cost) {
        if (durationOfRenting > 0) {
            if (durationOfRenting > daysFlatFee) {
                return cost + cost * (durationOfRenting - daysFlatFee);
            } else {
                return cost;
            }
        }
        return 0;
    }

    public void readDataFromDb(String sql, Customer customer) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:h2:/Users/fatimaelbaghdadi/IdeaProjects/videoRentalStoreApplication/src/rentalDB")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);


            HashMap<Film, Integer> films = new HashMap<>();
            while (resultSet.next()) {
                films.put(filmManager.getFilmByName(
                                resultSet.getString("film_name")),
                        Integer.valueOf(resultSet.getString("rented_days")));
            }
            this.filmsAndDurationPerCustomer.putIfAbsent(customer, films);
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDB(String sql) { // update status of films
        try (Connection connection = DriverManager.getConnection(
                "jdbc:h2:/Users/fatimaelbaghdadi/IdeaProjects/videoRentalStoreApplication/src/rentalDB")) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
