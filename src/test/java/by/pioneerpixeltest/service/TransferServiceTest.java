package by.pioneerpixeltest.service;

import by.pioneerpixeltest.dao.dto.TransferRequestDto;
import by.pioneerpixeltest.dao.entity.Account;
import by.pioneerpixeltest.dao.entity.User;
import by.pioneerpixeltest.exception.InsufficientFundsException;
import by.pioneerpixeltest.exception.UserValidationException;
import by.pioneerpixeltest.repository.UserRepository;
import by.pioneerpixeltest.service.impl.TransferServiceImpl;
import by.pioneerpixeltest.util.SecurityUtils;
import by.pioneerpixeltest.util.UserCacheUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserCacheUtil userCacheUtil;
    @InjectMocks
    private TransferServiceImpl transferService;
    private UUID senderId;
    private UUID recipientId;
    private User sender;
    private User recipient;
    private TransferRequestDto transferRequest;

    @BeforeEach
    void init() {
        senderId = UUID.randomUUID();
        recipientId = UUID.randomUUID();
        sender = new User();
        sender.setId(senderId);
        Account senderAccount = new Account();
        senderAccount.setBalance(new BigDecimal("100.00"));
        sender.setAccount(senderAccount);
        recipient = new User();
        recipient.setId(recipientId);
        Account recipientAccount = new Account();
        recipientAccount.setBalance(new BigDecimal("50.00"));
        recipient.setAccount(recipientAccount);
        transferRequest = new TransferRequestDto();
        transferRequest.setRecipientId(recipientId);
        transferRequest.setAmount(new BigDecimal("30.00"));
    }

    @Test
    void transferMoney_SuccessfulTransfer() {
        try (MockedStatic<SecurityUtils> securityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(senderId);
            when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
            when(userRepository.findById(recipientId)).thenReturn(Optional.of(recipient));

            transferService.transferMoney(transferRequest);

            assertEquals(new BigDecimal("70.00"), sender.getAccount().getBalance());
            assertEquals(new BigDecimal("80.00"), recipient.getAccount().getBalance());
            verify(userCacheUtil, times(1)).saveUser(sender);
            verify(userCacheUtil, times(1)).saveUser(recipient);
        }
    }

    @Test
    void transferMoney_InsufficientFunds() {
        transferRequest.setAmount(new BigDecimal("150.00"));

        try (MockedStatic<SecurityUtils> securityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(senderId);
            when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
            when(userRepository.findById(recipientId)).thenReturn(Optional.of(recipient));

            assertThrows(InsufficientFundsException.class, () -> transferService.transferMoney(transferRequest));
            assertEquals(new BigDecimal("100.00"), sender.getAccount().getBalance());
            assertEquals(new BigDecimal("50.00"), recipient.getAccount().getBalance());
            verify(userRepository, never()).save(any(User.class));
            verify(userCacheUtil, never()).saveUser(any());
        }
    }

    @Test
    void transferMoney_SenderNotFound() {
        try (MockedStatic<SecurityUtils> securityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(senderId);
            when(userRepository.findById(senderId)).thenReturn(Optional.empty());

            assertThrows(UserValidationException.class, () -> transferService.transferMoney(transferRequest));
            verify(userRepository, never()).save(any(User.class));
            verify(userCacheUtil, never()).saveUser(any());
        }
    }

    @Test
    void transferMoney_RecipientNotFound() {
        try (MockedStatic<SecurityUtils> securityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(senderId);
            when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
            when(userRepository.findById(recipientId)).thenReturn(Optional.empty());

            assertThrows(UserValidationException.class, () -> transferService.transferMoney(transferRequest));
            assertEquals(new BigDecimal("100.00"), sender.getAccount().getBalance());
            verify(userRepository, never()).save(any(User.class));
            verify(userCacheUtil, never()).saveUser(any());
        }
    }
}
