package org.example.decorator;

public interface Coffee {
    int cost();
}

class Espresso implements Coffee {

    @Override
    public int cost() {
        return 4000;
    }
}
