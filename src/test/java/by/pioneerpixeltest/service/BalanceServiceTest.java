package by.pioneerpixeltest.service;

import by.pioneerpixeltest.dao.entity.Account;
import by.pioneerpixeltest.dao.entity.User;
import by.pioneerpixeltest.repository.UserRepository;
import by.pioneerpixeltest.service.impl.BalanceServiceImpl;
import by.pioneerpixeltest.util.UserCacheUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserCacheUtil userCacheUtil;
    @InjectMocks
    private BalanceServiceImpl balanceService;
    private User user1;
    private User user2;

    @BeforeEach
    void init() {
        user1 = createUser(UUID.randomUUID(), new BigDecimal("100.00"));
        user2 = createUser(UUID.randomUUID(), new BigDecimal("50.00"));
    }

    @Test
    void increaseBalances_ShouldIncreaseBalanceWithinLimit() {
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        balanceService.increaseBalances();

        assertEquals(new BigDecimal("110.00"), user1.getAccount().getBalance().setScale(2, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("55.00"), user2.getAccount().getBalance().setScale(2, RoundingMode.HALF_UP));
        verify(userCacheUtil, times(2)).saveUser(any(User.class));
    }

    @Test
    void increaseBalances_ShouldHandleZeroBalance() {
        user1.getAccount().setBalance(BigDecimal.ZERO);
        List<User> users = List.of(user1);
        when(userRepository.findAll()).thenReturn(users);

        balanceService.increaseBalances();

        assertEquals(new BigDecimal("0.10"), user1.getAccount().getBalance().setScale(2, RoundingMode.HALF_UP));
        verify(userCacheUtil, times(1)).saveUser(user1);
    }

    private User createUser(UUID id, BigDecimal balance) {
        User user = new User();
        user.setId(id);
        Account account = new Account();
        account.setBalance(balance);
        user.setAccount(account);
        return user;
    }
}
