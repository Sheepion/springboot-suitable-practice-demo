# Mock test

This document tells you how to use Mockito to achieve your unit testing.

For code example, check [ProductServiceImplTest](/src/test/java/com/sheepion/demo/service/impl/ProductServiceImplTest.java)

Recommended reading:

- [Baeldung - Mockito series](https://www.baeldung.com/mockito-series)

## MockBean

Since Springboot 3.4, `@MockBean` annotation is marked as deprecated, use `@MockitoBean` instead.
Same as the `@SpyBean`. So we will use the mockito one.

`@MockitoBean` is different from `@MockitoSpyBean`,
while the spy bean is a wrapped bean for selective mock.
`@MockitoSpyBean` will **call real method by default**.

To use these beans, simply add those annotations to your dependencies.

```java
@Service
public class TestedServiceImpl implements TestedService{
    @Autowired
    private DependencyBean dependency;
    @Autowired
    private AnotherBean another;
}
@SpringBootTest
class MyTest{
    // the bean we want to test with
    @Resource
    private TestedService service;
    // mock service's dependency
    @MockitoBean
    private DependencyBean dependency;
    // spy another dependency;
    @MockitoSpyBean
    private AnotherBean another;
}
```

In this case, two mocked beans will be injected into service bean by Spring IoC.

## Usage

### MockitoBean

Simple usage:

```java
@MockitoBean
private DependencyBean dependency;

void func(){
    Mockito.when(dependency.something(args)).thenReturn(new ObjectYouWant());
}

```

Sometimes, you may want to interact with method's args,
use `Mockito.doAnswer()` or `when().thenAnswer()`.

```java
Mockito.doAnswer(invocation -> {
                Product product = invocation.getArgument(0);
                // Simulate the database auto-generation of uuid
                product.setUuid("test-uuid-12345");
                return 1; // Return number of affected rows
            }).when(productMapper).insert(Mockito.any(Product.class));
```

### MockitoSpyBean

Instead of using `Mockito.when().thenReturn()`, you **must** use `Mockito.doReturn().when()` to
prevent it from calling real method.

This is because Spy bean will call the real method by default, it is a wrapped bean.
And `when().thenReturn()` will firstly call real method in `when()`.

Same as other `when().thenXxx()` flows.

### Mock static methods

Use `Mockito.mockStatic()` and `MockedStatic<T>` to create mocked static methods.

```java
@Test  
void staticMockTest() {  
    MockedStatic<StrUtil> strUtilMocked = Mockito.mockStatic(StrUtil.class);  
    strUtilMocked.when(StrUtil::isBlank).thenReturn(true);  
}
```

### Verify method calling

After calling mocked methods, you can use `Mockito.verify()` to verify if a method is called by
specific times.

As the code below, you can also verify the method argument.

```java
Mockito.verify(productMapper, Mockito.times(1)).insert(Mockito.any(Product.class));
```

For complex argument verification, use [Argument Captor](#argument-captor)

### Argument captor

To capture the method arguments and interact with them, use a `ArgumentCaptor<T>`.

You can use `@Captor` annotation on class captor field.

Argument captor must be used after the `Mockito.verify()` to capture arguments.

```java
@SpringBootTest
class MyTest{
    @MockitoBean
    private TestService service;
    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @Test
    void captorTest(){
        // mock
        Mockito.when(service.createProduct(any(Product.class))).thenReturn(1);
        // call it first
        service.createProduct(new Product());
        // verify before capture
        Mockito.verify(service).createProduct(productCaptor.capture());
        Product product = productCaptor.getValue();
    }
}
```

For methods that have multiple arguments, you can define multiple captors as well.
