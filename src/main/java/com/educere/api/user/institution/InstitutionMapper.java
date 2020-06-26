package com.educere.api.user.institution;

import com.educere.api.entity.Institution;
import com.educere.api.user.auth.dto.SignUpRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface InstitutionMapper {

    Institution toInstitution(SignUpRequest signUpRequest);
}
