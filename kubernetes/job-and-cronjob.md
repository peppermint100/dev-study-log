## 1. Jobs - 일회성 작업 보장

### 개념

Pod는 실패 시 그냥 종료되지만, Job은 작업이 성공할 때까지 자동으로 재시도하여 완료를 보장한다. 배치 처리, 데이터 마이그레이션 등 반드시 완료되어야 하는 작업에 사용한다.

### 주요 설정

```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: throw-dice-job
spec:
  completions: 3        # 3번 성공해야 완료
  parallelism: 1        # 동시 실행 Pod 수
  backoffLimit: 6       # 최대 재시도 횟수
  template:
    spec:
      containers:
      - name: throw-dice
        image: kodekloud/throw-dice
      restartPolicy: Never  # Job은 Always 불가

```

### 기본 명령어

```bash
# Job 생성 (템플릿)
kubectl create job my-job --image=my-image --dry-run=client -o yaml > job.yaml

# 상태 확인
kubectl get job
# COMPLETIONS: 2/3 → 3개 중 2개 성공

kubectl describe job my-job
# Pods Statuses: 0 Active / 2 Succeeded / 1 Failed

# Job 수정 불가 → 삭제 후 재생성
kubectl delete job my-job

```

### 핵심 포인트

- **completions**: 몇 번 성공해야 완료되는가
- **parallelism**: 동시에 몇 개를 실행할 것인가
- **backoffLimit**: 최대 재시도 횟수 (기본값 6)
- **수정 불가**: Job은 생성 후 수정이 불가능하므로 삭제 후 재생성해야 한다
- **restartPolicy**: Never 또는 OnFailure만 사용 가능 (Always는 불가)

---

## 2. CronJobs - 주기적 작업 스케줄링

### 개념

Linux cron과 동일한 스케줄 문법으로 주기적으로 Job을 자동 실행한다. 백업, 리포트 생성, 정기 배치 작업에 사용한다.

### 주요 설정

```yaml
apiVersion: batch/v1
kind: CronJob
metadata:
  name: throw-dice-cron-job
spec:
  schedule: "30 21 * * *"  # 매일 21:30 실행
  jobTemplate:              # Job 템플릿
    spec:
      completions: 1
      backoffLimit: 6
      template:
        spec:
          containers:
          - name: throw-dice
            image: kodekloud/throw-dice
          restartPolicy: Never

```

### 기본 명령어

```bash
# CronJob 생성
kubectl create cronjob my-cronjob \\
  --image=my-image \\
  --schedule="30 21 * * *" \\
  --dry-run=client -o yaml > cronjob.yaml

kubectl apply -f cronjob.yaml

# 상태 확인
kubectl get cronjob

# 수동 실행 (테스트용)
kubectl create job test-job --from=cronjob/my-cronjob

```

### 스케줄 문법

```
# ┌───────────── 분 (0-59)
# │ ┌───────────── 시 (0-23)
# │ │ ┌───────────── 일 (1-31)
# │ │ │ ┌───────────── 월 (1-12)
# │ │ │ │ ┌───────────── 요일 (0-7, 0=일요일)
# │ │ │ │ │
  * * * * *

예시:
"0 2 * * *"     # 매일 02:00
"30 21 * * *"   # 매일 21:30
"0 0 * * 0"     # 매주 일요일 00:00
"*/15 * * * *"  # 매 15분마다
"0 9-17 * * 1-5"  # 평일 9시~17시 매 정각

```

### 핵심 포인트

- **schedule**: cron 표현식으로 실행 주기 정의
- **jobTemplate**: 실행할 Job의 템플릿 (Job spec과 동일)
- **자동 실행**: 지정된 시간에 자동으로 Job을 생성하고 실행한다
- **히스토리 관리**: 성공/실패한 Job이 자동으로 정리된다 (기본: 3개/1개 보관)

---

## 3. 비교 및 선택 가이드

| 구분 | Pod | Job | CronJob |
| --- | --- | --- | --- |
| **용도** | 상시 서비스 | 일회성 작업 | 주기적 작업 |
| **실패 시** | 재시작 | 재시도 후 완료 | 다음 스케줄에 재실행 |
| **완료 보장** | ✗ | ✓ | ✓ |
| **자동 실행** | ✗ | ✗ | ✓ (스케줄) |
| **사용 예시** | 웹 서버, API | 데이터 마이그레이션 | 백업, 리포트 생성 |

### 언제 무엇을 사용할까?

**Pod 사용:**

- 웹 서버, API 서버 등 계속 실행되어야 하는 서비스
- 실패 시 자동 재시작만 필요한 경우

**Job 사용:**

- 데이터 변환, 파일 처리 등 한 번만 실행하면 되는 작업
- 실패 시 자동으로 재시도하여 반드시 완료해야 하는 작업
- 병렬 처리가 필요한 배치 작업

**CronJob 사용:**

- 매일 자정에 실행되는 백업 작업
- 주기적인 리포트 생성, 로그 정리
- 특정 시간에 반복 실행되어야 하는 모든 작업

### 실전 팁

```bash
# Job 완료 후 자동 삭제 (TTL)
spec:
  ttlSecondsAfterFinished: 100  # 완료 100초 후 삭제

# CronJob 일시 중지
kubectl patch cronjob my-cronjob -p '{"spec":{"suspend":true}}'

# 실패한 Job Pod 로그 확인
kubectl logs <pod-name>

# Job에서 생성된 모든 Pod 확인
kubectl get pods --selector=job-name=my-job

```

**핵심**: Job은 성공 보장, CronJob은 자동 스케줄링