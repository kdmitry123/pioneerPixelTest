package by.pioneerpixeltest.service;

import by.pioneerpixeltest.dao.entity.User;

public interface BalanceService {
    void increaseBalances();

    void registerNewUser(User user);
}
