package kr.hhplus.concert.application.lock;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DistributeLock {
    String key();         // SpEL로 동적 키 만들기
    TimeUnit timeUnit() default TimeUnit.SECONDS;
    long waitTimeSec() default 5L;
    long leaseTimeSec() default 5L;
}
