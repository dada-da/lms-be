package org.com.lms_be.util;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

public final class PublishStatusAccess {

    private static final Set<String> PRIVILEGED_ROLES = Set.of("ROLE_TEACHER", "ROLE_ADMIN");

    private PublishStatusAccess() {
    }

    public static List<PublishStatus> resolveVisibleStatuses(PublishStatus requested, Authentication auth) {
        boolean privileged = isPrivileged(auth);

        if (requested != null) {
            if (!privileged && requested != PublishStatus.PUBLISHED) {
                throw new AccessDeniedException("Only published items are visible to your role");
            }
            return List.of(requested);
        }

        return privileged
                ? List.of(PublishStatus.DRAFT, PublishStatus.PUBLISHED)
                : List.of(PublishStatus.PUBLISHED);
    }

    private static boolean isPrivileged(Authentication auth) {
        if (auth == null) {
            return false;
        }
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(PRIVILEGED_ROLES::contains);
    }
}
