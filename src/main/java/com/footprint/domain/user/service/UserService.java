package com.footprint.domain.user.service;

import com.footprint.domain.user.dto.UpdateUserRequest;
import com.footprint.domain.user.dto.UserResponse;
import com.footprint.domain.user.entity.User;
import com.footprint.domain.user.exception.UserException;
import com.footprint.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserException::notFound);
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse updateMyProfile(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserException::notFound);

        // 닉네임 변경 시 중복 체크 (본인 닉네임과 다른 경우만)
        if (!user.getNickname().equals(request.getNickname())
                && userRepository.existsByNickname(request.getNickname())) {
            throw UserException.duplicateNickname();
        }

        user.updateProfile(
                request.getNickname(),
                request.getName(),
                request.getBirth(),
                request.getGender()
        );

        return UserResponse.from(user);
    }

    @Transactional
    public void deleteMyAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserException::notFound);
        user.delete();
    }
}
