package kr.hhplus.concert.infrastructure.lock;

import kr.hhplus.concert.application.lock.DistributeLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@RequiredArgsConstructor
public class DistributeLockAspect {
    private final static String LOCK_KEY_PREFIX = "lock:";

    private final RedissonClient redissonClient;
    private final ExpressionParser parser = new SpelExpressionParser();
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(kr.hhplus.concert.application.lock.DistributeLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributeLock distributeLock = method.getAnnotation(DistributeLock.class);

        String key = getKey(joinPoint, distributeLock.key());
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("DistributedLock 키를 생성할 수 없습니다. SpEL 표현식을 확인하세요.");
        }
        long waitTimeSec = distributeLock.waitTimeSec();
        long leaseTimeSec = distributeLock.leaseTimeSec();

        RLock lock = redissonClient.getLock(key);
        log.info("redissonClient: {}", redissonClient);
        boolean available = false;

        try {
            available = lock.tryLock(waitTimeSec, leaseTimeSec, distributeLock.timeUnit());
            if (!available) {
                throw new IllegalStateException("잠금 획득 실패: " + key);
            }
            log.info("[DistributedLock] 락 획득 성공: {}", key);
            return aopForTransaction.proceed(joinPoint);
        } finally {
            if (available && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("[DistributedLock] 락 해제: {}", key);
            }
        }
    }

    private String getKey(ProceedingJoinPoint joinPoint, String keyExpression) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        String parsedKey = parser.parseExpression(keyExpression).getValue(context, String.class);
        return LOCK_KEY_PREFIX + parsedKey;
    }
}