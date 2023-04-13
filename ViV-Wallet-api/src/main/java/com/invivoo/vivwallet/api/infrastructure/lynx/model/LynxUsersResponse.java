package com.invivoo.vivwallet.api.infrastructure.lynx.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LynxUsersResponse {

	@JsonProperty("dataTable")
	private List<LynxUser> users;

	@JsonIgnore
	public int size() {
		return Optional.ofNullable(users)
					   .map(List::size)
					   .orElse(0);
	}
}