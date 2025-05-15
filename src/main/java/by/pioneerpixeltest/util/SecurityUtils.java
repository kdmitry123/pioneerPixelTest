package by.pioneerpixeltest.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

@UtilityClass
public class SecurityUtils {

    public static UUID getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() instanceof String) {
            throw new AccessDeniedException("User is not authenticated");
        }
        return (UUID) authentication.getPrincipal();
    }
}
