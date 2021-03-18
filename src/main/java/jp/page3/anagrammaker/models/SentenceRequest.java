package jp.page3.anagrammaker.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SentenceRequest {
	@NotBlank
	@Size(max = 10)
	private String s;
}
