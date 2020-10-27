### ORDER BY Multiple condition

```sql
SELECT ANIMAL_ID, NAME, DATETIME FROM ANIMAL_INS ORDER BY NAME, DATETIME DESC;
```

NAME에 따라 정렬하고 NAME이 같은 경우엔 DATETIME이 늦은 순서대로 선택한다.

### SELECT oldest datetime

```sql
SELECT NAME, DATETIME FROM ANIMAL_INS ORDER BY DATETIME LIMIT 1;
```

DATETIME 순으로 정렬하고 가장 위 한 컬럼만 가져온다.

또는 MAX를 이용하여

```sql
SELECT NAME, DATETIME FROM ANIMAL_INS WHERE DATETIME = (SELECT MAX(DATETIME) FROM ANIMAL_INS);
```

가져와도된다.

### COUNT문

```sql
SELECT COUNT(NAME) FROM ANIMAL_INS;
```

COUNT를 통해 컬럼을 선택하면 중복 값들은 지워지니 모든 값이 필요할 때는 꼭 ID로 가져오도록 한다.

### Inner Join

```sql
SELECT T1.A,T1.B, T2.C
FROM T1 , T2
WHERE  T1.A = T2.A
```

### Outer Join

```sql
SELECT T1.A,T1.B, T2.C
FROM T1 , T2
WHERE  T1.A  *= T2.A 
// LEFT

SELECT T1.A,T1.B, T2.C
FROM T1 , T2
WHERE  T1.A  =* T2.A
// RIGHT
```

두 조인을 UNION하면 Full Outer Join이 된다.

### Having

```sql
SELECT NAME, COUNT(NAME) FROM ANIMAL_INS GROUP BY NAME HAVING COUNT(NAME) > 1 ORDER BY NAME ASC;
```

### HOUR

```sql
HOUR(DATETIME)
```

DATETIME 타입에서 시간 단위만 따로 INT 타입으로 반환한다.

### IFNULL

```sql
SELECT ANIMAL_TYPE, IFNULL(NAME, "No name"), SEX_UPON_INTAKE FROM ANIMAL_INS;
```

값이 null이면 default 값을 어떻게 처리할 것인지에 대한 명령어

### LEFT OUTER JOIN

```sql
SELECT O.ANIMAL_ID, O.NAME
FROM ANIMAL_OUTS O
LEFT OUTER JOIN ANIMAL_INS I
ON O.ANIMAL_ID = I.ANIMAL_ID
WHERE I.ANIMAL_ID IS NULL;
```

4번쨰 라인까지하면 O에 해당되는 컬럼들만 출력된다. LEFT OUTER JOIN이므로 O의 값들은 NULL인 경우에도 전부 출력이 되는데 여기서 마지막 줄을 추가하면 공통된 교집합, 즉 INNER JOIN 값들은 뺄 수 있으므로 완벽한 O - I 가 된다.

### 괄호

```sql
-- 코드를 입력하세요
SELECT O.ANIMAL_ID, O.ANIMAL_TYPE, O.NAME
FROM ANIMAL_OUTS O, ANIMAL_INS I
WHERE O.ANIMAL_ID = I.ANIMAL_ID
AND I.SEX_UPON_INTAKE LIKE 'Intact%'
AND (O.SEX_UPON_OUTCOME LIKE 'Spayed%' OR O.SEX_UPON_OUTCOME LIKE 'Neutered%');
```

AND 와 OR 이 많을 때는 괄호를 적절히 사용해야 한다.

### IN

```sql
-- 코드를 입력하세요
SELECT ANIMAL_ID, NAME, SEX_UPON_INTAKE
FROM ANIMAL_INS
WHERE NAME IN ('Lucy', 'Ella', 'Pickle', 'Rogan', 'Sabrina', 'Mitty')
ORDER BY ANIMAL_ID;
```

아마 IN 내에 새로운 SELECT 문을 써도 될 것으로 생각 된다.

### IF

```sql
SELECT ANIMAL_ID, NAME, IF(SEX_UPON_INTAKE LIKE 'N%' OR SEX_UPON_INTAKE LIKE 'S%', 'O', 'X') AS '중성화'
FROM ANIMAL_INS
ORDER BY ANIMAL_ID;
```

IF 문을 컬럼 명으로 사용할 수 있다.

### convert datetime

```sql
**SELECT ANIMAL_ID, NAME, DATE_FORMAT(DATETIME, '%Y-%m-%d') AS '날짜'
FROM ANIMAL_INS
ORDER BY ANIMAL_ID;**
```

%y는 년도에서 뒤 두자리만 가져오고 대문자 Y를 사용하면 네자리 모두 가져온다

%m은 숫자로 달을 가져오고 %M은 영어로 가져온다

%d 는 숫자로 날짜를 가져오고 %D는 ~th의 형태로 가져온다.(first가 아니고 1st, 2nd 이런식으로 가져온다.)
