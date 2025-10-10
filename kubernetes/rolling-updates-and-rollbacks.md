# Kubernetes 배포 전략 - 무중단 업데이트의 두 가지 방식

## 1. RollingUpdate 전략

애플리케이션을 단계적으로 업데이트하여 서비스 중단 없이 새 버전으로 전환하는 배포 전략이다. 일부 Pod만 새 버전으로 교체하면서 점진적으로 전환하므로, 사용자는 업데이트 중에도 서비스를 계속 이용할 수 있다.

### 주요 설정

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app
spec:
  replicas: 4
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxUnavailable: 25%  # 동시에 정지 가능한 최대 Pod 비율
      maxSurge: 25%        # 동시에 생성 가능한 추가 Pod 비율
  minReadySeconds: 20      # Pod가 Ready 상태가 된 후 대기 시간
  template:
    spec:
      containers:
      - name: app
        image: my-app:v2
        ports:
        - containerPort: 8080

```

### 주요 명령어

```bash
# 배포 생성 또는 업데이트
kubectl apply -f deployment.yaml

# 이미지 버전 변경 (직접 편집)
kubectl edit deployment my-app

# 배포 상태 확인
kubectl describe deployment my-app

# 실시간 업데이트 과정 확인
kubectl get pods -w

```

### 업데이트 과정 예시

```bash
# RollingUpdate 진행 중 상태
# Replicas: 4 desired | 2 updated | 5 total | 3 available | 2 unavailable
# - 기존 Pod 3개 실행 중
# - 새 버전 Pod 2개 생성됨
# - 전체 5개 (구 3 + 신 2)

# Events 예시:
# Scaled up new ReplicaSet from 0 to 1    -> 새 Pod 1개 생성
# Scaled down old ReplicaSet from 4 to 3   -> 구 Pod 1개 제거
# Scaled up new ReplicaSet from 1 to 2     -> 새 Pod 추가 생성

```

### 핵심 포인트

- **점진적 전환**: 구버전과 신버전이 잠시 공존하며, 트래픽이 양쪽에 모두 분산된다
- **maxUnavailable**: 값이 작을수록 안정적이지만 업데이트 시간이 길어진다
- **maxSurge**: 추가 리소스가 필요하지만 더 빠른 배포가 가능하다
- **minReadySeconds**: 새 Pod가 안정화될 시간을 확보하여 문제를 조기에 발견한다
- **기본 전략**: Kubernetes에서 별도 설정이 없으면 RollingUpdate가 기본값이다

---

## 2. Recreate 전략

모든 기존 Pod를 먼저 삭제한 후, 새 버전의 Pod를 생성하는 배포 전략이다. 구버전과 신버전이 동시에 실행되지 않아야 하는 경우 사용한다.

### 주요 설정

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app
spec:
  replicas: 4
  strategy:
    type: Recreate  # maxUnavailable, maxSurge 설정 불필요
  template:
    spec:
      containers:
      - name: app
        image: my-app:v3
        ports:
        - containerPort: 8080

```

### 배포 전략 변경

```bash
# 배포 전략을 Recreate로 변경
kubectl edit deployment my-app
# strategy.type을 'Recreate'로 수정 후 저장

# 변경 확인
kubectl describe deployment my-app | grep StrategyType
# 출력: StrategyType: Recreate

```

### 업데이트 과정 예시

```bash
# Recreate 진행 과정
# 1. 모든 구버전 Pod 동시 종료
# 2. 잠시 다운타임 발생 (모든 Pod 0개)
# 3. 새 버전 Pod 4개 한꺼번에 생성

# Events 예시:
# Scaled down old ReplicaSet from 4 to 0   -> 모든 구 Pod 삭제
# Scaled up new ReplicaSet from 0 to 4     -> 모든 신 Pod 생성

```

### 핵심 포인트

- **완전한 교체**: 구버전이 완전히 종료된 후 신버전이 시작된다
- **다운타임 발생**: 짧지만 서비스 중단 시간이 존재한다
- **리소스 효율적**: 동시에 실행되는 Pod 수가 replicas를 초과하지 않는다
- **사용 케이스**: 데이터베이스 스키마 변경, 싱글톤 애플리케이션, 상태 충돌 방지가 필요한 경우

---

## 3. 전략 비교 및 선택 가이드

| 구분 | RollingUpdate | Recreate |
| --- | --- | --- |
| **다운타임** | 없음 (무중단) | 있음 (짧은 중단) |
| **리소스 사용** | 일시적 증가 (maxSurge) | 변동 없음 |
| **버전 공존** | 가능 (혼재 트래픽) | 불가능 |
| **롤백** | 점진적 복구 가능 | 즉시 재배포 필요 |
| **복잡도** | 높음 (설정 튜닝 필요) | 낮음 (설정 단순) |
| **적합한 상황** | 대부분의 웹 서비스 | DB 마이그레이션, 파일 시스템 변경 |

### 언제 무엇을 사용할까?

**RollingUpdate 추천:**

- REST API, 웹 애플리케이션 등 무중단이 중요한 서비스
- 구/신 버전이 동시 실행되어도 문제없는 경우
- 트래픽이 많아 서비스 중단이 치명적인 경우

**Recreate 추천:**

- 데이터베이스 스키마 변경이 필요한 업데이트
- 상태를 공유하는 애플리케이션 (싱글톤, 파일 잠금 등)
- 개발/테스트 환경에서 빠른 재배포가 필요한 경우

### 실전 팁

```bash
# 배포 진행 중 롤백
kubectl rollout undo deployment my-app

# 특정 버전으로 롤백
kubectl rollout undo deployment my-app --to-revision=1

# 배포 히스토리 확인
kubectl rollout history deployment my-app

# 배포 일시 정지/재개
kubectl rollout pause deployment my-app
kubectl rollout resume deployment my-app
```