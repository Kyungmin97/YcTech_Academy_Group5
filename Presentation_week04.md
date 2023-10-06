# spring security

## Spring Security란?
 - Spring Security는 Spring 기반의 애플리케이션의 보안(인증과 권한, 인가 등)을 담당하는 스프링 하위 프레임워크이다.
 - Spring Security는 '인증'과 '권한'에 대한 부분을 Filter 흐름에 따라 처리하고 있다.  
 - Filter는 Dispatcher Servlet으로 가기 전에 적용되므로 가장 먼저 URL 요청을 받지만, Interceptor는 Dispatcher와 Controller사이에 위치한다는 점에서 적용 시기의 차이가 있다.   
 - Spring Security는 보안과 관련해서 체계적으로 많은 옵션을 제공해주기 때문에 개발자 입장에서는 일일이 보안관련 로직을 작성하지 않아도 된다는 장점이 있다.  

## 용어 정리

- 접근 주체(Principal) : 보호된 대상에 접근하는 유저
- 인증(Authenticate) : 현재 유저가 누구인지 확인(ex. 로그인)하며 애플리케이션의 작업을 수행할 수 있는 주체임을 증명
- 인가(Authorize) : 현재 유저가 어떤 서비스, 페이지에 접근할 수 있는 권한이 있는지 검사
- 권한 : 인증된 주체가 애플리케이션의 동작을 수행할 수 있도록 허락되있는지를 결정
  - 권한 승인이 필요한 부분으로 접근하려면 인증 과정을 통해 주체가 증명 되어야만 한다.
  - 권한 부여에도 두가지 영역이 존재
    -  웹 요청 권한
    -  메소드 호출 및 도메인 인스턴스에 대한 접근 권한
 - Dispatcher Servlet : 웹 애플리케이션에서 들어오는 클라이언트 요청을 받아서 처리할 적절한 Controller로 연결하는 역할을 하는 것으로, Spring Framework에서 웹 요청을 관리하는 핵심 component
 - Interceptor : 클라이언트의 요청이 controller에 도달하기 전이나 후에 추가 작업을 수행하도록 해주는 기능 (ex. 로깅, 인증, 권한 검사 등의 작업을 처리)
 - Dispatcher : Front Controller로서 클라이언트 요청을 받아서 처리할 적절한 컴포넌트로 분배. Spring Security에서는 Dispatcher Servlet이 클라이언트 요청을 받아 Spring Security Filter 체인에 전달하여 보안 검사
 - Filter : 웹 애플리케이션에서 클라이언트 요청과 서버 응답을 가로채어 수정하거나 검사. Spring Security에서는 여러 개의 필터를 사용하여 보안 관련 작업을 수행. 각 필터는 특정한 작업을 수행하고 요청 및 응답을 수정하거나 확인.
   - AuthenticationFilter: 이 필터는 사용자의 인증(로그인)을 처리합니다. 클라이언트가 로그인 정보를 제출하면 이 필터가 해당 정보를 검사하고 사용자를 인증합니다.
   - AuthorizationFilter: 인증된 사용자에게 권한을 부여하는 데 사용됩니다. 이 필터는 요청에 대한 접근 권한을 확인하고, 사용자가 해당 리소스에 액세스할 수 있는지 확인합니다.
   - ExceptionTranslationFilter: 예외 처리를 담당하는 필터로, 인증되지 않은 요청 또는 권한 부족으로 인한 예외를 처리하고 적절한 에러 페이지 또는 응답을 생성합니다.

## Spring Security 처리 과정

<img
  src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FUOabX%2FbtqEJBBNixH%2FPGDv64FTKaBSLzMiiXkA3K%2Fimg.png"
  width="800"
  height="550"
/>

1. 사용자가 아이디 비밀번호로 로그인을 요청함
2. AuthenticationFilter에서 UsernamePasswordAuthenticationToken을 생성하여 AuthenticaionManager에게 전달
3. AuthenticaionManager는 등록된 AuthenticaionProvider(들)을 조회하여 인증을 요구함
4. AuthenticaionProvider는 UserDetailsService를 통해 입력받은 아이디에 대한 사용자 정보를 DB에서 조회함
5. 입력받은 비밀번호를 암호화하여 DB의 비밀번호화 매칭되는 경우 인증이 성공된 UsernameAuthenticationToken을 생성하여 AuthenticaionManager로 반환함
6. AuthenticaionManager는 UsernameAuthenticaionToken을 AuthenticaionFilter로 전달함
7. AuthenticationFilter는 전달받은 UsernameAuthenticationToken을 LoginSuccessHandler로 전송하고, SecurityContextHolder에 저장함

## 구현 및 수행

pom.xml  
```xml
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- Spring Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
</dependencies>

```  
  
SecurityConfig
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/home").permitAll() // 루트 및 홈 페이지는 모든 사용자에게 허용
                .anyRequest().authenticated() // 그 외의 모든 요청은 인증이 필요
                .and()
            .formLogin()
                .loginPage("/login") // 로그인 페이지 지정
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("USER"); // 사용자 인증 정보 지정
    }
}

```
  
HomeController 
```javaimport org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

```
  
home.html
```html
<!DOCTYPE html>
<html>
<head>
    <title>Home Page</title>
</head>
<body>
    <h1>Welcome to the Home Page!</h1>
    <p>This page is accessible to all users.</p>
    <a href="/login">Login</a>
</body>
</html>

```
  
login.html
```html
<!DOCTYPE html>
<html>
<head>
    <title>Login Page</title>
</head>
<body>
    <h1>Login</h1>
    <form method="post">
        <div>
            <label for="username">Username:</label>
            <input type="text" id="username" name="username">
        </div>
        <div>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password">
        </div>
        <div>
            <button type="submit">Login</button>
        </div>
    </form>
</body>
</html>
```



## Reference

https://mangkyu.tistory.com/76  
https://mangkyu.tistory.com/77  
https://galid1.tistory.com/576  
