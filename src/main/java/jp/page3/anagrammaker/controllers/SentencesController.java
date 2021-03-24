package jp.page3.anagrammaker.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.uncommons.maths.combinatorics.PermutationGenerator;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

import jp.page3.anagrammaker.models.SentenceRequest;
import jp.page3.anagrammaker.models.SentenceResponse;

@RestController
@Validated
public class SentencesController {

	@CrossOrigin(origins = "https://anagram-maker.netlify.app")
	@PostMapping("/sentences")
	public ResponseEntity<List<List<SentenceResponse>>> post(@Valid @RequestBody SentenceRequest r) {

		Tokenizer tokenizer = new Tokenizer();
		List<String> result = this.permutation(r.getS());
		String[] include = r.getInc();
		List<List<Token>> tmp = new ArrayList<>();
		List<List<SentenceResponse>> response = new ArrayList<>();
		boolean f = true;

		for (String sentence : result) {
			List<Token> tokens = tokenizer.tokenize(sentence);
			if (tokens.stream().allMatch(t -> t.isKnown())) {
				if (include != null && include.length > 0) {
					for (String i : include) {
						if (i.length() == 0) {
							continue;
						}
						if (!tokens.stream().anyMatch(t -> t.getSurface().equals(i))) {
							f = false;
							break;
						}
					}
				}
				if (f) {
					tmp.add(tokens);
				}
				f = true;
			}
		}

		tmp.stream().sorted((c, n) -> c.size() - n.size()).forEach(t -> {
			if (response.size() < 300) {
				List<SentenceResponse> lsr = new ArrayList<>();
				t.forEach(tt -> {
					lsr.add(SentenceResponse.tokenToSr(tt));
				});
				response.add(lsr);
			}
		});

		return ResponseEntity.ok(response);
	}

	private List<String> permutation(String sentence) {
		List<String> result = new ArrayList<>();
		PermutationGenerator<String> gen = new PermutationGenerator<>(sentence.split(""));
		while (gen.hasMore()) {
			String[] a = gen.nextPermutationAsArray();
			result.add(String.join("", a));
		}
		return result = new ArrayList<String>(new HashSet<>(result));
	}
}
