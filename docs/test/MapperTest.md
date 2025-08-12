# MapperTest

> For full demo code, check [ProductMapperTest](/src/test/java/com/sheepion/demo/mapper/ProductMapperTest.java).

Use JUnit 5 and Testcontainers to test your MyBatis mapper.

## Why Testcontainers?

Usually, when say 'Unit test' ahout mapper, you may think about mock a database or use H2 instead.
But sometimes, a mocked database can not really inflect your real situation, 
like H2 may not implement some features in MySQL.

In this case, you want a REAL database to test the expected behavior.
So we bring up [Testcontainers](https://www.testcontainers.org/) for better mapper test.

When use a Tetscontainer in the test class, it will pull and run a specified docker image, 
which is your database instance for test.

> Note: Docker installation is necessary for using Testcontainers

## Configure

### Dependencies

To use Testcontainers with Springboot and PostgreSQL, add these dependencies:
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-testcontainers</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>postgresql</artifactId>
            <scope>test</scope>
        </dependency>
        <!-- This is for MyBatis -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter-test</artifactId>
            <version>${mybatis-spring-boot-starter.version}</version>
            <scope>test</scope>
        </dependency>
```

### Test configuration

And create a Test configuration.
```java
// This configuration won't take effect unless you import it in your test class.
@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {
    
    @Bean
    // Since 3.1.0, add this annotation will replace your datasource automatically.
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"))
                // specify the db name and search schema, 
                // since we use Flyway to migrate automatically.
                .withDatabaseName("demo")
                .withUsername("root")
                .withPassword("root")
                .withUrlParam("currentSchema", "demo");
    }
}
```
In this way, Testcontainers' PostgreSQL instance will replace the datasource when running tests.

In demo code, we also add `@Import` here for two interpceotors, 
so when test class imported `TestcontainersConfiguration`, two interceptors will take effect either,

## Test code

We need these annotations on the test class:

- `@MybatisTest`: Instead of `@SpringBootTest`, this only load mybatis related beans, brings faster test execution.
- `@AutoConfigureTestDatabase`: `@MybatisTest` will replace our database with embedded one by default, but we 
want to use the Testcontainers one. So override the default behavior.
- `@Import`: Import our Testcontainers configuration to take effect.

And then, the Testcontainers datasource will take effect, and FLyway migration will be done in it.

`@MybatisTest` will enabled transaction management automatically, so you can focus on your test code.


