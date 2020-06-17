package com.invivoo.vivwallet.api.infrastructure.lynx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Activities {

    @JsonProperty("dataTable")
    private List<Activity> activities;
}
