# 1 - 데이터 모델링

# 서론
첫 이직을 하며 이전 회사와 다른 사람들과 다른 도메인에서 다른 기술들, 다른 업무를 접할 일이 정말 많았다. 언어, 프레임워크, 데이터베이스 전부 다른 기술을 사용하고 MSA 비스무리한 구조에 훨씬 더 많은 기술스택을 접해보고 있다.

최근에 진행했던 업무들 중 하나는 고도화로 인해 변경되는 스키마에 맞춰 기존에 쌓여있던 데이터를 마이그레이션 하는 작업이었다. NIFI라는 ETL 솔루션을 사용했고 덕분에 데이터들이 어떻게 생긴지 확인할 수 있었다.

현재 서비스와 데이터들이 쌓인 구조를 보며 꽤 많은 공부가 되었는데, 조금 더 원론적인 부분이 알고 싶어 `데이터 파이프라인 핵심 가이드` 라는 책을 읽고 있다.

책을 읽으며 기억에 남은 부분들을 정리하여 블로그에 올려보려고 한다. 이번에 정리해본 부분은 데이터를 쌓기 전에 어떻게 쌓을지 모델링하는 내용으로, 다양한 모델링 방식의 특징과 장단점을 토대로 비교한다.

# 증분 수집된 데이터 모델링
증분 수집된 데이터 모델링(Incrementally Collected Data Modeling)은 전체 데이터를 매번 가져오는 대신 마지막으로 수집한 시점 이후에 변경된 데이터만 선택적으로 가져온다. 주로 타임스탬프나 순차적인 ID를 기준으로 새로운 데이터를 식별한다. 

이 방식은 전체 데이터를 전부 스캔하고 분석할 필요가 없기 때문에 데이터 전송량을 줄이고 처리 시간을 단축시킨다.

## 현재 거주지를 기반으로 데이터 정리

```sql
CREATE TABLE Orders (
  OrderId int,
  OrderStatus varchar(30),
  OrderDate timestamp,
  CustomerId int,
  OrderTotal numeric
);

INSERT INTO Orders
  VALUES(1,'Shipped','2020-06-09',100,50.05);
INSERT INTO Orders
  VALUES(2,'Shipped','2020-07-11',101,57.45);
INSERT INTO Orders
  VALUES(3,'Shipped','2020-07-12',102,135.99);
INSERT INTO Orders
  VALUES(4,'Shipped','2020-07-12',100,43.00);

CREATE TABLE Customers
(
  CustomerId int,
  CustomerName varchar(20),
  CustomerCountry varchar(10)
);

INSERT INTO Customers VALUES(100,'Jane','USA');
INSERT INTO Customers VALUES(101,'Bob','UK');
INSERT INTO Customers VALUES(102,'Miles','UK');
```
- 현재 주문 현황 테이블
- 현재 고객 정보 테이블

```sql
CREATE TABLE Customers_staging (  
  CustomerId int,  
  CustomerName varchar(20),  
  CustomerCountry varchar(10),  
  LastUpdated timestamp  
);  
  
INSERT INTO Customers_staging  
  VALUES(100,'Jane','USA','2019-05-01 7:01:10');  
INSERT INTO Customers_staging  
  VALUES(101,'Bob','UK','2020-01-15 13:05:31');  
INSERT INTO Customers_staging  
  VALUES(102,'Miles','UK','2020-01-29 9:12:00');  
INSERT INTO Customers_staging  
  VALUES(100,'Jane','UK','2020-06-20 8:15:34');
```
- 고객의 거주지 변경에 대한 테이블
- Jane은 19년도에 USA에 있었지만 20년도에 UK로 거주지를 이동함.

```sql
CREATE TABLE order_summary_daily_current
(
  order_date date,
  order_country varchar(10),
  total_revenue numeric,
  order_count int
);

INSERT INTO order_summary_daily_current
  (order_date, order_country,
  total_revenue, order_count)
WITH customers_current AS
(
  SELECT CustomerId,
    MAX(LastUpdated) AS latest_update
  FROM Customers_staging
  GROUP BY CustomerId
)
SELECT
  o.OrderDate AS order_date,
  cs.CustomerCountry AS order_country,
  SUM(o.OrderTotal) AS total_revenue,
  COUNT(o.OrderId) AS order_count
FROM Orders o
INNER JOIN customers_current cc
  ON cc.CustomerId = o.CustomerId
INNER JOIN Customers_staging cs
  ON cs.CustomerId = cc.CustomerId
    AND cs.LastUpdated = cc.latest_update
GROUP BY o.OrderDate, cs.CustomerCountry;
```
- 가장 최신의 LastUpdated를 기준으로 Customer를 가져오는 customers_current 테이블
- 가장 최신의 LastUpdated가 기준이므로, 최신 거주지를 기준으로 주문의 수량과 주문의 revenue를 쿼리한다.

## 주문 당시 거주지를 기반으로 데이터 정리

```sql
INSERT INTO Orders
  VALUES(1,'Shipped','2020-06-09',100,50.05);
INSERT INTO Orders
  VALUES(2,'Shipped','2020-07-11',101,57.45);
INSERT INTO Orders
  VALUES(3,'Shipped','2020-07-12',102,135.99);
INSERT INTO Orders
  VALUES(4,'Shipped','2020-07-12',100,43.00);
```

- Order 테이블의 생성시점과 `Jane`에 해당 되는 Customer의 Id(100)을 확인하면 OrderId 1번에 해당하는 주문은 Jane이 USA에 거주할 때 주문했다.

```sql
CREATE TABLE order_summary_daily_pit  
(  
  order_date date,  
  order_country varchar(10),  
  total_revenue numeric,  
  order_count int  
);  
  
INSERT INTO order_summary_daily_pit  
  (order_date, order_country, total_revenue, order_count)  
WITH customer_pit AS  
(  
  SELECT  
    cs.CustomerId,  
    o.OrderId,  
    MAX(cs.LastUpdated) AS max_update_date  
  FROM Orders o  
  INNER JOIN Customers_staging cs  
    ON cs.CustomerId = o.CustomerId  
      AND cs.LastUpdated <= o.OrderDate  
  GROUP BY cs.CustomerId, o.OrderId  
)  
SELECT  
  o.OrderDate AS order_date,  
  cs.CustomerCountry AS order_country,  
  SUM(o.OrderTotal) AS total_revenue,  
  COUNT(o.OrderId) AS order_count  
FROM Orders o  
INNER JOIN customer_pit cp  
  ON cp.CustomerId = o.CustomerId  
    AND cp.OrderId = o.OrderId  
INNER JOIN Customers_staging cs  
  ON cs.CustomerId = cp.CustomerId  
    AND cs.LastUpdated = cp.max_update_date  
GROUP BY o.OrderDate, cs.CustomerCountry;
```
- 주문을 했던 날짜인 OrderDate가 고객의 LastUpdated보다 늦은, 즉 주문 당시 고객의 Customer 정보로 customer_pit 임시 테이블을 만든다.
- cp의 max_updated_date로 Customer를 찾아 그 때의 거주지를 기반으로 주문의 수량과 주문의 revenue를 쿼리한다.

# 추가 전용 데이터 모델링
추가 전용 모델(Append-Only Data Modeling)은 기존의 데이터를 수정하거나 삭제하지 않고 새로운 데이터의 추가만 허용하는 방식이다. 데이터의 변경 이력을 모두 보존하며 불변성을 보장한다. 주로 금융 거래 기록, 로그 데이터, 이벤트 스트림 처리에 사용된다.

```sql
CREATE TABLE PageViews (
  CustomerId int,
  ViewTime timestamp,
  UrlPath varchar(250),
  utm_medium varchar(50)
);

INSERT INTO PageViews
  VALUES(100,'2020-06-01 12:00:00',
    '/home','social');
INSERT INTO PageViews
  VALUES(100,'2020-06-01 12:00:13',
    '/product/2554',NULL);
INSERT INTO PageViews
  VALUES(101,'2020-06-01 12:01:30',
    '/product/6754','search');
INSERT INTO PageViews
  VALUES(102,'2020-06-02 7:05:00',
    '/home','NULL');
INSERT INTO PageViews
  VALUES(101,'2020-06-02 12:00:00',
    '/product/2554','social');
```
- 고객이 어떤 웹 페이지에 언제 접근했는지 기록하는 테이블

## 각 페이지뷰 당 조회수가 몇인지 알아보기
```sql
CREATE TABLE pageviews_daily (  
  view_date date,  
  url_path varchar(250),  
  customer_country varchar(50),  
  view_count int  
);  
  
INSERT INTO pageviews_daily  
  (view_date, url_path, customer_country, view_count)  
SELECT  
  CAST(p.ViewTime as Date) AS view_date,  
  p.UrlPath AS url_path,  
  c.CustomerCountry AS customer_country,  
  COUNT(*) AS view_count  
FROM PageViews p  
LEFT JOIN Customers c ON c.CustomerId = p.CustomerId  
GROUP BY  
  CAST(p.ViewTime as Date),  
  p.UrlPath,  
  c.CustomerCountry;
```
- ViewTime을 date로 캐스팅 후 날짜, URL Path, Country 별로 해당 사이트에 몇 번의 뷰수가 있었는지 확인

```sql
INSERT INTO PageViews
  VALUES(102,'2020-06-02 12:03:42',
    '/home','NULL');
INSERT INTO PageViews
  VALUES(101,'2020-06-03 12:25:01',
    '/product/567','social');
```
- PageViews에 새로운 데이터가 들어온다.

## 추가 전용 데이터 모델링의 데이터 중복 이슈

```sql
CREATE TABLE pageviews_daily_2 AS
SELECT * FROM pageviews_daily;

INSERT INTO pageviews_daily_2
  (view_date, url_path,
  customer_country, view_count)
SELECT
  CAST(p.ViewTime as Date) AS view_date,
  p.UrlPath AS url_path,
  c.CustomerCountry AS customer_country,
  COUNT(*) AS view_count
FROM PageViews p
LEFT JOIN Customers c
  ON c.CustomerId = p.CustomerId
WHERE
  p.ViewTime >
  (SELECT MAX(view_date) FROM pageviews_daily_2)
GROUP BY
  CAST(p.ViewTime as Date),
  p.UrlPath,
  c.CustomerCountry;
```
- 위 데이터만 증분하여 `pageviews_daily_2`에 최신화한다.
- 하지만 기존 pageviews_daily에 있던 6월 2일 데이터가 또 다시 daily_2를 집계할 때 포함되어 조회수가 오버되어 나온다.

![](./Pasted%20image%2020250723152803.png)


```sql
CREATE TABLE tmp_pageviews_daily AS
SELECT *
FROM pageviews_daily
WHERE view_date
  < (SELECT MAX(view_date) FROM pageviews_daily);

INSERT INTO tmp_pageviews_daily
  (view_date, url_path,
  customer_country, view_count)
SELECT
  CAST(p.ViewTime as Date) AS view_date,
  p.UrlPath AS url_path,
  c.CustomerCountry AS customer_country,
  COUNT(*) AS view_count
FROM PageViews p
LEFT JOIN Customers c
  ON c.CustomerId = p.CustomerId
WHERE p.ViewTime
  > (SELECT MAX(view_date) FROM pageviews_daily)
GROUP BY
  CAST(p.ViewTime as Date),
  p.UrlPath,
  c.CustomerCountry;

TRUNCATE TABLE pageviews_daily;

INSERT INTO pageviews_daily
SELECT * FROM tmp_pageviews_daily;

DROP TABLE tmp_pageviews_daily;
```
- 가장 최근의 날짜보다 하루전 데이터로 temp 테이블을 만든다.
- temp 테이블에 증분 집계 쿼리를 실행하여 데이터를 넣는다.
-  결과 테이블(`pageviews_daily`)을 temp 테이블로 갈아 끼운다.

이렇게 하면 중복으로 집계된 날짜 없이 데이터를 집계할 수 있다.

# 변경 캡처 데이터 모델링
변경 데이터 캡처(Change Data Capture, CDC)는 데이터베이스에서 발생하는 모든 변경(Insert, Update, Delete)를 실시간으로 감지하고, 변경 내역 자체를 데이터로 만든다. 원본 데이터베이스에 가해지는 부하를 최소화하면서 데이터 변경을 정확하게 포착할 수 있다.

```sql
CREATE TABLE Orders_cdc
(
  EventType varchar(20),
  OrderId int,
  OrderStatus varchar(20),
  LastUpdated timestamp
);

INSERT INTO Orders_cdc
  VALUES('insert',1,'Backordered',
    '2020-06-01 12:00:00');
INSERT INTO Orders_cdc
  VALUES('update',1,'Shipped',
    '2020-06-09 12:00:25');
INSERT INTO Orders_cdc
  VALUES('delete',1,'Shipped',
    '2020-06-10 9:05:12');
INSERT INTO Orders_cdc
  VALUES('insert',2,'Backordered',
    '2020-07-01 11:00:00');
INSERT INTO Orders_cdc
  VALUES('update',2,'Shipped',
    '2020-07-09 12:15:12');
INSERT INTO Orders_cdc
  VALUES('insert',3,'Backordered',
    '2020-07-11 13:10:12');
```
- 주문의 변경사항을 위와 같이 추적할 수 있다.

```sql
CREATE TABLE orders_current (
  order_status varchar(20),
  order_count int
);

INSERT INTO orders_current
  (order_status, order_count)
  WITH o_latest AS
  (
    SELECT
       OrderId,
       MAX(LastUpdated) AS max_updated
    FROM Orders_cdc
    GROUP BY orderid
  )
  SELECT o.OrderStatus,
    Count(*) as order_count
  FROM Orders_cdc o
  INNER JOIN o_latest
    ON o_latest.OrderId = o_latest.OrderId
      AND o_latest.max_updated = o.LastUpdated
  GROUP BY o.OrderStatus;
```
- 최신 변경사항만 수집하여 `OrderStatus`로 GroupBy하면 최신으로 유지된 주문 상태를 집계할 수 있다.

## 주문 - 배송까지 걸린 평균 시간 찾기
```sql
CREATE TABLE orders_time_to_ship (
  OrderId int,
  backordered_days interval
);

INSERT INTO orders_time_to_ship
  (OrderId, backordered_days)
WITH o_backordered AS
(
  SELECT
     OrderId,
     MIN(LastUpdated) AS first_backordered
  FROM Orders_cdc
  WHERE OrderStatus = 'Backordered'
  GROUP BY OrderId
),
o_shipped AS
(
  SELECT
     OrderId,
     MIN(LastUpdated) AS first_shipped
  FROM Orders_cdc
  WHERE OrderStatus = 'Shipped'
  GROUP BY OrderId
)
SELECT b.OrderId,
  first_shipped - first_backordered
    AS backordered_days
FROM o_backordered b
INNER JOIN o_shipped s on s.OrderId = b.OrderId;
```
- 위 쿼리는 backordered(주문)에서 shipped(배송)까지 얼마나 걸렸는지를 알려준다.
- CDC를 통해 이러한 데이터를 뽑아낼 수 있다.
- 첫 번째 backordered의 상태 테이블(o_backordered)를 CTE(Common Table Expression)로 만들고, 첫 번째 Shipped 상태 테이블(o_shipped)도 만든 후 둘을 조인해서 두 시간 차이를 비교한다.

![](Pasted%20image%2020250724084252.png)
`orders_time_to_ship` 테이블 내용


```sql
SELECT AVG(backordered_days)
FROM orders_time_to_ship;
```
- 이제 위와 같은 쿼리를 통해 평균 배송 시간까지 알 수 있다.

# 데이터 모델링 방식 최종 비교

## 증분 수집
- 마지막 수집 지점(타임스탬프 혹은 Id) 이후의 최신 데이터만 가져온다.
- 삭제나 수정이 없고 데이터가 계속 쌓이기만 할 때 좋다.(IoT의 센서 데이터, 서버 로그 등..)
### 장점
- 구현이 간단하고 직관적이다.
- 풀스캔보다 리소스면에서 효율적이다.
### 단점
- 삭제나 과거 데이터 수정을 감지할 수 없다.
- 중복 집계를 피하기 위한 로직이 복잡해 질 수 있다.(tmp_pageviews_daily)

## 추가 전용
- 데이터 변경시 기존 데이터는 그대로 두고 새로운 상태를 추가한다.
- 현재 상태 뿐만 아니라 과거 이력도 중요할 때 사용한다.(고객 정보, 상품 정보, 주소 등 시간에 따라 변하는 차원 테이블 관리)

### 장점
- 모든 변경 이력이 보존된다.
- 과거 시점 조회(point-in-time) 분석에 용이하다.
	- 과거에 어떤 상태였다 라는 레코드가 존재한다. CDC의 경우엔 과거의 어떤 상태가 어떻게 변했다이므로 상태를 바로 알아채기가 어려움
- 데이터 불변성이 보장된다.
### 단점
- 데이터가 계속 쌓여 저장공간 비용이 증가한다.
- 최신 상태를 조회하는 쿼리가 복잡해진다.
- 테이블이 커지면 쿼리 성능이 저하된다.

## 변경 데이터 캡쳐
- 데이터베이스의 모든 변경(INSERT, UPDATE, DELETE)을 이벤트 스트림으로 감지한다.
- 데이터베이스의 상태를 실시간으로 완벽하게 복제하고 싶을 때 사용한다.(OLTP의 변경사항을 OLAP에 실시간으로 동기화할 때)

### 장점
- 삭제와 수정을 완벽하게 포착한다. 
- 원본 DB에주는 부하가 거의 없다.
	- db 내부의 트랜잭션 로그(mysql의 이진로그)
- 실시간 데이터 동기화가 가능하다.(AWS DMS와 같은 곳에서 사용한다.)

### 단점.
- Kafka와 같은 별도의 도구나 인프라 구축이 필요하다.(이벤트 스트림)
- 초기 설정이 복잡하고 유지보수 포인트가 늘어난다.
- 데이터베이스의 로그 설정에 의존적이다.

