package com.invivoo.vivwallet.api.infrastructure.lynx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LynxUser {
	@JsonProperty("cand_hr_login")
	private String candHrLogin;
	@JsonProperty("cand_rp_login")
	private String candRpLogin;
	@JsonProperty("cand_rc_login")
	private String candRcLogin;
	@JsonProperty("cand_id")
	private String candId;
	@JsonProperty("cand_state")
	private String candState;
	@JsonProperty("cand_lastname")
	private String candLastname;
	@JsonProperty("cand_firstname")
	private String candFirstname;
	@JsonProperty("cand_degrees")
	private String candDegree;
	@JsonProperty("cand_skills")
	private String candSkills;
	@JsonProperty("inv_entry")
	private LocalDateTime invEntry;
	@JsonProperty("inv_exit")
	private LocalDateTime invExit;
	@JsonProperty("cust_start")
	private LocalDateTime custStart;
	@JsonProperty("cust_stop")
	private LocalDateTime custStop;
	@JsonProperty("cand_pos_type")
	private String candPosType;
	@JsonProperty("cand_pos_typeid")
	private Long candPosTypeid;
	@JsonProperty("int_ext")
	private String intExt;
	@JsonProperty("cand_resume_mainposition")
	private String candResumeMainposition;
	@JsonProperty("cand_resume_experience")
	private String candResumeExperience;
	@JsonProperty("cand_resume_tech")
	private String candResumeTech;
	@JsonProperty("xp")
	private long xp;
	@JsonProperty("cand_avaibility_date")
	private LocalDateTime candAvaibilityDate;
	@JsonProperty("cand_avaibility_days")
	private LocalDateTime candAvailbilityDays;
	@JsonProperty("cand_rg_contactid")
	private String cangRgContactod;
	@JsonProperty("cand_rg_firstname")
	private String cangRgFirstname;
	@JsonProperty("cand_rg_lastname")
	private String cangRgLastname;
	@JsonProperty("inv_customer_team")
	private String invCustomerTeam;
	@JsonProperty("inv_position_label")
	private String invPositionLabel;
	@JsonProperty("inv_gt_position")
	private String invGtPosition;
	@JsonProperty("RC")
	private String RC;
	@JsonProperty("RP")
	private String RP;
	@JsonProperty("Client")
	private String client;
	@JsonProperty("Projet")
	private String projet;
	@JsonProperty("Nom")
	private String nom;
	@JsonProperty("Prénom")
	private String prenom;
	@JsonProperty("Entrée")
	private LocalDateTime entree;
	@JsonProperty("Sortie")
	private LocalDateTime sortie;
	@JsonProperty("Début")
	private LocalDateTime debut;
	@JsonProperty("Fin")
	private LocalDateTime fin;
	@JsonProperty("Durée")
	private Float duree;
	@JsonProperty("G0")
	private Long G0;

}
