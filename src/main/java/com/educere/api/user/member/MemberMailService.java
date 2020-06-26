package com.educere.api.user.member;

import com.educere.api.common.Constants;
import com.educere.api.common.enums.UserType;
import com.educere.api.config.MailConfig;
import com.educere.api.entity.Member;
import com.educere.api.entity.User;
import com.educere.api.mail.MailService;
import com.educere.api.user.institution.InstitutionService;
import com.educere.api.user.tutor.TutorService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class MemberMailService {

    private static final Logger logger = LoggerFactory.getLogger(MemberMailService.class);
    private final Configuration template;
    private final MailConfig mailConfig;
    @Autowired
    private MailService mailService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private InstitutionService institutionService;

    @Autowired
    MemberMailService(MailConfig mailConfig, Configuration template) {
        this.mailConfig = mailConfig;
        this.template = template;
    }

    @Async
    public void sendVerificationMail(User user, String verificationCode) {
        try {
            Template t = template.getTemplate("email-verification.ftl");
            Map<String, String> map = getTemplateVariable(user);
            map.put("VERIFICATION_CODE", verificationCode);
            String body = FreeMarkerTemplateUtils.processTemplateIntoString(t, map);
            mailService.sendMail(user.getEmail(), Constants.VERIFY_EMAIL_ID, body);
        } catch (Exception ex) {
            logger.error("Error while sending verification mail to member id: {}", user.getEmail(), ex);
        }
    }

    @Async
    public void sendMail(Member sender, String subject, String emailTemplate) {
        try {
            String templateName = emailTemplate;
            if (!templateName.isEmpty()) {
                Template t = template.getTemplate(templateName);
                String body = FreeMarkerTemplateUtils.processTemplateIntoString(t, getTemplateVariable(sender));
                mailService.sendMail(sender.getEmail(), subject, body);
            } else {
                logger.warn("No template found to send an email.");
            }

        } catch (Exception ex) {
            logger.error("Error while sending email", ex);
        }
    }

    private Map<String, String> getTemplateVariable(User user) {
        Map<String, String> map = new HashMap<>();
        map.put("USER_NAME", getUserName(user));
        map.put("BASE_URL", mailConfig.getBaseUrl());

        return map;
    }

    private String getUserName(User user){
        if(user.getUserType().equals(UserType.TUTOR)){
            return tutorService.getById(user.getId()).getFirstName();
        }

        return institutionService.getById(user.getId()).getInstitutionName();
    }
}
