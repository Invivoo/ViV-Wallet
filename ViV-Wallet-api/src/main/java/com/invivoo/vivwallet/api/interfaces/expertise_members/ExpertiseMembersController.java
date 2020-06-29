package com.invivoo.vivwallet.api.interfaces.expertise_members;

import com.invivoo.vivwallet.api.domain.expertise.ExpertiseMember;
import com.invivoo.vivwallet.api.domain.expertise.ExpertiseMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(ExpertiseMembersController.API_V1_EXPERTISES)
public class ExpertiseMembersController {

    static final String API_V1_EXPERTISES = "/api/v1/expertiseMembers";
    private final ExpertiseMemberRepository expertiseMemberRepository;

    @Autowired
    public ExpertiseMembersController(ExpertiseMemberRepository expertiseMemberRepository) {
        this.expertiseMemberRepository = expertiseMemberRepository;
    }

    @GetMapping
    public ResponseEntity<List<ExpertiseMember>> getAllExpertiseMembers() {
        List<ExpertiseMember> expertiseMembers = expertiseMemberRepository.findAll();
        return ResponseEntity.ok(expertiseMembers);
    }

    @PostMapping
    public ResponseEntity<ExpertiseMember> postExpertiseMember(@RequestBody ExpertiseMember expertiseMember) {
        ExpertiseMember savedExpertiseMember = expertiseMemberRepository.save(expertiseMember);
        return ResponseEntity.created(getLocation(savedExpertiseMember))
                .body(expertiseMember);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpertiseMember> getExpertiseMember(@PathVariable("id") Long expertiseMemberId) {
        return expertiseMemberRepository.findById(expertiseMemberId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound()
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpertiseMember> putExpertiseMember(@PathVariable("id") Long expertiseMemberId, @RequestBody ExpertiseMemberUpdateDTO expertiseMemberUpdate) {
        return expertiseMemberRepository.findById(expertiseMemberId)
                .map(expertiseMember -> updateExpertiseMember(expertiseMember, expertiseMemberUpdate))
                .map(expertiseMemberRepository::save)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound()
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ExpertiseMember> deleteExpertiseMember(@PathVariable("id") Long expertiseMemberId) {
        return expertiseMemberRepository.findById(expertiseMemberId)
                .map(this::deleteExpertiseMember)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound()
                        .build());
    }

    private ExpertiseMember deleteExpertiseMember(ExpertiseMember expertiseMember) {
        expertiseMemberRepository.delete(expertiseMember);
        return expertiseMember;
    }

    private URI getLocation(ExpertiseMember savedExpertiseMember) {
        return URI.create(String.format("%s/%s", API_V1_EXPERTISES, savedExpertiseMember.getId()));
    }

    private ExpertiseMember updateExpertiseMember(ExpertiseMember expertiseMember, ExpertiseMemberUpdateDTO expertiseMemberUpdate) {
        expertiseMember.setStatus(expertiseMemberUpdate.getStatus());
        expertiseMember.setStartDate(expertiseMemberUpdate.getStartDate());
        expertiseMember.setEndDate(expertiseMemberUpdate.getEndDate());
        return expertiseMember;
    }
}

