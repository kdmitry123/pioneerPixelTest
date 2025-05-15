package by.pioneerpixeltest.repository;


import by.pioneerpixeltest.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    boolean existsByEmailDataEmailAndIdNot(String email, UUID userId);

    boolean existsByPhoneDataPhoneAndIdNot(String phone, UUID userId);

    Optional<User> findByName(String name);
}
