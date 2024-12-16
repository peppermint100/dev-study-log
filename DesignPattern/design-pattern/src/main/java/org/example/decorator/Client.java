package org.example.decorator;

public class Client {

    public static void main(String[] args) {

        Coffee coffee = new Espresso();

        Coffee oatMilk = new OatMilkDecorator(coffee);
        Coffee vanillaSyrup = new VanillaSyrupDecorator(coffee, 3);

        System.out.println("coffee.cost() = " + coffee.cost());
        System.out.println("oatMilk.cost() = " + oatMilk.cost());
        System.out.println("vanillaSyrup.cost() = " + vanillaSyrup.cost());

        Coffee vanillaOatLatte =
                new OatMilkDecorator(new VanillaSyrupDecorator(coffee, 3));

        System.out.println("vanillaOatLatte.cost() = " + vanillaOatLatte.cost());

    }
}

/*
데코레이터 패턴
객체에 대해서 확장, 기능 변경이 필요할 때 객체의 결합을 통해 확장, 변경하는 패턴
- 필요 기능의 조합을 런타임에서 동적으로 생성한다.
- 서브 클래싱보다 유연하게 기능을 확장한다.
- 각 클래스마다 역할이 있으므로 SRP 준수
- 데코레이터를 수평적으로 계속 추가할 수 있으므로 OCP 준수
- 구현체가 아닌 인터페이스를 바라봄으로서 DIP 준수
 */
