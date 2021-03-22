package jp.page3.anagrammaker.models;

import com.atilika.kuromoji.ipadic.Token;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SentenceResponse {
	private String surface;
	private String partOfSpeechLevel1;

	public static SentenceResponse tokenToSr(Token token) {
		SentenceResponse sr = new SentenceResponse(token.getSurface(), token.getPartOfSpeechLevel1());
		return sr;
	}
}
