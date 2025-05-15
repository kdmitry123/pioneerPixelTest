package by.pioneerpixeltest;

import by.pioneerpixeltest.mapper.UserMapper;
import by.pioneerpixeltest.repository.UserRepository;
import by.pioneerpixeltest.service.UserService;
import by.pioneerpixeltest.service.impl.UserServiceImpl;
import by.pioneerpixeltest.util.UserCacheUtil;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@TestConfiguration
@EnableJpaRepositories(basePackages = "by.pioneerpixeltest.repository")
@EnableTransactionManagement
public class UserServiceTestConfig {
    @Bean
    public UserService userService(UserRepository userRepository, UserCacheUtil userCacheUtil, UserMapper userMapper) {
        return new UserServiceImpl(userRepository, userMapper);
    }

    @Bean
    public UserCacheUtil userCacheUtil(UserRepository userRepository, UserMapper userMapper) {
        return new UserCacheUtil(userRepository, null, userMapper);
    }

    @Bean
    public UserMapper userMapper() {
        return Mappers.getMapper(UserMapper.class);
    }
}