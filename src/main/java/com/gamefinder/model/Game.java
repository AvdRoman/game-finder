package com.gamefinder.model;

public class Game {
    private String name;
    private String released;
    private double rating;
    private String backgroundImage;

    public Game() {}

    public Game(String name, String released, double rating, String backgroundImage) {
        setName(name);
        setReleased(released);
        setRating(rating);
        setBackgroundImage(backgroundImage);
    }

    public void setName(String name) {
        if (name == null || name.isBlank() || "null".equalsIgnoreCase(name)) {
            this.name = "Неизвестно";
        } else {
            this.name = name;
        }
    }

    public String getName() {
        return name;
    }

    public void setReleased(String released) {
        if (released == null || released.isBlank() || "null".equalsIgnoreCase(released)) {
            this.released = "Дата не указана";
        } else {
            this.released = released;
        }
    }

    public String getReleased() {
        return released;
    }

    public void setRating(double rating) {
        // Вместо выброса ошибки ограничиваем значение от 0.0 до 5.0
        if (rating < 0) {
            this.rating = 0.0;
        } else if (rating > 5) {
            this.rating = 5.0;
        } else {
            this.rating = rating;
        }
    }

    public double getRating() {
        return rating;
    }

    public void setBackgroundImage(String backgroundImage) {
        if (backgroundImage == null) {
            this.backgroundImage = "";
        } else {
            this.backgroundImage = backgroundImage.trim();
        }
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    @Override
    public String toString() {
        return "Game{" +
                "name='" + name + '\'' +
                ", released='" + released + '\'' +
                ", rating=" + rating +
                ", backgroundImage='" + backgroundImage + '\'' +
                "}";
    }
}
