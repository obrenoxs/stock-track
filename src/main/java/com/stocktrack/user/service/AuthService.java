package com.stocktrack.user.service;

import com.stocktrack.shared.security.JwtTokenProvider;
import com.stocktrack.user.dto.request.LoginRequestDTO;
import com.stocktrack.user.dto.request.UserRequestDTO;
import com.stocktrack.user.dto.response.AuthResponseDTO;
import com.stocktrack.user.dto.response.UserResponseDTO;
import com.stocktrack.user.entity.User;
import com.stocktrack.user.mapper.UserMapper;
import com.stocktrack.user.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String TOKEN_TYPE = "Bearer";

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponseDTO register(UserRequestDTO dto) {
        UserResponseDTO created = userService.register(dto);
        String token = jwtTokenProvider.generateToken(created.re(), created.role().name());
        return new AuthResponseDTO(token, TOKEN_TYPE, created);
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.re(), dto.password())
        );

        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        String token = jwtTokenProvider.generateToken(user.getRe(), user.getRole().name());

        return new AuthResponseDTO(token, TOKEN_TYPE, userMapper.toResponseDTO(user));
    }
}
