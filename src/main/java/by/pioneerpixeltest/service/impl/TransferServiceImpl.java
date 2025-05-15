package by.pioneerpixeltest.service.impl;

import by.pioneerpixeltest.dao.dto.TransferRequestDto;
import by.pioneerpixeltest.dao.dto.UserDto;
import by.pioneerpixeltest.dao.entity.User;
import by.pioneerpixeltest.exception.InsufficientFundsException;
import by.pioneerpixeltest.exception.UserValidationException;
import by.pioneerpixeltest.mapper.UserMapper;
import by.pioneerpixeltest.repository.UserRepository;
import by.pioneerpixeltest.service.TransferService;
import by.pioneerpixeltest.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    @Override
    @Transactional
    public void transferMoney(TransferRequestDto transferRequest) {
        UUID senderId = SecurityUtils.getCurrentUserId();
        UUID recipientId = transferRequest.getRecipientId();
        BigDecimal amount = transferRequest.getAmount();
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UserValidationException("Sender not found"));

        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new UserValidationException("Recipient not found"));
        validateTransfer(sender, amount);
        performTransfer(sender, recipient, amount);

        log.info("Successful transfer from {} to {} amount {}", senderId, recipientId, amount);
    }

    private void validateTransfer(User sender, BigDecimal amount) {
        if (sender.getAccount().getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for transfer");
        }
    }

    private void performTransfer(User sender, User recipient, BigDecimal amount) {
        sender.getAccount().setBalance(sender.getAccount().getBalance().subtract(amount));
        recipient.getAccount().setBalance(recipient.getAccount().getBalance().add(amount));

        saveUser(sender);
        saveUser(recipient);
    }

    private void saveUser(User user) {
        UserDto dto = UserMapper.convertToDto(userRepository.save(user));
        updateUserInCache(dto);
    }

    private void updateUserInCache(UserDto dto) {
        cacheManager.getCache("users").put(dto.getId(), dto);
    }

}
