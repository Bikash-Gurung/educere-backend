package com.educere.api.user.tutor;

import com.educere.api.common.ListResponse;
import com.educere.api.entity.Tutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/tutor")
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @GetMapping()
    public ListResponse getTutorsList() {
        List<Tutor> tutors = tutorService.getTutors();

        return new ListResponse(tutors);
    }
}
