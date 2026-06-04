package com.familyleague.service;

import com.familyleague.dto.request.UpdateProfileRequest;
import com.familyleague.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse getProfile(String username);
    UserResponse updateProfile(String username, UpdateProfileRequest request);
    Page<UserResponse> getAllUsers(Pageable pageable);
}
