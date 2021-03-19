package jp.page3.anagrammaker.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WordRequest {
	@NotBlank
	@Size(max = 15)
	private String w;

	@NotNull
	@Min(2)
	@Max(7)
	private Integer mn;

	@NotNull
	private String[] t;

	@NotNull
	@Min(0)
	@Max(1)
	private Integer vob;

	@NotNull
	@Min(0)
	@Max(1)
	private Integer ajob;

	@NotNull
	@Min(0)
	@Max(1)
	private Integer avob;
}
