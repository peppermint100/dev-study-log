package org.example.absractfatory;

public class Client {

    public static void main(String[] args) {
        ProductFactory mobileFactory = new MobileFactory();
        ProductFactory tabletFactory = new TabletFactory();

        // 사용하는 구현부에서는 구체적인 구현을 모르고 단순히 인터페이스에만 의존한다.

        /*
         둘다 똑같은 createAppleProduct를 호출하지만
         어떤 팩토리의 객체이냐에 따라 다른 제품을 반환한다.
         */
        AppleProduct appleMobile = mobileFactory.createAppleProduct();
        AppleProduct appleTablet = tabletFactory.createAppleProduct();
        SamsungProduct samsungTablet = tabletFactory.createSamsungProduct();
    }
}

/*
- 제품들이 다양한 제품과 관련되어 있으며,
  해당 제품의 구체적인 클래스에 의존하고 싶지 않고
  제품에 대한 라이브러리를 제공하며 구현이 아닌 인터페이스를 노출 시키고 싶을 때 사용한다.

- 객체 생성 코드를 분리하여 클라이언트와의 결합도를 낮춘다.
- 제품군을 쉽게 대체 할 수 있다.
- SRP, OCP 준수
- 코드의 복잡성이 증가한다.
- 기준 추상 팩토리의 세부 사항이 변경되면 모든 팩토리와 서브클래스를 수정해주어야 한다.
 */
