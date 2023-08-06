# videoRentalStoreApplication
The main goal of this project is putting in practice all the knowledge of Java creating a service for renting videos.

For a video rental store we want to create a system for managing the rental administration. 3 primary functions were implemented:  
- Have an inventory of films and customer					
- Calculate the price for rentals	and surcharges if the film was returned late.					
- Keep track of the customers “bonus” points


## Inventory of films/customers: 
To track the status of films and customers, 3 differt tables were created:
* customer_information: to add new customers (name, surname and customer_id) and update the bonus_points when customers rent films.

  <img width="590" alt="image" src="https://github.com/timaelbaghdadi/videoRentalStoreApplication/assets/63514409/1fe8fda8-8afa-441a-aca0-3630a94c6161">

* video_information: where we store all the available films using name, type of film and is_available (if a book is rent, then it is not available anymore for the next customer).

  <img width="680" alt="image" src="https://github.com/timaelbaghdadi/videoRentalStoreApplication/assets/63514409/7e326971-483b-4508-901e-0b4e58c7003b">

* rent_information: this table stores the data related to the rent itself by the name of customer, name of film and duration of rent, the cost of renting this film and the time when it was done. Additionaly, when the film is returned we check the how many days the book was returned late and the surcharge.

  <img width="1289" alt="image" src="https://github.com/timaelbaghdadi/videoRentalStoreApplication/assets/63514409/39886be2-2705-49d7-9e2d-8322796eb471">


## Price: 
The price of rentals is based type of film rented and how many days the film is rented for. The customers say when renting for how many days they want to rent for and pay up front. If the film is returned late, then rent for the extra days is charged when returning. The store has three types of films.			
* New releases – Price is <premium price> times number of days rented.
 						
* Regular films – Price is <basic price> for the first 3 days and then <basic price> times the number of days over 3.
 						
* Old film - Price is <basic price> for the first 5 days and then <basic price> times the number of days over 5
 											 					
premium price -> is 40 E 

basic price -> is 30 E


## Bonus points
Customers get bonus points when renting films. A new release gives 2 points and other films give one point per rental (regardless of the time rented). 


## Examples of price calculations			
* Matrix 11 (New release) 1 days 40 E Spider Man (Regular rental) 5 days 90 E Spider Man 2 (Regular rental) 2 days 30 E Out of Africa (Old film) 7 days 90 E. Total price: 250 E

* When returning films late: Matrix 11 (New release) 2 extra days 80 E Spider Man (Regular rental) 1 days 30 E. Total late charge: 110 E


## How project is distribuited:
The project is distribuited in 4 different parts:
* Domain: where we have just simple classes such as film/customer.
* Manager: where we have classes that acts as managers like CustomerManager responsible of changes at customer level, FilmManager responsible of change at film level, RentalService that acts as an intermediate between film, manager and rent.
* TextUI: where we have a class that interacts with the user.
* Main: main file that launches the application.
