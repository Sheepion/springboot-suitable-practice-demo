# Service layer design practices in enterprise-level applications

This doc is aimed to introduce to you a good service layer design
when implementing a enterprise-level(industrial) software development.

## Why a dedicated service layer?

New programmers might consider a dedicated service layer is unnecessary,
and may attempt to implement business logic directly within the controller or data access layers.

However, in industrial development, it is common practice to  **encapsulate business logic**
to service layer to keep core logic separate from web or data concerns.

A dedicated layer simply brings those benefits:

- Encapsulating business logic
- Reusability: allowing multiple controllers or other modules to use the same service.
- Testability: facilitating unit testing by isolating business logic.
- Declarative transactions: after encapsulating business logic, declarative transaction management is easier to accomplish.

## Core concepts of service layer

### Interface-Driven Design

Always define a **Service Interface** to decouple the contract from the implementation:

```java
public interface PaymentService {
    void pay(double amount);
}

@Service("creditCardPaymentService")
public class CreditCardPaymentService implements PaymentService {
}

@Service("paypalPaymentService")
public class PaypalPaymentService implements PaymentService {
}
```

By doing this, other modules can call the service interface instead of specific implement class.
And implement class can be replaced without affecting other modules.
Spring Ioc container willmanage these beans.

This also brings easier mocking during testing. We only need to mock one interface class rather than
specific implements.

### Decouple from data layer using VO

If you simply use models(entities) in your controller, it might be unsafe and inflexible for further
development as your business logic becomes more complex.

For example, you don't want user's password being returned in your controller, so you
need to manually remove the password field every time before returning the user model.

In this case, returning model is unsafe and unreliable, so it is recommonding use a
VO(View Object) in service layer.

By using VOs, only explicitly declared fields from the model are returned. This approach ensures decoupling, even when controller or model fields change.

However, in a real project, it might be inevitable to keep methods that return models directly,
for internal use between services. So don't stuck in principles, stick to your real demands.

```java
public interface ProductService {
    /**
     * Internal use only! Not recommonded technically.
     * @param id
     * @return model
     */
    Product getProductById(String id);

    /**
     * commonly used to return product information to the client or other modules.
     * @param uuid
     * @return vo
     */
    ProductVo getProductByUuid(String uuid);
}
```

### Receive arguments by DTO

DTO(Data Transfer Object), usually used between controller layer and service layer.

Just like VO, it is recommonded to receive arguments by DTOs instead of a list of fields or map.

This also provide decoupling from other layers, you can only add necessary fields in DTO.
And when your service added new argument, you can only modify the DTO class for easier maintaining.

By using DTO to wrap your controller arguments, you can bring up automatical validation using
Jakarta validation API or other validation utils.

### Validation and business rule enforcement

By encapsulating business logic into service methods, you can validate arguments
in your service while other modules reuse the business logic, enhancing the robustness.

Only adding validation in controllers is **absolutely unsafe**, since any other modules may call
service methods either.

## Common issue

We take lots of benefits from the IoC container, but it can brings issue if used not properly.

Recommond read:

- [Spring official IoC document](https://docs.spring.io/spring-framework/reference/core/beans/introduction.html)

### Declarative transaction management

When Spring creates a transactional service bean, it doesn't directly use the original bean instance. Instead, it creates a proxy object that wraps the original bean. So when you trying to
**self-invocation** in a service impl bean, it bypass the proxy chain.

In result, your `@Transactional` annotation will not take effect anymore, because it only exist in 
the proxy bean.

```java
@Service
public class BalanceServiceImpl implements BalanceService{
    @Transactional
    public void transfer(String from, String to, BigDecimal amount){
        // self-invocation!!! Spring's proxy chain is bypassed
        debit(from, amount);
        credit(to, amount);
    }

    @Transactional
    public void debit(String from, BigDecimal amount){...}

    @Transactional
    public void credit(String to, BigDecimal amount){...}
}

```

In this scenario, you may refactor your code to extract those methods to another bean.
But this may make your service looks weird, imagine your `transfer` and other balance related
methods are in different serviceImpl.

So another solution is self-reference, which means inject this bean to itself.

```java
@Service
public class BalanceServiceImpl implements BalanceService{

    // self-reference. you may use @Lazy or other ways to inject, it depends.
    @Autowired
    private BalanceServiceImpl self;

    @Transactional
    public void transfer(String from, String to, BigDecimal amount){
        // call by the proxy bean. the transaction propagate by default.
        self.debit(from, amount);
        self.credit(to, amount);
    }

    @Transactional
    public void debit(String from, BigDecimal amount){...}

    @Transactional
    public void credit(String to, BigDecimal amount){...}
}

```

### Circular Dependencies

For some reason, circular dependencies between beans are a common scenario.

It might be two different case:

1. Design issue. Common logic can be extracted to a public service.
2. Business issue. You do need circular dependencies for your business logic.

The design issue is easy to address, just refactor your code.

The business issue may imply underlying design issue, please review your code closely first.

If you are sure about your business issue not caused by design issue, you may use lazy initialization.

```java
@Service
public class ServiceA{
    @Autowired
    @Lazy
    private ServiceB serviceB;
}

@Service
public class ServiceB{
    @Autowired
    @Lazy
    private ServiceA serviceA;
}
```
