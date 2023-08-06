package textUi;

import domain.Customer;
import domain.Film;
import manager.CustomerManager;
import manager.FilmManager;
import manager.RentalService;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

public class UI {

    public UI() {
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        RentalService rentalService = new RentalService();

        System.out.println("Welcome to GlovoVideo rental store!!!");
        Customer customer = getInfoFromUser();
        System.out.println("How can help you " + customer.getName() + " ? Please, select the 1 option of the following ones: (e.g 1)");
        System.out.println("1 - Rent videos");
        System.out.println("2 - Return videos");
        System.out.println("3 - Check my bonus points");

        Integer option = Integer.valueOf(scanner.nextLine());
        if (option == 1) {
            while (true) {
                FilmManager filmManager = new FilmManager(true);
                System.out.println("Please enter the exact name of the film that you want to rent");
                System.out.println(filmManager.getFilms());
                String nameOfFilm = scanner.nextLine();
                Film filmSelected = filmManager.getFilmByName(nameOfFilm);
                System.out.println("How many days do you want to rent it?");
                Integer duration = Integer.valueOf(scanner.nextLine());
                rentalService.addFilmsPerCustomer(customer, filmSelected, duration);
                System.out.println("Do you want to rent another book?(Y/N)");
                String boolNextRent = scanner.nextLine();
                if (boolNextRent.equals("N")) {
                    break;
                }
            }
            System.out.println("Your total cost is " +
                    rentalService.calculateTotalCost(customer, false) + " €");

        } else if (option == 2) {
            while (true) {
                FilmManager filmManager = new FilmManager(false);
                System.out.println("Please enter the exact name of your rented films that you want to return");
                System.out.println(rentalService.getFilmsRentedPerCustomer(customer));
                String nameOfFilm = scanner.nextLine();
                Film filmSelected = filmManager.getFilmByName(nameOfFilm);
                System.out.println("How long have you delayed the book? ( 0 is no delay, 1 is one day of delay, and so on");
                Integer daysDelayed = Integer.valueOf(scanner.nextLine());
                rentalService.removeFilmPerCustomer(customer, filmSelected, daysDelayed);
                System.out.println("Do you want to return another book?(Y/N)");
                String boolNextRent = scanner.nextLine();
                if (boolNextRent.equals("N")) {
                    break;
                }
            }
            System.out.println("Your total cost is " +
                    rentalService.calculateTotalCost(customer, true) + " €");
        } else if (option == 3) {
            System.out.println(" Your current Bonus Points are" + customer.getBonus());
        } else {
            System.out.println("Selected option doesn't exist, by!!");
            System.exit(0);
        }
    }

    @Contract(" -> new")
    private @NotNull Customer getInfoFromUser() {
        CustomerManager customerManager = new CustomerManager();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please, provide us your name");
        String name = scanner.nextLine();
        System.out.println("Please, provide us your surname");
        String surname = scanner.nextLine();
        System.out.println("Please, provide us your customerId");
        String customerId = scanner.nextLine();
        if (!customerManager.isCustomerRegistered(customerId)) {
            System.out.println("Welcome " + name + "! You are a new user, right? We will register you now :) ");
            customerManager.addCustomer(new Customer(name, surname, customerId, 0));
            System.out.println("Done, now you can start using our service.");
        }
        return new Customer(name, surname, customerId, 0);
    }
}
