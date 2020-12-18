package com.invivoo.vivwallet.api.infrastructure.lynx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Activity {

    @JsonProperty("funnel_stageid")
    private Long funnelStageId;
    @JsonProperty("stage_name")
    private String stageName;
    @JsonProperty("activityid")
    private Long id;
    @JsonProperty("activity_typeid")
    private Long typeId;
    @JsonProperty("activity_label")
    private ActivityType type;
    @JsonProperty("act_date")
    private LocalDateTime date;
    @JsonProperty("start_act_from")
    private LocalDateTime startActFrom;
    @JsonProperty("act_date_end")
    private LocalDateTime endDate;
    @JsonProperty("value_date")
    private LocalDateTime valueDate;
    @JsonProperty("act_status")
    private String status;
    @JsonProperty("activity_comment")
    private String comment;
    @JsonProperty("relatedto")
    private String relatedTo;
    @JsonProperty("owner")
    private String owner;
    @JsonProperty("participant_email")
    private String participantEmail;
    @JsonProperty("others")
    private String others;
    @JsonProperty("opportunity")
    private String opportunity;
    @JsonProperty("opp_label")
    private String opportunityLabel;
    @JsonProperty("inv_entry")
    private LocalDateTime invEntry;
    @JsonProperty("validity")
    private String validity;
}
