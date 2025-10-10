## 1. 기본 개념

**Labels (레이블)**

- Pod, Service 등에 붙이는 키-밸류 태그
- 예: `version: v1`, `app: myapp`

**Selector (셀렉터)**

- 특정 레이블을 가진 리소스를 선택하는 조건

---

## 2. 연결 구조 예시

Service
selector: version: v1  → "version: v1 레이블 Pod로 트래픽"

Deployment
selector.matchLabels: version: v1  → "version: v1 레이블 Pod 관리"
template.labels: version: v1  → "Pod에 이 레이블 부착"

**흐름**: Pod labels → Deployment matchLabels → Service selector

---

## 3. 각 Selector의 역할

| 위치 | 역할 |
| --- | --- |
| Service selector | 트래픽 전송 대상 선택 |
| Deployment matchLabels | 관리할 Pod 선택 |
| Pod labels | 실제 Pod에 붙는 레이블 |

---

## 4. 핵심 개념

### Service는 Deployment를 거치지 않는다!

Service → (label만 확인) → Pod로 직접 트래픽

- Service는 Deployment 존재를 모름
- label만 보고 Pod 선택
- 여러 Deployment의 Pod 동시 선택 가능

**예시**

- Deployment A: version: v1 (3개)
- Deployment B: version: v1 (1개)
- Service: version: v1 선택
→ 총 4개 Pod 모두에게 트래픽 전송

---

## 5. 핵심 규칙

1. Deployment 내부: selector.matchLabels와 template.labels 반드시 일치
2. Service: Pod labels만 맞으면 됨 (Deployment 무관)
3. 여러 레이블 사용 가능 (AND 조건)

---

## 6. 트래픽 전송 방법

| 방법 | Label 사용 | 추천도 |
| --- | --- | --- |
| 일반 Service | ✅ | ★★★★★ |
| Headless Service | ✅ | StatefulSet용 |
| 수동 Endpoints | ❌ | 외부 서비스용 |
| ExternalName | ❌ | 외부 DNS용 |

**결론**: Label selector가 표준

---

## 7. Blue-Green 배포 활용

Blue: labels.version: v1
Green: labels.version: v2

Service selector를 v1 → v2로 변경하여 즉시 전환