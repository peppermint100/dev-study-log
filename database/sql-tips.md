# SQL 실용 팁

## Monthly 통계 기법

월별 통계를 계산할 때 유용한 기법입니다. CTE(Common Table Expression)를 사용해 월 테이블을 만들고, 이를 기준으로 월별 집계를 수행합니다.

```sql
WITH month AS (
    SELECT '2024-07-01'::date AS month_start, '2024-07-31'::date AS month_end
    UNION ALL
    SELECT '2024-08-01'::date, '2024-08-31'::date
    UNION ALL
    SELECT '2024-09-01'::date, '2024-09-30'::date
)
SELECT
    m.month_start,
    m.month_end,
    COUNT(CASE WHEN s.start_date >= m.month_start AND s.start_date <= m.month_end THEN 1 END) AS new_subscriptions,
    COUNT(CASE WHEN s.end_date >= m.month_start AND s.end_date <= m.month_end THEN 1 END) AS end_subscriptions,
    COUNT(s.subscription_id) AS active_count,
    SUM(s.monthly_fee) AS total_mrr
FROM month m
INNER JOIN subscriptions s
    ON s.start_date <= m.month_end
    AND (s.end_date >= m.month_start OR s.end_date IS NULL)
GROUP BY m.month_start, m.month_end
```

### 핵심 포인트

1. **월 테이블 생성**: `WITH` 절로 분석하고자 하는 월의 시작일과 종료일을 정의
2. **조인 조건**: 구독이 해당 월에 활성화되어 있었는지 확인
   - `start_date <= month_end`: 월 종료일 이전에 시작
   - `end_date >= month_start OR end_date IS NULL`: 월 시작일 이후까지 유효하거나 아직 종료되지 않음
3. **CASE 조건부 집계**: 특정 조건을 만족하는 경우만 카운트

---

## COUNT가 0인 경우 결과에서 가장 뒤로 정렬하기

집계 결과에서 특정 값이 0인 경우를 결과의 맨 뒤로 보내고 싶을 때 유용한 기법입니다.

```sql
ORDER BY
    CASE WHEN COUNT(a.appointment_id) = 0 THEN 1 ELSE 0 END,  -- 첫 번째 정렬 기준
    COUNT(a.appointment_id) DESC                               -- 두 번째 정렬 기준
```

### 동작 원리

- `appointment_id`가 NULL인 경우(count가 0)를 ORDER BY에서 뒤로 보내고 싶을 때 사용
- `CASE` 문을 사용해서:
  - `COUNT = 0`이면 `1` 반환
  - `COUNT > 0`이면 `0` 반환
- 첫 번째 정렬 기준에서 0이 먼저 오고(오름차순), 두 번째 정렬 기준에서 count가 큰 순서대로 정렬

### 결과

| user_id | appointment_count | case_value |
|---------|-------------------|------------|
| 101     | 15                | 0          |
| 102     | 10                | 0          |
| 103     | 5                 | 0          |
| 104     | 0                 | 1          | ← count가 0인 경우 뒤로 이동
| 105     | 0                 | 1          | ← count가 0인 경우 뒤로 이동

이렇게 하면 appointment가 없는 사용자들이 자연스럽게 결과의 맨 뒤로 정렬됩니다.