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



