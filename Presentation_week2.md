### 학습내용

<br/>
 
#### Bean 주입
:jack_o_lantern: WIP

:santa: Bean 주입이란 Spring 컨테이너가 관리하는 객체(Bean)를 다른 Bean 또는 클라이언트에 제공하는 과정을 말합니다. 이를 통해 객체 간의 의존성을 외부에서 관리할 수 있으며, 코드의 유연성과 재사용성을 높일 수 있습니다.

:ghost: Bean은 Spring Framework에서 IoC(Inversion of Control)에 의해 관리되는 객체를 지칭합니다. 다시 말해, IoC를 통해 Java 객체가 생성될 때, 이 객체를 Bean이라고 부릅니다. 
이러한 Bean 객체는 @Bean, @Component, @Service, @Repository 등과 같은 어노테이션을 사용하여 생성할 수 있으며, 또한 application.xml과 같은 XML 파일을 사용하여 Bean을 직접 설정할 수도 있습니다. 
이 이외에 JavaConfig를 활용해 Bean을 설정할 수 있습니다.
 
<br/>
 
#### 사용 방법

- XML 설정: `<bean>` 태그를 사용하여 Bean을 정의하고, `<property>` 태그로 의존성을 주입합니다.
- Annotation: `@Autowired`, `@Inject` 등의 어노테이션을 사용하여 자동으로 Bean을 주입합니다.
- JavaConfig: Config 파일에 @Configuration과 @Bean Annotation을 사용해class를 관리합니다.

1. XML

<img
  src="https://github.com/Kyungmin97/YCTech_Backend/assets/87704434/1d7ddf05-d1ed-4162-96a2-a61f31e96a21"
  width="600"
  height="280"
/>
<img
  src="https://github.com/Kyungmin97/YCTech_Backend/assets/87704434/4bb5e64f-d157-45b1-afdf-1aa1cd9eada1"
  width="500"
  height="280"
/>

2. Annotation

```java
package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyBean {
    
    @Value("Hello, World!")
    private String property1;

    public String getProperty1() {
        return property1;
    }
}
```

3. JavaConfig

```java
package com.example.demo.config;

import com.example.demo.service.PostService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public PostService postService(){
        return new PostService();
    }
}
```

 <br/>
 

#### Bean Component 차이

---
:ghost:
|Bean|Componment|
|---|---|
|메소드에 사용|클래스에 사용|
|외부 라이브러리 사용|내부 라이브러리 사용|
|개발자가 수정할 수 없음|개발자 수정 가능|

---
:santa:
- Bean: Spring 컨테이너에 의해 생성, 관리되는 객체입니다. XML 파일이나 Java 클래스에 `@Bean` 어노테이션을 사용하여 정의할 수 있습니다.
- Component: `@Component` 어노테이션을 사용하여 Bean으로 등록되는 클래스입니다. `@Service`, `@Repository`, `@Controller` 등도 결국은 `@Component`를 확장한 것입니다.

주의점
- `@Bean`은 개발자가 완전히 제어를 할 수 있는 외부 라이브러리 등에 사용됩니다.
- `@Component`는 개발자가 클래스를 직접 작성하고, Spring에 의해 관리되기를 원할 때 사용됩니다.
---

   <br/>
 
#### Field Injection, Constructor Injection 차이

##### 필드 주입 방식(Field Injection)

필드 주입 방식(Field Injection)은 의존성을 주입할 때 @Autowired 어노테이션을 필드에 직접 사용하는 방식입니다. 
이 방식은 간단하게 사용할 수 있지만 주입된 객체를 불변 상태로 만들 수 없는 한계가 있다. 또한 생성자나 setter 메서드를 통한 주입 방식이 아니기 때문에 Spring DI 컨테이너 밖에서는 이 필드에 주입을 할 수 없습니다.

이 방식을 사용하기 위해 클래스의 멤버 변수에 직접 `@Autowired` 어노테이션을 사용하여 의존성을 주입합니다.


```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    @Autowired
    private MyRepository myRepository;

    public String getData() {
        return myRepository.getData();
    }
}

@Service
public class MyRepository {

    public String getData() {
        return "Data from MyRepository";
    }
}
```

```java
  @Autowired
  private MyService myService;
  
```

##### 생성자 주입 방식(Constructor Injection)

생성자 주입 방식(Constructor Injection)은 생성자를 통해 의존성을 주입하는 방식입니다. Spring 프레임워크에서는 생성자가 하나만 있는 경우에 @Autowired 어노테이션을 생략해도 주입이 가능합니다. 

이 방식을 사용하면 주입 받을 필드를 final로 선언하여 불변성을 확보할 수 있고, 컴파일 시점에서 누락된 의존성을 확인할 수 있으며, 객체 생성 시점에 필수적인 초기화를 수행하여 NullPointerException을 방지할 수 있는 장점이 있습니다.

정리하여, 생성자를 통해 의존성을 주입합니다. 이 방법이 권장됩니다.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    private final MyRepository myRepository;

    @Autowired
    public MyService(MyRepository myRepository) {
        this.myRepository = myRepository;
    }

    public String getData() {
        return myRepository.getData();
    }
}

@Service
public class MyRepository {

    public String getData() {
        return "Data from MyRepository";
    }
}

```

```java
  private final MyService myService;

  @Autowired
  public MyClass(MyService myService) {
    this.myService = myService;
  }
```

##### 장단점
- Field Injection은 코드가 간결하지만, 테스트하기 어렵고 변경에 유연하지 않습니다.
- Constructor Injection은 불변성을 보장하고, 테스트하기 쉽습니다.

###### *수정자 주입 방식 (Setter Injection)

>수정자 주입 방식 (Setter Injection)은 주입 받을 객체를 설정하는 setter 메서드를 통해 의존성을 주입하는 방식이다. 주로 주입 받는 객체가 변경될 가능성이 있을 때 사용한다. 그러나 실제 개발에서는 의존 관계가 자주 변경되지 않으며, 이 방식을 사용하면 객체를 불필요하게 수정할 수 있고, 이는 객체 지향 프로그래밍의 개방-폐쇄 원칙을 위반할 수 있다. 그래서 객체의 불변성을 보장하고 변경 가능성을 배제하기 위해서는 생성자 주입 방식을 사용하는 것이 좋다.

 
   
  <br/>
  
#### @Primary, @Qualifier annotation

@Primary 어노테이션과 @Qualifier 어노테이션은 Spring Framework에서 빈 주입을 제어하고 여러 빈 중에서 어떤 빈을 선택할지 조절하는 데 사용된다.

##### Primary

 - @Primary 어노테이션은 여러 빈 중에서 하나를 기본값(default)으로 지정하는 데 사용된다.
 - 같은 타입의 여러 빈이 있을 때, @Primary 어노테이션이 붙은 빈은 기본적으로 주입 대상이 된다.
 - @Primary 어노테이션이 없거나 다른 빈에도 붙어 있을 경우, @Autowired나 @Qualifier 등을 통해 명시적으로 주입할 빈을 선택할 수 있다.


##### Qualifier

 - @Qualifier 어노테이션은 여러 빈 중에서 어떤 빈을 주입할지 세부적으로 지정할 때 사용된다.
 - 스프링 컨테이너가 여러 빈을 같은 타입으로 찾은 경우, 추가적인 정보를 제공하여 어떤 빈을 선택해야 하는지 명시적으로 지정할 수 있다.
 - @Qualifier 어노테이션을 사용할 때는 어떤 빈을 사용할지를 해당 빈의 이름 또는 식별자로 지정합니다. 이를 통해 특정 빈을 주입할 수 있다.


@Primary 어노테이션이 있는 빈은 기본적으로 주입되며, @Qualifier 어노테이션을 사용하여 특정 빈을 선택하는 것은 Optional이다. 

 
 

#### @Configuration을 이용한 설정

```java
@Configuration
public class DatabaseConfig {

  @Bean
  @Profile("dev")
  public DataSource h2DevDataSource() {
    // 메모리에 저장
  }

  @Bean
  @Profile("live")
  public DataSource h2LiveDataSource() {
    // 파일에 저장
  }
}
```

  <br/>

#### Team blog

 - [:santa: Tistory](https://juliabiolat.tistory.com/84)
 - [:ghost: GITHUB](https://github.com/Kyungmin97/YCTech_Backend/blob/main/%EA%B0%95%EC%9D%98%20%EB%85%B8%ED%8A%B8/2%EC%A3%BC%EC%B0%A8-%EC%84%A0%ED%96%89.md)
  
#### 학습 참조 자료  
- https://www.baeldung.com/spring-tutorial
- https://wildeveloperetrain.tistory.com/139
