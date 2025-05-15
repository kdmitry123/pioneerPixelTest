package by.pioneerpixeltest.mapper;

import by.pioneerpixeltest.dao.dto.UserDto;
import by.pioneerpixeltest.dao.entity.EmailData;
import by.pioneerpixeltest.dao.entity.PhoneData;
import by.pioneerpixeltest.dao.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "emails", source = "emailData", qualifiedByName = "emailDataToEmails")
    @Mapping(target = "phones", source = "phoneData", qualifiedByName = "phoneDataToPhones")
    @Mapping(target = "balance", source = "account.balance", defaultValue = "0.00")
    UserDto convertToDto(User user);

    @Named("emailDataToEmails")
    default List<String> emailDataToEmails(List<EmailData> emailData) {
        return emailData.stream().map(EmailData::getEmail).toList();
    }

    @Named("phoneDataToPhones")
    default List<String> phoneDataToPhones(List<PhoneData> phoneData) {
        return phoneData.stream().map(PhoneData::getPhone).toList();
    }
}
