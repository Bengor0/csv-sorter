package cz.muni.fi.pb162.hw03.impl;

import java.util.Set;

/**
 * Representation of a pokemon.
 */
public interface IsPokemon {
    String getName();

    Set<String> getLabels();

    String getTotal();

    String getAttack();

    String getDefense();

    String getSpecialAttack();

    String getSpecialDefense();

    String getSpeed();
}
