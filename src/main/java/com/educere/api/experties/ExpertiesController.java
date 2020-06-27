package com.educere.api.experties;

import com.educere.api.experties.dto.ExpertiesRequest;
import com.educere.api.experties.dto.ExpertiesResponse;
import com.educere.api.security.CurrentUser;
import com.educere.api.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/experties")
public class ExpertiesController {

    @Autowired
    private ExpertiesService expertiesService;

    @PostMapping("")
    public List<ExpertiesResponse> addExperties(@Valid @RequestBody List<ExpertiesRequest> request, @CurrentUser UserPrincipal userPrincipal) {

        return expertiesService.addExperties(request, userPrincipal.getId());
    }
}
