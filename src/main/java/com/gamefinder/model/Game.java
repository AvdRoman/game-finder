package com.gamefinder.model;

public class Game {
    private String name;
    private String released;
    private double rating;
    private String backgroundImage;

    public Game() {
    }

    public Game(String name, String released, double rating, String backgroundImage) {
        setName(name);
        setReleased(released);
        setRating(rating);
        setBackgroundImage(backgroundImage);
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Название игры не может быть пустым");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setReleased(String released) {
        if (released != null && released.trim().isEmpty())
            throw new IllegalArgumentException("Дата выхода не может быть пустой");
        this.released = released;
    }

    public String getReleased() {
        return released;
    }

    public void setRating(double rating) {
        if (rating < 0 || rating > 5)
            throw new IllegalArgumentException("Рейтинг должен быть от 0 до 5");
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
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
