package com.stocktrack.user.service;

import com.stocktrack.shared.exception.DuplicateResourceException;
import com.stocktrack.shared.exception.ResourceNotFoundException;
import com.stocktrack.user.dto.request.UserRequestDTO;
import com.stocktrack.user.dto.request.UserUpdateRequestDTO;
import com.stocktrack.user.dto.response.UserResponseDTO;
import com.stocktrack.user.entity.User;
import com.stocktrack.user.enums.Role;
import com.stocktrack.user.exception.InvalidCurrentPasswordException;
import com.stocktrack.user.mapper.UserMapper;
import com.stocktrack.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO register(UserRequestDTO dto) {
        return createUser(dto, Role.COLLABORATOR);
    }

    @Transactional
    public UserResponseDTO createWarehouseManager(UserRequestDTO dto) {
        return createUser(dto, Role.WAREHOUSE_MANAGER);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findByRe(String re) {
        User user = getUserOrThrow(re);
        return userMapper.toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateSelf(String re, UserUpdateRequestDTO dto) {
        User user = getUserOrThrow(re);
        userMapper.updateEntityFromDto(dto, user);

        if(dto.newPassword() != null && !dto.newPassword().isBlank()) {
            validateCurrentPassword(dto, user);
            user.setPassword(passwordEncoder.encode(dto.newPassword()));
        }

        User saved = userRepository.save(user);
        return userMapper.toResponseDTO(saved);
    }

    private UserResponseDTO createUser(UserRequestDTO dto, Role role) {
        validateReNotDuplicated(dto.re());

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(role);

        User saved = userRepository.save(user);
        return userMapper.toResponseDTO(saved);
    }

    private void validateCurrentPassword(UserUpdateRequestDTO dto, User user) {
        if (dto.currentPassword() == null || !passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new InvalidCurrentPasswordException("Senha atual incorreta");
        }
    }

    private void validateReNotDuplicated(String re) {
        if (userRepository.existsByRe(re)) {
            throw new DuplicateResourceException("RE já cadastrado: " + re);
        }
    }

    private User getUserOrThrow(String re) {
        return userRepository.findByRe(re)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com RE: " + re));
    }
}
