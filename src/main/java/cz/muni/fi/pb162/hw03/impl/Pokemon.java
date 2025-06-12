package cz.muni.fi.pb162.hw03.impl;

import java.util.Objects;
import java.util.Set;

public class Pokemon implements IsPokemon {
    private final String name;
    private final Set<String> labels;
    private final String total;
    private final String attack;
    private final String defense;
    private final String specialAttack;
    private final String specialDefense;
    private final String speed;

    public Pokemon(String name, Set<String> labels, String total, String attack,
                   String defense, String specialAttack, String specialDefense, String speed) {
        Objects.requireNonNull(name, "Name should not be null");
        Objects.requireNonNull(labels, "Labels should not be null");
        Objects.requireNonNull(total, "Total should not be null");
        Objects.requireNonNull(attack, "Attack should not be null");
        Objects.requireNonNull(defense, "Defense should not be null");
        Objects.requireNonNull(specialAttack, "Special attack should not be null");
        Objects.requireNonNull(specialDefense, "Special defense should not be null");
        Objects.requireNonNull(speed, "Speed should not be null");

        this.name = name;
        this.labels = labels;
        this.total = total;
        this.attack = attack;
        this.defense = defense;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.speed = speed;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Set<String> getLabels() {
        return this.labels;
    }

    @Override
    public String getTotal() {
        return this.total;
    }

    @Override
    public String getAttack() {
        return this.attack;
    }

    @Override
    public String getDefense() {
        return this.defense;
    }

    @Override
    public String getSpecialAttack() {
        return this.specialAttack;
    }

    @Override
    public String getSpecialDefense() {
        return this.specialDefense;
    }

    @Override
    public String getSpeed() {
        return this.speed;
    }
}
