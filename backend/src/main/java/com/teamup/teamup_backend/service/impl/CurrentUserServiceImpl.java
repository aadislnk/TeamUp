package com.teamup.teamup_backend.service.impl;

import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.constant.ApplicationConstants;
import com.teamup.teamup_backend.constant.SecurityConstants;
import com.teamup.teamup_backend.constant.ValidationMessages;
import com.teamup.teamup_backend.entity.User;
import com.teamup.teamup_backend.exception.UnauthorizedException;
import com.teamup.teamup_backend.security.model.CustomUserDetails;
import com.teamup.teamup_backend.service.CurrentUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    @Override
    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException(ApiMessages.UNAUTHORIZED);
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof CustomUserDetails customUserDetails)) {
            throw new UnauthorizedException("Invalid authentication.");
        }

        return customUserDetails.getUser();
    }
}