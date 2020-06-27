package com.educere.api.user.tutor;

import com.educere.api.entity.Tutor;
import com.educere.api.user.auth.dto.SignUpRequest;
import com.educere.api.user.tutor.dto.TutorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TutorMapper {

    Tutor toTutor(SignUpRequest signUpRequest);

    Tutor ToTutorResponse(Tutor tutor);

    List<TutorResponse> toTutorResponseList(List<Tutor> tutors);


}
