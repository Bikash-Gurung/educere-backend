package com.educere.api.experties;

import com.educere.api.entity.Experties;
import com.educere.api.entity.Tutor;
import com.educere.api.experties.dto.ExpertiesRequest;
import com.educere.api.experties.dto.ExpertiesResponse;
import com.educere.api.user.tutor.TutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpertiesService {

    @Autowired
    private ExpertiesMapper expertiesMapper;

    @Autowired
    private ExpertiesRepository expertiesRepository;

    @Autowired
    private TutorService tutorService;

    public List<ExpertiesResponse> addExperties(List<ExpertiesRequest> request, Long tutorId) {

        Tutor tutor = tutorService.getById(tutorId);
        List<Experties> experties = expertiesMapper.toExpertisesList(request);
        List<Experties> tutorList = experties.stream().map(experties1 -> {
            experties1.setTutor(tutor);
            expertiesRepository.save(experties1);
            return experties1;
    }
        ).collect(Collectors.toList());

        return expertiesMapper.toExpertisesResponseList(tutorList);
    }
}
