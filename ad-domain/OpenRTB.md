```table-of-contents
```

# ORTB란?
OpenRTB (Open Real-Time Bidding)는 광고 구매자(DSP)와 판매자(SSP) 간의 실시간 광고 거래를 위한 개방형 산업 표준 프로토콜이다. 이 프로토콜은 이들 간의 통신을 위한 공통 언어를 만들어 실시간 입찰(RTB) 시장의 성장을 촉진하는 것을 목표로 한다.

ORTB는 2011년 초반에 모바일 광고 지원에 초점을 맞추어 출시했다가 이후에 비디오 광고 지원을 포함한 2.0이 출시되었다. 산업 전반적으로 폭넓게 채택되면서 현재는 IAB의 표준으로 채택되었다.

- IAB는 Interactive Advertising Bureau으 줄임말로, 디지털 광고 산업을 위한 표준을 개발하고 시행하는 비영리 무역 협회를 뜻한다.

# OpenRTB의 기본
OpenRTB는 익스체인지(SSP)와 비더(DSP) 간의 상호 작용과 그에 필요한 기술적 요소를 정의한 프로토콜이다.  이해가 쉽도록 설명하자면 SSP가 여러 DSP에게 브로드캐스팅 방식으로 동시에 광고 요청을 보내고, 그 중에 적절한 광고를 찾아서(광고가 적절한지에 대해서는 Bid Request라고 하는 광고 요청에 정의한다) 채택하는 것이다. 

즉 여러 DSP사에 광고를 요청하며 그 중에 1개의 광고를 승자(winner)로 채택한다.

ORTB 통신의 기본적인 규칙은 아래와 같다.
## 전송
- 통신 프로토콜로 HTTP를 사용하며 입찰 요청(Bid Request)는 HTTP Post Method를 사용한다. 
- 낙찰알림(Win Notice)에는 Post 혹은 Get을 사용한다. 
- 유효한 응답은 Http 200, 내용이 없는 경우는 204, 잘못된 요청을 400을 반환한다.

## 보안
- HTTPS가 필수는 아니나 권장한다.

## 데이터 형식
- Json을 권장하며 헤더에 Content-Type: application/json을 사용한다.
- Bid Response는 Bid Request와 동일해야 한다.

## 데이터 인코딩
- 네트워크 대역폭 절약을 위해 데이터 압축을 권장한다.
- Accpet-Encoding: gzip 헤더를 보내고, 비더는 Content-Encoding: gzip을 헤더로 응답할  수 있다.

---

# ORTB 요청

ORTB 프로토콜의 Bid Request 요청 예시는 아래와 같다.

```json
{
  "id": "80ce30c53c16e6ede735f123ef6e32361bfc7b22", // Bid Request의 고유 ID
  "at": 1, // 경매 유형: 1 = First Price (첫 번째 가격) 
  "cur": [ "USD" ], // 입찰에 허용되는 통화 (ISO-4217 알파 코드) 
  "imp": [ // 임프레션(광고 게재 기회) 객체 배열. 최소 1개 필수 
    {
      "id": "1", // 임프레션의 고유 식별자 
      "bidfloor": 0.03, // 이 임프레션에 대한 최소 입찰가 (CPM 기준) 
      "banner": { // 배너 광고 기회임을 나타내는 객체 
        "h": 250, // 배너의 정확한 높이 (DIPS) 
        "w": 300, // 배너의 정확한 너비 (DIPS) 
        "pos": 0 // 화면 내 광고 위치: 0 = Unknown (알 수 없음) 
      },
      "pmp": { // 개인 마켓플레이스(PMP) 컨테이너
        "private_auction": 1, // 경매 적격성 지표: 1 = 입찰이 지정된 거래 및 그 조건으로 제한됨 (비공개 경매) 
        "deals": [ // 이 임프레션에 적용 가능한 특정 거래(Deal) 객체들의 배열 
          {
            "id": "AB-Agency1-0001", // 직접 거래의 고유 식별자 
            "at": 1, // 이 거래에 대한 경매 유형 오버라이드: 1 = First Price (첫 번째 가격) 
            "bidfloor": 2.5, // 이 거래에 대한 최소 입찰가 (CPM 기준) 
            "wseat": [ "Agency1" ] // 이 거래에 입찰이 허용된 구매자 시트 화이트리스트 
          },
          {
            "id": "XY-Agency2-0001", // 또 다른 직접 거래의 고유 식별자 
            "at": 2, // 이 거래에 대한 경매 유형 오버라이드: 2 = Second Price Plus (두 번째 가격 플러스) 
            "bidfloor": 2, // 이 거래에 대한 최소 입찰가 (CPM 기준) 
            "wseat": [ "Agency2" ] // 이 거래에 입찰이 허용된 구매자 시트 화이트리스트 
          }
        ]
      }
    }
  ],
  "site": { // 퍼블리셔 웹사이트에 대한 세부 정보 (앱이 아닌 경우) 
    "id": "102855", // 익스체인지 고유 사이트 ID 
    "cat": [ "IAB3-1" ], // 사이트의 IAB 콘텐츠 카테고리: IAB3-1 = Advertising (광고) 
    "domain": "www.foobar.com", // 사이트 도메인 
    "page": "http://www.foobar.com/1234.html", // 임프레션이 표시될 페이지 URL 
    "publisher": { // 사이트 퍼블리셔에 대한 세부 정보 
      "id": "8953", // 익스체인지 고유 퍼블리셔 ID 
      "name": "foobar.com", // 퍼블리셔 이름
      "cat": [ "IAB3-1" ], // 퍼블리셔의 IAB 콘텐츠 카테고리 
      "domain": "foobar.com" // 퍼블리셔의 최상위 도메인 
    }
  },
  "device": { // 사용자가 상호 작용하는 기기에 대한 정보 
    "ua": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/537.13 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2", // 브라우저 사용자 에이전트 문자열 
    "ip": "123.145.167.10" // 기기에서 가장 가까운 IPv4 주소 
  },
  "user": { // 기기 사용자에 대한 정보
    "id": "55816b39711f9b5acf3b90e313ed29e51665623f" // 익스체인지 고유 사용자 ID
  }
}
```

요청 옆에 간단히 해당 값에 대한 설명이 있으나 추가 설명이 필요한 부분을 아래 더 정리해보았다.

## at
- auction type의 약자로 1은 first price, 2는 second price plus이다.
- first price는 입찰에 참여한 DSP중 가장 높은 가격을 제시한 광고를 채택한다.
- second price plus는 디폴트 값으로 입찰에 참여한 DSP중 가장 높은 가격을 제시한 광고를 채택하나, 그 가격은 두 번째로 가장 높은 가격에 미리 정해진 약간의 금액을 더한 가격을 지불하게 된다.

## imp
- imp는 Bid Request의 필수 속성중 하나로, 광고 노출 단위를 정의한다. 실제로 노출되는 위치, 형식, 입찰가 등 조건을 담고 있다.

### pmp
pmp는 private marketplace의 약자로 선별된 광고주만 경매에 참여할 수 있도록 하는 비공개 광고 거래방식을 정의한다. 모든 광고주가 참여하지 않고 퍼블리셔가 초대한 특정 DSP난 입찰에 참여 할수 있다.

이를 정하게 되는 값이 pmp 내부의 private auction 값이다. private auction이 1이면 pmp 거래만 허용하고 0이면 공개 입찰 . 도허용할 수 있다.

### deal
deal은 퍼블리셔와 DSP가 합의된 조건으로 거래할 수 있도록 정의된 거래 단위이다. 내부에 최소 가격, 통화, wseat을 통한 구매자 화이트리스트 등을 처리한다.

# ORTB의 거래 유형
거래 유형에는 보통 PG, PD, PA, OA가 있는데, ORTB의 Bid Request에는 이를 명시적으로 지정하는 값이 없다. 하지만 pmp 내부의 값을 통해 이러한 거래 유형을 표현할 수 있다.

## OA(open auction)
- ORTB의 기본 경매 방식으로 모든 입찰자에게 노출 기회가 공개되며 가격 경쟁을 통해 낙찰자가 결정된다.
- 입찰 요청시 pmp 객체가 없거나 pmp내부 private_auction이 0으로 설정된 경우

## PA(private auction)
- OA와 달리 사설 경매로, 특정 구매자에게만 입찰 자격이 주어진다.
- pmp내 private auction을 1을 주어 private auction을 활성화한다.
- deal 객체의 wseat을 통해 정해진 DSP만 참여하도록 설정한다.

## PD(preferred deal)
- 구매자와 판매자간 미리 합의된 조건으로 노출 기회를 제공한다. 
- deal 객체의 bidfloor로 최소 가격을 정한다.
- deal 객체의 at를 1 혹은 2로 설정하여 딜의 가격 경매. ㅠ형을 정한다.
- deal 객체의 wseat을 통해 정해진 DSP만 참여하도록 설정한다.

## PG(Programattic Guaranteed)
- SSP, DSP간 미리 합의된 고정 가격으로 노출 기회를 보장한다. 경매라기 보다는 합의된 가격으로의 구매 제안에 가깝다.
- deal 객체의 at를 3으로 설정한다.
	- 3은 bidfloor에 전달된 값이 합의된 거래 가격임을 의미한다. 이 가격이 최종 가격이며 경매 없이 해당 가격으로 노출 기회를 구매하겠다는 뜻이다.
- deal 객체의 wseat을 통해 정해진 DSP만 참여하도록 설정한다.

# ORTB 응답
```json
{
  "id": "1234567890", // 이 응답이 속한 입찰 요청의 고유 ID (Bid Request의 id와 동일) 
  "bidid": "abc1123", // 로깅/추적을 돕기 위해 입찰자가 생성한 응답 ID. 낙찰 통지에 포함될 수 있음
  "cur": "USD",       // 입찰에 사용된 통화 (ISO-4217 알파 코드). 기본값은 "USD"
  "seatbid": [        // 특정 구매자 좌석(seat)을 대표하여 입찰자가 제출한 입찰들의 컬렉션 배열 
    {
      "seat": "512",  // 이 입찰이 이루어진 구매자 좌석의 ID (예: 광고주, 대행사)
      "bid": [        // 하나 이상의 Bid 객체 배열
        {
          "id": "1",           // 로깅/추적을 돕기 위해 입찰자가 생성한 입찰 ID 
          "impid": "102",      // 관련 입찰 요청에 있는 Imp 객체의 ID 
          "price": 9.43,       // CPM(Cost Per Mille)으로 표시된 입찰 가격 
          "nurl": "http://adserver.com/winnotice?impid=102", // 입찰이 낙찰될 경우 익스체인지가 호출하는 낙찰 통지(Win Notice) URL
          "iurl": "http://adserver.com/pathtosampleimage", // 광고 품질/안전 확인을 위한 캠페인 콘텐츠를 대표하는 이미지의 URL 
          "adomain": [ "advertiserdomain.com" ], // 블록 리스트 확인을 위한 광고주 도메인
          "cid": "campaign111", // 광고 품질 확인을 돕는 캠페인 ID 
          "crid": "creative112", // 광고 품질 확인을 돕는 크리에이티브 ID 
          "attr": [ 1, 2, 3, 4, 5, 6, 7, 12 ] // 크리에이티브를 설명하는 속성 집합. (List 5.3 참조)
        }
      ]
    }
  ]
}
```

요청의 응답의 예시는 위와 같다. 요청 id와 같은 id를 보내주고 광고 요청의 결과를 seatbid에 담아서 보내준다. seatbid 내에는 3개의 url이 있는데, nurl, iurl, lurl, burl이다. 

- nurl은 win url의 약자로 이 광고를 채택한다면 DSP사에 채택했다고 알려주는 url이다.
- lurl은 lose url의 약자로 이 광고를 채택하지 않는다면 채택하지 않았다고 DSP사에 알려주는 url이다. 이 값은 필수값은 아니어서 제외해도 된다.
- iurl은 광고 품질, 안정성 확인을 검증하기 위해 사용하는 대표 이미지 url이다.
- burl은 과금 통지 url로 승리한 입찰이 과금 상태가 될 때 SSP가 호출하는 URL이다. 일반적으로 광고가 실제로 소비자에게 전달되거나 view된 시점을 의미한다. 경매에 낙찰된 것만으로 과금을 보장하지 않으므로 구매자가 실제로 비용을 지불해야 하는 시점을 알려준다.
