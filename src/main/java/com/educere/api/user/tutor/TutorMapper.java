package com.educere.api.user.tutor;

import com.educere.api.entity.Tutor;
import com.educere.api.user.auth.dto.SignUpRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TutorMapper {

    Tutor toTutor(SignUpRequest signUpRequest);
}
