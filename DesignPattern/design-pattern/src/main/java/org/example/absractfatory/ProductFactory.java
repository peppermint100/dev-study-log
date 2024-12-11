package org.example.absractfatory;

// 삼성, 애플의 제품을 생산하는 팩토리
// 아래 두 팩토리는 같은 메소드를 생성하지만 각각 다른 결과를 만들어 낸다.
interface ProductFactory {
    SamsungProduct createSamsungProduct();
    AppleProduct createAppleProduct();
}

// 각 제조사의 모바일폰을 생성
class MobileFactory implements ProductFactory {
    public SamsungProduct createSamsungProduct() {
        return new SamsungGalaxyS20();
    }

    public AppleProduct createAppleProduct() {
        return new iPadAir11();
    }
}

// 각 제조사의 태블릿을 생성
class TabletFactory implements ProductFactory {
    public SamsungProduct createSamsungProduct() {
        return new SamsungGalaxyS20();
    }

    public AppleProduct createAppleProduct() {
        return new iPhone15Pro();
    }
}

