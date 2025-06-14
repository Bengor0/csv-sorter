package cz.muni.fi.pb162.hw03.impl;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        if (set == null) {
            System.out.println("krv");
        } else if (set.size() == 1) {
            System.out.println("kkt");
        } else {
            System.out.println("ppc");
        }

    }
}
