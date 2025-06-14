package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw02.HasLabels;

import java.util.Objects;
import java.util.Set;

public record Pokemon(String name, Set<String> labels, String total, String hitPoints, String attack,
                      String defense, String specialAttack, String specialDefense, String speed) implements HasLabels {

    public Pokemon {
        Objects.requireNonNull(name, "Name should not be null");
        Objects.requireNonNull(labels, "Labels should not be null");
        Objects.requireNonNull(total, "Total should not be null");
        Objects.requireNonNull(attack, "Attack should not be null");
        Objects.requireNonNull(defense, "Defense should not be null");
        Objects.requireNonNull(specialAttack, "Special attack should not be null");
        Objects.requireNonNull(specialDefense, "Special defense should not be null");
        Objects.requireNonNull(speed, "Speed should not be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pokemon)) return false;
        Pokemon pokemon = (Pokemon) o;
        return name.equals(pokemon.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s, %s, %s, %s", name, labelsToString(),
                total, attack, defense, specialAttack, specialDefense, speed);
    }

    public String labelsToString() {
        return String.join(" ", labels);
    }

    @Override
    public Set<String> getLabels() {
        return this.labels;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getDate() {
        return null;
    }

    @Override
    public String getHits() {
        return null;
    }
}
