package com.familyleague.controller;

import com.familyleague.dto.request.BulkNotifyRequest;
import com.familyleague.dto.response.ApiResponse;
import com.familyleague.entity.EmailLog;
import com.familyleague.entity.User;
import com.familyleague.enums.EmailType;
import com.familyleague.exception.ResourceNotFoundException;
import com.familyleague.repository.EmailLogRepository;
import com.familyleague.repository.UserRepository;
import com.familyleague.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Tag(name = "Admin")
public class AdminController {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final EmailLogRepository emailLogRepository;

    @PostMapping("/notify")
    @Operation(summary = "Bulk notify selected users with a custom message")
    public ResponseEntity<ApiResponse<Void>> bulkNotify(@Valid @RequestBody BulkNotifyRequest request) {
        List<String> emails = request.getUserIds().stream()
                .map(id -> userRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id)))
                .map(User::getEmail)
                .toList();
        emailService.sendBulk(emails, request.getSubject(), request.getMessage(), EmailType.BULK_NOTIFY);
        return ResponseEntity.ok(ApiResponse.ok("Notifications queued", null));
    }

    @GetMapping("/emails")
    @Operation(summary = "View email log (paginated)")
    public ResponseEntity<ApiResponse<Page<EmailLog>>> getEmailLogs(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(emailLogRepository.findAllByDeletedFalse(pageable)));
    }
}
