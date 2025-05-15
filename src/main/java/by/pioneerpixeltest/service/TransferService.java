package by.pioneerpixeltest.service;

import by.pioneerpixeltest.dao.dto.TransferRequestDto;

public interface TransferService {
    void transferMoney(TransferRequestDto transferRequest);
}
