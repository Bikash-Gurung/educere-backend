package com.educere.api.rating;

import com.educere.api.common.ListResponse;
import com.educere.api.security.CurrentUser;
import com.educere.api.security.UserPrincipal;
import com.educere.api.user.tutor.dto.RatingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping("")
    @PreAuthorize("hasAnyRole('INSTITUTION','TUTOR')")
    public ListResponse getRatingList(@CurrentUser UserPrincipal userPrincipal) {
        return new ListResponse(ratingService.getRatingList(userPrincipal));
    }

    @PostMapping("/rate")
    @PreAuthorize("hasAnyRole('INSTITUTION','TUTOR')")
    public String  rateTutor(@Valid @RequestBody RatingRequest ratingRequest) {
        ratingService.rateTutor(ratingRequest);
        return "Thank you for your feedback.";
    }
}
