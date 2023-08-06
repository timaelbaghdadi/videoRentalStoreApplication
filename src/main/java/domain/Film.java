package domain;

import java.sql.*;

public class Film {
    private final String nameOfFilm;
    private final String typeOfFilm;
    private Boolean available;

    public Film(String nameOfFilm, String typeOfFilm, Boolean available) {
        this.nameOfFilm = nameOfFilm;
        this.typeOfFilm = typeOfFilm;
        this.available = available;
    }

    public String getTypeOfFilm() {

        return this.typeOfFilm;
    }

    public boolean getAvailability() {
        String sql = "Select is_available from video_information " +
                " where film_name = '" + this.nameOfFilm + "' " +
                "and type = '" + this.typeOfFilm + "'";
        try (Connection connection = DriverManager.getConnection(
                "jdbc:h2:/Users/fatimaelbaghdadi/IdeaProjects/videoRentalStoreApplication/src/rentalDB")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                this.available =  Boolean.valueOf(resultSet.getString("is_available"));
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.available;
    }
    public void updateAvailability() {
        this.available = !getAvailability();
        String sql = "Update  video_information " +
                " SET is_available = " + this.available +
                " where film_name = '" + this.nameOfFilm + "' " +
                "and type = '" + this.typeOfFilm + "'"; // we assume that a book can not be rented twice iby the same customer and the same time.
        try (Connection connection = DriverManager.getConnection(
                "jdbc:h2:/Users/fatimaelbaghdadi/IdeaProjects/videoRentalStoreApplication/src/rentalDB")) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getNameofFilm() {
        return this.nameOfFilm;
    }

    @Override
    public String toString() {
        return this.nameOfFilm + "\n";
    }
}
