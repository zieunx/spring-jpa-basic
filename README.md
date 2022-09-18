# Spring Data JPA 기초



강의 : https://www.inflearn.com/course/jpa-spring-data-%EA%B8%B0%EC%B4%88

유튜브에 무료 공개 되어있는 최범균님의 JPA 기초, 필요한 부분 위주로 학습해본다.



**ORM**

자바 객체와 관계형 DB 간의 매핑 처리를 위한 API



## 01 일단 해보기 - 02 코드 구조 & 영속 컨텍스트

- 간단한 설정으로 클래스와 테이블 간 매핑 처리를 해보았다.
- 객체 변경만으로 DB 테이블이 업데이트 되었다.
- 쿼리를 작성하지 않았다.



처음에 SpringBoot 프로젝트로 생성해서 persistence 어노테이션들을 자동 import 했더니 javax.persistence 로 import 되어 에러가 발생했다. 해당 예제는 jakarta.persistence 에서 import 해야한다.

Persistence 설정

>  main > resources > META-INF > persistence.xml 



영속 단위 기준으로  EntityManagerFactory 를 생성.

<persistence-unit> 으로 설정된 유닛별로 영속성 관리 한다.

다음과 같이 접근해주면 된다.

```java
Persistence.createEntityManagerFactory(persistence-unit의 name);
```

어플리케이션 최초 구동 시, EntityManagerFactory로 1번 영속한다.



-> 대부분 Spring 이 대신 처리해주는 과정. 한번 알고가면 좋을 듯하다.



## 03 엔티티 매핑
**엔티티 클래스 제약 조건**
- @Entity 어노테이션
- @Id 적용
- 인자 없는 기본 생성자 필수
- 기본 생성자는 public 이나 protected 여야 한다
- final, enum, interface, inner 클래스는 안된다.
- 저장필드 final 금지

**접근타입**
두개의 접근 타입
- 필드 접근: 필드 값을 사용해서 매핑
- 프로퍼티 접근: getter/setter 메서드를 사용해서 매핑

설정방법
- @Id 어노테이션을 필드에 붙히면 필드 접근
- @Id 어노테이션을 getter 메서드에 붙히면 프로퍼티 접근
- @Access 어노테이션을 사용해서 명시적으로 지정

## 05 식별자 생성 방식

### @GeneratedValue
- GenerationType.IDENTITY
	- 데이터베이스에 위임
	- ex. MySQL 자동 증가 컬럼
	- EntityManager#persist() 호출 시점에 **INSERT 쿼리 실행**
	- **persist() 실행할 때 객체에 식별자 값 할당**
	```java
    transaction.begin();

    Review review = new Review(5, "H-01", "작성자", "댓글");

    log.info("persist 실행 전");
    em.persist(review);
    log.info("persist 실행 후");

    log.info("생성한 식별자: {}", review.getId());

    log.info("커밋 전");
    transaction.commit();
    log.info("커밋 완료");
    ```
    위의 코드는 아래의 로그를 통해서 INSERT 문을 먼저 실행하여 식별자를 가져온다는 것을 알 수 있다. 이미 INSERT 문을 식별자 생성 방식때메 이미 실행했기 때문에 커밋 시 INSERT 문이 실행되지 않는다.
	```planText
    18:17:55.628 [main] INFO  c.j.b.s.identifier.NativeMain - persist 실행 전
18:17:55.652 [main] DEBUG org.hibernate.SQL - insert into Review (comment, created, hotel_id, mark, writer_name) values (?, ?, ?, ?, ?)
18:17:55.697 [main] INFO  c.j.b.s.identifier.NativeMain - persist 실행 후
18:17:55.697 [main] INFO  c.j.b.s.identifier.NativeMain - 생성한 식별자: 1
18:17:55.697 [main] INFO  c.j.b.s.identifier.NativeMain - 커밋 전
18:17:55.710 [main] INFO  c.j.b.s.identifier.NativeMain - 커밋 완료
    ```
- GenerationType.SEQUENCE
	- SEQUENCE 를 사용할 때 Long 타입 권장.
	- 테이블마다 다른 시퀀스를 사용하고 싶으면 @SequenctGenerator 사용
	- allocationSize 은 1 권장
	- **persist() 실행할 때 객체에 식별자 값 할당**
	```java
    transaction.begin();

    ActivityLog activityLog = new ActivityLog("U01", "VISIT");

    log.info("persist 실행 전");
    em.persist(activityLog);
    log.info("persist 실행 후");

    log.info("생성한 식별자: {}", activityLog.getId());

    log.info("커밋 전");
    transaction.commit();
    ```
    위의 코드는 아래의 로그를 통해서 시퀀스를 이용하여 식별자를 먼저 구하고 커밋을 통해 ISERT를 실행하는 것을 확인할 수 있다.
	```planText
    21:02:18.399 [main] INFO  c.j.b.s.main.identifier.SequenceMain - persist 실행 전
21:02:18.405 [main] DEBUG org.hibernate.SQL - select activity_seq.nextval from dual
21:02:18.446 [main] INFO  c.j.b.s.main.identifier.SequenceMain - persist 실행 후
21:02:18.447 [main] INFO  c.j.b.s.main.identifier.SequenceMain - 생성한 식별자: 1
21:02:18.447 [main] INFO  c.j.b.s.main.identifier.SequenceMain - 커밋 전
21:02:18.455 [main] DEBUG org.hibernate.SQL - insert into activity_log (activity_type, created, user_id, id) values (?, ?, ?, ?)
21:02:18.493 [main] INFO  c.j.b.s.main.identifier.SequenceMain - 커밋 완료
    ```
- GenerationType.TABLE
	- 키 생성용 테이블을 사용
	- @TableGenerator 필요
	- 모든 DB 사용 가능하지만 성능에 영향(DE Lock 등)
	- allocationSize 은 1 권장
	- **persist() 실행할 때 테이블 사용**
	```java
    transaction.begin();

    AccessLog accessLog = new AccessLog("/path01", LocalDateTime.now());

    log.info("persist 실행 전");
    em.persist(accessLog);
    log.info("persist 실행 후");

    log.info("생성한 식별자: {}", accessLog.getId());

    log.info("커밋 전");
    transaction.commit();
    log.info("커밋 완료");
    ```
    위의 코드는 아래의 로그를 통해서 persist 시 식별자용테이블에 접근하여 시퀀스를 설정한다.
	```
    21:16:02.251 [main] INFO  c.j.b.s.main.identifier.TableMain - persist 실행 전
21:16:02.287 [main] DEBUG org.hibernate.SQL - select tbl.nextval from id_seq tbl where tbl.entity=? for update
21:16:02.304 [main] DEBUG org.hibernate.SQL - update id_seq set nextval=?  where nextval=? and entity=?
21:16:02.322 [main] INFO  c.j.b.s.main.identifier.TableMain - persist 실행 후
21:16:02.322 [main] INFO  c.j.b.s.main.identifier.TableMain - 생성한 식별자: 4
21:16:02.323 [main] INFO  c.j.b.s.main.identifier.TableMain - 커밋 전
21:16:02.343 [main] DEBUG org.hibernate.SQL - insert into access_log (accessed, path, id) values (?, ?, ?)
21:16:02.359 [main] INFO  c.j.b.s.main.identifier.TableMain - 커밋 완료
    ```
- GenerationType.AUTO
	- 방언에 따라 자동 지정, 기본 값

