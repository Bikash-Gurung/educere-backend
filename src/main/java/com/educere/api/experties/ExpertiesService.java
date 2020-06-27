package com.educere.api.experties;

import com.educere.api.entity.Experties;
import com.educere.api.entity.Tutor;
import com.educere.api.experties.dto.ExpertiesRequest;
import com.educere.api.experties.dto.ExpertiesResponse;
import com.educere.api.user.tutor.TutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpertiesService {

    @Autowired
    private ExpertiesMapper expertiesMapper;

    @Autowired
    private ExpertiesRepository expertiesRepository;

    @Autowired
    private TutorService tutorService;

    public ExpertiesResponse addExperties(ExpertiesRequest request, Long tutorId) {

        Tutor tutor = tutorService.getById(tutorId);
        Experties experties = expertiesMapper.toExperties(request);
        experties.setTutor(tutor);
        expertiesRepository.save(experties);

        return expertiesMapper.toExpertiesResponse(experties);
    }
}
