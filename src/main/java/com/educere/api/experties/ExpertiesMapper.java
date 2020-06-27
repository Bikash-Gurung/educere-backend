package com.educere.api.experties;

import com.educere.api.entity.Experties;
import com.educere.api.experties.dto.ExpertiesRequest;
import com.educere.api.experties.dto.ExpertiesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ExpertiesMapper {

    Experties toExperties(ExpertiesRequest request);

    ExpertiesResponse toExpertiesResponse(Experties experties);
}
