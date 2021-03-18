package jp.page3.anagrammaker.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.uncommons.maths.combinatorics.PermutationGenerator;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

import jp.page3.anagrammaker.models.SentenceRequest;

@RestController
//@Validated
public class SentencesController {

	//	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/sentences")
	public ResponseEntity<List<List<Token>>> post(@RequestBody SentenceRequest r) {

		Tokenizer tokenizer = new Tokenizer();
		List<String> result = this.permutation(r.getS());
		List<List<Token>> tmp = new ArrayList<>();
		List<List<Token>> response = new ArrayList<>();

		for (String sentence : result) {
			List<Token> tokens = tokenizer.tokenize(sentence);
			if (tokens.stream().allMatch(t -> t.isKnown())) {
				tmp.add(tokens);
			}
		}
		tmp.stream().sorted((c, n) -> c.size() - n.size()).forEach(t -> {
			if (response.size() < 500) {
				response.add(t);
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
