package by.pioneerpixeltest.service.impl;

import by.pioneerpixeltest.dao.entity.User;
import by.pioneerpixeltest.repository.UserRepository;
import by.pioneerpixeltest.service.BalanceService;
import by.pioneerpixeltest.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {
    private final Map<UUID, BigDecimal> initialBalances = new ConcurrentHashMap<>();
    private final UserRepository userRepository;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final UserService userService;

    @Value("${increase-balances.period:30}")
    private Integer period;

    @PostConstruct
    public void init() {
        userRepository.findAll().forEach(this::registerNewUser);
        scheduler.scheduleAtFixedRate(this::increaseBalances, period, period, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdown();
    }

    @Override
    @Transactional
    public void increaseBalances() {
        log.info("Balance increase task iteration");
        userRepository.findAll().forEach(user -> {
            BigDecimal currentBalance = user.getAccount().getBalance();
            BigDecimal initialBalance = initialBalances.get(user.getId());
            if (initialBalance == null) {
                initialBalance = currentBalance;
                initialBalances.put(user.getId(), initialBalance);
                log.info("Initial balance set for new user {}: {}", user.getId(), initialBalance);
            }
            if (initialBalance.compareTo(BigDecimal.ZERO) == 0) {
                initialBalance = new BigDecimal("0.1");
                initialBalances.put(user.getId(), initialBalance);
                log.info("Initial balance adjusted for user {}: 0 -> 0.1", user.getId());
            }
            BigDecimal maxBalance = initialBalance.multiply(new BigDecimal("2.07"));
            BigDecimal increasedBalance = currentBalance.multiply(new BigDecimal("1.10"));
            if (currentBalance.compareTo(BigDecimal.ZERO) == 0) {
                increasedBalance = new BigDecimal("0.1");
            }
            if (increasedBalance.compareTo(maxBalance) <= 0) {
                user.getAccount().setBalance(increasedBalance);
                userService.saveAndCacheUser(user);
                log.info("Increased balance for user {}: {} -> {}",
                        user.getId(), currentBalance, increasedBalance);
            } else {
                log.info("Balance increase skipped for user {} - would exceed maximum",
                        user.getId());
            }
        });
    }

    @Override
    public void registerNewUser(User user) {                // если создается новый пользователь, то после сохранения добавить вызов registerNewUser(savedUser);
        BigDecimal initialBalance = user.getAccount().getBalance();
        initialBalances.put(user.getId(), initialBalance);
        log.info("Registered new user {} with initial balance {}", user.getId(), initialBalance);
    }
}
