package org.lt.project.service;

import org.lt.project.core.AuthenticationUtil;
import org.lt.project.core.JwtTokenUtil;
import org.lt.project.core.result.DataResult;
import org.lt.project.core.result.ErrorDataResult;
import org.lt.project.core.result.SuccessDataResult;
import org.lt.project.dto.AuthenticationRequestDto;
import org.lt.project.dto.AuthenticationResponseDto;
import org.lt.project.entity.UserEntity;
import org.lt.project.repository.AuthenticationRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private final AuthenticationRepository authenticationRepository;
    private final AuthenticationUtil authenticationUtil;

    public UserService(AuthenticationRepository authenticationRepository,
            AuthenticationUtil authenticationUtil) {
        this.authenticationRepository = authenticationRepository;
        this.authenticationUtil = authenticationUtil;
    }

    public DataResult<AuthenticationResponseDto> login(AuthenticationRequestDto requestDto) {
        Optional<UserEntity> optionalAuthenticationEntity = authenticationRepository
                .getByUsername(requestDto.getUsername());
        if (optionalAuthenticationEntity.isPresent()) {
            UserEntity authenticationEntity = optionalAuthenticationEntity.get();
            boolean isTrue = authenticationUtil.isPasswordMatch(requestDto.getPassword(),
                    authenticationEntity.getPassword());
            if (isTrue) {
                String token = JwtTokenUtil.generateToken(authenticationEntity.getUsername());
                AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto(
                        authenticationEntity.getUsername(),
                        authenticationEntity.getLastLoginDate(), token);
                updateLastLoginDate(requestDto.getUsername());
                return new SuccessDataResult<>("Giriş başarılı.",
                        authenticationResponseDto);
            } else {
                return new ErrorDataResult<>("Kullanıcı adı yada şifre yanlış.");
            }
        } else {
            return new ErrorDataResult<>("Kullanıcı adı yada şifre yanlış.");
        }
    }

    public DataResult<AuthenticationResponseDto> addUser(AuthenticationRequestDto requestDto) {
        UserEntity authenticationEntity = new UserEntity();
        authenticationEntity.setIsActive(true);
        authenticationEntity.setLastLoginDate(new Date());
        authenticationEntity.setUsername(requestDto.getUsername());
        authenticationEntity.setPassword(authenticationUtil.getHashedPassword(requestDto.getPassword()));
        authenticationRepository.save(authenticationEntity);
        return login(requestDto);
    }

    public DataResult<UserEntity> updateLastLoginDate(String username) {
        DataResult<UserEntity> dataResultAuthenticationEntity = getUserByUsername(username);
        if (dataResultAuthenticationEntity.isSuccess()) {
            dataResultAuthenticationEntity.getData().setLastLoginDate(new Date());
            UserEntity savedEntity = authenticationRepository.save(dataResultAuthenticationEntity.getData());
            if (savedEntity != null) {
                return new SuccessDataResult<>(savedEntity);
            }
        }
        return new ErrorDataResult<>("güncellenemedi");
    }

    public DataResult<UserEntity> getUserByUsername(String username) {
        Optional<UserEntity> optionalAuthenticationEntity = authenticationRepository
                .getByUsername(username);
        if (optionalAuthenticationEntity.isPresent()) {
            return new SuccessDataResult<>(optionalAuthenticationEntity.get());
        } else {
            return new ErrorDataResult<>("Böyle bir kullanıcı yok");
        }
    }

    public DataResult<String> getHashedPassword(String password) {
        return new SuccessDataResult<>("Şifre hashlandi şimdi DB içerisine yeni kullanıcıyı ekleyebilirsiniz.",
                authenticationUtil.getHashedPassword(password));
    }
}
