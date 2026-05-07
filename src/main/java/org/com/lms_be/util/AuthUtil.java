package org.com.lms_be.util;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

public final class AuthUtil {

    private AuthUtil() {
    }

    public static Long currentUserId(Authentication auth) {
        if (auth == null || !(auth.getPrincipal() instanceof Long userId)) {
            throw new AccessDeniedException("Authentication required");
        }
        return userId;
    }
}
