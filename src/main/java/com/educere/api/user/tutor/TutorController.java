package com.educere.api.user.tutor;

import com.educere.api.common.ListResponse;
import com.educere.api.user.tutor.dto.TutorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/tutors")
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @GetMapping()
    public ListResponse getTutorsList() {
        List<TutorResponse> tutors = tutorService.getTutors();

        return new ListResponse(tutors);
    }
}
