package testVideoRentalApp;

import domain.Customer;
import domain.Film;
import manager.FilmManager;
import manager.RentalService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class TestFile {
    RentalService rentalService = new RentalService();
    Customer customer1 = new Customer("Fatima", "El baghdadi", "31912604N", 0);
    Film film1 = new Film("Matrix 11", "new", true);
    Film film2 = new Film("Spider Man", "regular", true);
    Film film3 = new Film("Spider Man 2", "regular", true);
    Film film4 = new Film("Out of Africa", "old", true);
    Film film5 = new Film("Spider Man", "regular", true);
    Film film6 = new Film("Spider Man 2", "regular", true);
    Film film7 = new Film("Out of Africa", "old", true);

    @Test
    void checkTotalCost() {
        rentalService.addFilmsPerCustomer(customer1, film1, 1);
        rentalService.addFilmsPerCustomer(customer1, film2, 5);
        rentalService.addFilmsPerCustomer(customer1, film3, 2);
        rentalService.addFilmsPerCustomer(customer1, film4, 7);
        Set<Customer> customersRegistered = rentalService.getRegisteredCustomers();
        for (Customer customer : customersRegistered) {
            System.out.println("Expected results of the rent: 250 € and the current result if " +
                    rentalService.calculateTotalCost(customer, false) + " €" );
            Assertions.assertEquals(250, rentalService.calculateTotalCost(customer, false));
        }
    }
    @Test
    void checkTotalCostAfterDelay() {
        rentalService.removeFilmPerCustomer(customer1, film1, 2);
        rentalService.removeFilmPerCustomer(customer1, film2, 1);
        rentalService.removeFilmPerCustomer(customer1, film3, 0);
        rentalService.removeFilmPerCustomer(customer1, film4, 0);
        Set<Customer> customersRegistered = rentalService.getRegisteredCustomers();
        for (Customer customer : customersRegistered) {
            System.out.println("Expected results after returning the films: 110 € and the current result if " +
                    rentalService.calculateTotalCost(customer, true) + " €" );
            Assertions.assertEquals(110, rentalService.calculateTotalCost(customer, true));
        }
    }

    @Test
    void checkBonusPointsByCustomer() {
        System.out.println("Expected bonus points are 5 and the current bonus Points are " +
                customer1.getBonus());
            Assertions.assertEquals(5, customer1.getBonus());
    }


}
