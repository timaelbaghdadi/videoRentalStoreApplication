package manager;

import domain.Film;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmManager {
    private final List<Film> listOfFilms;

    public FilmManager(Boolean isAvailable) {
        this.listOfFilms = new ArrayList<>();
        String sql = "Select * from video_information where is_available = " + isAvailable + " ";
        readDataFromDb(sql);
    }

    public String getFilms() {
        String output = "";
        for (Film film : this.listOfFilms) {
            output = output + film.toString();
        }
        return output;
    }

    public Film getFilmByName(String name) {
        for (Film film : this.listOfFilms) {
            if (film.getNameofFilm().equals(name)) {
                return film;
            }
        }
        return new Film("", "", true);
    }

    public void readDataFromDb(String sql) {// Just show available films
        try (Connection connection = DriverManager.getConnection("jdbc:h2:/Users/fatimaelbaghdadi/IdeaProjects/videoRentalStoreApplication/src/rentalDB")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                this.listOfFilms.add(
                        new Film(resultSet.getString("film_name"), // name
                                resultSet.getString("type"), // type
                                Boolean.valueOf(resultSet.getString("is_available")) // Availability
                        ));

            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateDB(String sql) { // update status of films
        try (Connection connection = DriverManager.getConnection("jdbc:h2:/Users/fatimaelbaghdadi/IdeaProjects/videoRentalStoreApplication/src/rentalDB")) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
