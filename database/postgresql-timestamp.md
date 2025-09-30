# PostgreSQL Timestamp 계산

## 날짜 빼기

```sql
rr.process_date - rr.request_date

-- 결과
'2024-09-05 14:00:00' - '2024-09-03 10:00:00'
= INTERVAL '2 days 04:00:00'
```

PostgreSQL에서 timestamp 타입끼리 뺄셈을 하면 INTERVAL 타입으로 결과가 반환됩니다.

## EXTRACT 함수

```sql
EXTRACT(EPOCH FROM (rr.process_date - rr.request_date))
```

- `EXTRACT`를 통해서 날짜/시간에서 특정 부분을 추출한다.
- `EPOCH`: 유닉스 타임스탬프(1970-01-01 00:00:00 UTC부터의 초)를 추출한다.
- 결과적으로 초 단위의 숫자가 나온다.

### 다양한 EXTRACT 옵션

```sql
-- 연도 추출
EXTRACT(YEAR FROM timestamp_column)

-- 월 추출
EXTRACT(MONTH FROM timestamp_column)

-- 일 추출
EXTRACT(DAY FROM timestamp_column)

-- 시간 추출
EXTRACT(HOUR FROM timestamp_column)

-- 요일 추출 (0=일요일, 6=토요일)
EXTRACT(DOW FROM timestamp_column)
```

## 활용 예시

### 처리 시간 계산 (초 단위)

```sql
SELECT
    request_id,
    EXTRACT(EPOCH FROM (process_date - request_date)) AS processing_time_seconds
FROM requests;
```

### 처리 시간 계산 (시간 단위)

```sql
SELECT
    request_id,
    EXTRACT(EPOCH FROM (process_date - request_date)) / 3600 AS processing_time_hours
FROM requests;
```

### 처리 시간 계산 (일 단위)

```sql
SELECT
    request_id,
    EXTRACT(EPOCH FROM (process_date - request_date)) / 86400 AS processing_time_days
FROM requests;
```