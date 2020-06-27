package com.educere.api.rating;

import com.educere.api.entity.Rating;
import com.educere.api.rating.dto.RatingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RatingMapper {

    List<RatingResponse> toRatingResponseList(List<Rating> rating);
}
