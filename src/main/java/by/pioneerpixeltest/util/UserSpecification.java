package by.pioneerpixeltest.util;

import by.pioneerpixeltest.dao.dto.UserSearchDto;
import by.pioneerpixeltest.dao.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    private UserSpecification() {
    }

    public static Specification<User> userFilter(UserSearchDto searchDto) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (searchDto.getPhone() != null && !searchDto.getPhone().isEmpty()) {
                predicates.add(cb.equal(
                        root.join("phoneData").get("phone"),
                        searchDto.getPhone()
                ));
            }

            if (searchDto.getEmail() != null && !searchDto.getEmail().isEmpty()) {
                predicates.add(cb.equal(
                        root.join("emailData").get("email"),
                        searchDto.getEmail()
                ));
            }

            if (searchDto.getName() != null && !searchDto.getName().isEmpty()) {
                predicates.add(cb.like(root.get("name"), searchDto.getName() + "%"));
            }

            if (searchDto.getDateOfBirth() != null) {
                predicates.add(cb.greaterThan(root.get("dateOfBirth"), searchDto.getDateOfBirth()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
