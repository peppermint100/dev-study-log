package org.example.decorator;

abstract class CoffeeDecorator implements Coffee {

    Coffee coffee;

    CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }

    @Override
    public int cost() {
        return coffee.cost();
    }
}

class OatMilkDecorator extends CoffeeDecorator {

    OatMilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public int cost() {
        return super.cost() + 500;
    }
}

class VanillaSyrupDecorator extends CoffeeDecorator {

    private int count;

    VanillaSyrupDecorator(Coffee coffee, int count) {
        super(coffee);
        this.count = count;
    }

    @Override
    public int cost() {
        return super.cost() + 300 * this.count;
    }
}

