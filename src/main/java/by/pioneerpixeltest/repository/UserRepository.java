package by.pioneerpixeltest.repository;


import by.pioneerpixeltest.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmailDataEmail(String email);

    boolean existsByPhoneDataPhone(String phone);
}
