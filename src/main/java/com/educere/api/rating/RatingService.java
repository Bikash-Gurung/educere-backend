package com.educere.api.rating;

import com.educere.api.entity.Rating;
import com.educere.api.entity.User;
import com.educere.api.rating.dto.RatingResponse;
import com.educere.api.security.UserPrincipal;
import com.educere.api.user.UserService;
import com.educere.api.rating.dto.RatingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private UserService userService;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingMapper ratingMapper;

    public void rateTutor(RatingRequest ratingRequest){
        User user = userService.findById(ratingRequest.getUserId());
        Rating rating = new Rating();
        rating.setComment(ratingRequest.getComment());
        rating.setStar(ratingRequest.getStar());
        rating.setUser(user);
        save(rating);
    }

    public List<RatingResponse> getRatingList(UserPrincipal userPrincipal){
        List<Rating> rating = ratingRepository.findAllByUser(userService.findById(userPrincipal.getId()));
        return ratingMapper.toRatingResponseList(rating);
    }

    public Rating save(Rating rating){
        return ratingRepository.save(rating);
    }
}
