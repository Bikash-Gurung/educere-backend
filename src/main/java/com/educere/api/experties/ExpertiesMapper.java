package com.educere.api.experties;

import com.educere.api.entity.Experties;
import com.educere.api.experties.dto.ExpertiesRequest;
import com.educere.api.experties.dto.ExpertiesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ExpertiesMapper {

    Experties toExperties(ExpertiesRequest request);

    List<Experties> toExpertisesList(List<ExpertiesRequest> requests);

    ExpertiesResponse toExpertiesResponse(Experties experties);

    List<ExpertiesResponse> toExpertisesResponseList(List<Experties> experties);
}
