package com.familyleague.service.impl;

import com.familyleague.dto.request.UpdateProfileRequest;
import com.familyleague.dto.response.UserResponse;
import com.familyleague.entity.User;
import com.familyleague.exception.ResourceNotFoundException;
import com.familyleague.repository.UserRepository;
import com.familyleague.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getProfile(String username) {
        User user = findActiveUser(username);
        return toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateProfile(String username, UpdateProfileRequest request) {
        User user = findActiveUser(username);
        if (request.getDisplayName() != null) user.setDisplayName(request.getDisplayName());
        if (request.getAvatarName() != null) user.setAvatarName(request.getAvatarName());
        return toResponse(userRepository.save(user));
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toResponse);
    }

    private User findActiveUser(String username) {
        return userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .avatarName(user.getAvatarName())
                .role(user.getRole().name())
                .active(user.isActive())
                .build();
    }
}
