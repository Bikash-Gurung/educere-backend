package com.educere.api.user.password;

import com.educere.api.entity.ResetPassword;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ResetPasswordRepository extends PagingAndSortingRepository<ResetPassword, Long> {
    ResetPassword findByToken(String token);

    ResetPassword findByUserIdAndExpiryDateIsAfter(Long userId, LocalDateTime localDateTime);
}
