# Window Functions

일반 집계함수(SUM, COUNT)는 여러 row를 하나로 합치지만, Window Function은 각 row를 유지하며 다른 Row를 참조할 수 있다.

## LAG

이전 행 값을 가져온다.

```sql
LAG(컬럼명) OVER (ORDER BY 정렬기준)
```

### 원본 데이터

| month_start | total_mrr |
|-------------|-----------|
| 2024-07-01  | 300000    |
| 2024-08-01  | 350000    |
| 2024-09-01  | 380000    |

```sql
SELECT
    month_start,
    total_mrr,
    LAG(total_mrr) OVER (ORDER BY month_start) as prev_mrr
FROM monthly_stats
```

### 결과

| month_start | total_mrr | prev_mrr |
|-------------|-----------|----------|
| 2024-07-01  | 300000    | NULL     | ← 이전 행이 없어서 NULL
| 2024-08-01  | 350000    | 300000   | ← 이전 행(7월)의 total_mrr
| 2024-09-01  | 380000    | 350000   | ← 이전 행(8월)의 total_mrr

→ 이렇게 LAG를 활용하면 현재 row에서 이전 row의 값을 가져와서 증감률, 변화 등을 계산하기에 좋다.

### LAG with Offset

몇 번째 전 행을 가져올 지 선택한다.

```sql
LAG(total_mrr, 2) OVER (ORDER BY month_start)  -- 2행 전의 값
```

## LEAD

LAG의 반대로 다음 행의 값을 가져온다.

```sql
LEAD(total_mrr) OVER (ORDER BY month_start)  -- 다음 행의 값
```

## 활용 예제: 증감률 계산

```sql
SELECT
    month_start, month_end,
    ROUND(
        (total_mrr - LAG(total_mrr) OVER (ORDER BY month_start)) * 100
        / LAG(total_mrr) OVER (ORDER BY month_start), 2
    ) AS mrr_growth_rate
FROM monthly_stats ms
```

이전 total_mrr 값을 가지고 와서 증감률을 계산한다.

---

## ROW_NUMBER()

`ROW_NUMBER()`는 SQL의 윈도우 함수(Window Function)로, 특정 기준에 따라 정렬된 각 행에 고유한 순번을 부여합니다. 결과셋을 정렬하여 순위를 매기거나, 그룹별 순번을 매길 때 매우 유용하게 사용됩니다.

### 기본 구조

`ROW_NUMBER()` 함수는 항상 `OVER()` 절과 함께 사용되어야 합니다. `OVER()` 절은 순번을 어떻게 계산할지(어떤 그룹으로 나누고, 어떤 순서로 매길지) 정의합니다.

```sql
ROW_NUMBER() OVER (
    [PARTITION BY 컬럼1, 컬럼2...]
    ORDER BY 정렬용_컬럼 [ASC | DESC]
)
```

### 핵심 구성 요소

- **ORDER BY (필수)**

    순번을 매길 기준을 정의합니다. ORDER BY 절에 지정된 컬럼을 기준으로 행들이 정렬되고, 그 순서에 따라 1부터 순번이 부여됩니다.

- **PARTITION BY (선택)**

    전체 데이터를 특정 그룹으로 나눌 때 사용합니다. PARTITION BY 절을 사용하면 각 그룹별로 순번이 다시 1부터 시작됩니다. 이 절을 생략하면 전체 데이터셋을 하나의 그룹으로 간주합니다.

### 주요 사용 사례

#### 1. 전체 데이터에 순위 매기기

`PARTITION BY` 없이 `ORDER BY`만 사용하여 전체 결과에 대한 순위를 매깁니다.

**예시: 가격이 비싼 제품 순으로 순위 매기기**

```sql
SELECT
    product_name,
    price,
    ROW_NUMBER() OVER (ORDER BY price DESC) AS price_rank
FROM
    products;
```

결과:

| product_name | price | price_rank |
|:---|:---|:---|
| Laptop | 2,000 | 1 |
| Tablet | 1,200 | 2 |
| Phone | 1,000 | 3 |
| ... | ... | ... |

#### 2. 그룹별 순위 매기기 (Top-N 구하기)

`PARTITION BY`를 사용하여 각 그룹 내에서 순위를 매깁니다. 각 부서에서 급여를 가장 많이 받는 상위 2명의 직원을 찾는 경우에 유용합니다.

**예시: 각 부서(department_id)별로 급여가 높은 직원 순위 매기기**

```sql
SELECT
    employee_name,
    department_id,
    salary,
    ROW_NUMBER() OVER (PARTITION BY department_id ORDER BY salary DESC) AS rank_in_dept
FROM
    employees;
```

결과: (부서가 바뀔 때마다 rank_in_dept가 1로 초기화됩니다.)

| employee_name | department_id | salary | rank_in_dept |
|:---|:---|:---|:---|
| John | 101 | 9,000 | 1 |
| Jane | 101 | 8,500 | 2 |
| Smith | 102 | 9,500 | 1 |
| Alice | 102 | 8,000 | 2 |
| ... | ... | ... | ... |

#### 3. 중복 데이터 식별 및 제거

`PARTITION BY`로 중복 여부를 판단할 컬럼을 지정하고, 순번이 1보다 큰 데이터를 찾으면 중복된 데이터를 쉽게 식별할 수 있습니다.

**예시: 이메일(email)이 중복된 고객 찾기 (가장 최근 가입자만 남기고 싶을 때)**

```sql
WITH NumberedCustomers AS (
    SELECT
        email,
        created_at,
        ROW_NUMBER() OVER (PARTITION BY email ORDER BY created_at DESC) AS rn
    FROM
        customers
)
SELECT * FROM NumberedCustomers WHERE rn > 1; -- rn이 1보다 큰 데이터가 중복 데이터
```

#### 4. 페이지네이션 (Pagination) 구현

게시판이나 상품 목록 페이지를 구현할 때, 특정 범위의 행을 가져오는 데 사용할 수 있습니다.

**예시: 제품 목록을 이름순으로 정렬하여 11번째부터 20번째까지의 데이터 가져오기**

```sql
WITH NumberedProducts AS (
    SELECT
        product_name,
        ROW_NUMBER() OVER (ORDER BY product_name ASC) AS row_num
    FROM
        products
)
SELECT * FROM NumberedProducts WHERE row_num BETWEEN 11 AND 20;
```