package com.footprint.domain.auth.service;

import com.footprint.domain.auth.dto.*;
import com.footprint.domain.auth.entity.RefreshToken;
import com.footprint.domain.auth.exception.AuthException;
import com.footprint.domain.auth.repository.RefreshTokenRepository;
import com.footprint.domain.user.entity.User;
import com.footprint.domain.user.exception.UserException;
import com.footprint.domain.user.repository.UserRepository;
import com.footprint.global.config.JwtProperties;
import com.footprint.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;

    @Transactional
    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw UserException.duplicateEmail();
        }
        if (userRepository.existsByNickname(request.nickname())) {
            throw UserException.duplicateNickname();
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = request.toEntity(encodedPassword);
        userRepository.save(user);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(UserException::notFound);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw UserException.passwordMismatch();
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        saveRefreshToken(user.getId(), refreshToken);

        return TokenResponse.of(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse reissue(ReissueRequest request) {
        if (!jwtTokenProvider.validateToken(request.refreshToken())) {
            throw AuthException.invalidToken();
        }

        RefreshToken storedToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(AuthException::invalidToken);

        if (storedToken.isExpired()) {
            refreshTokenRepository.delete(storedToken);
            throw AuthException.expiredToken();
        }

        Long userId = storedToken.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(UserException::notFound);

        String newAccessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        storedToken.updateToken(newRefreshToken, calculateRefreshTokenExpiry());

        return TokenResponse.of(newAccessToken, newRefreshToken);
    }

    private void saveRefreshToken(Long userId, String token) {
        refreshTokenRepository.findByUserId(userId)
                .ifPresentOrElse(
                        existing -> existing.updateToken(token, calculateRefreshTokenExpiry()),
                        () -> refreshTokenRepository.save(
                                RefreshToken.builder()
                                        .userId(userId)
                                        .token(token)
                                        .expiryDate(calculateRefreshTokenExpiry())
                                        .build()
                        )
                );
    }

    private LocalDateTime calculateRefreshTokenExpiry() {
        return LocalDateTime.now().plusSeconds(jwtProperties.refreshTokenExpiry() / 1000);
    }
}
