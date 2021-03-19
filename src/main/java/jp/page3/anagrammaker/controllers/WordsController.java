package jp.page3.anagrammaker.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.uncommons.maths.combinatorics.CombinationGenerator;
import org.uncommons.maths.combinatorics.PermutationGenerator;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

import jp.page3.anagrammaker.models.WordRequest;

@RestController
@Validated
public class WordsController {

	@CrossOrigin(origins = "https://anagram-maker.netlify.app")
	@PostMapping("/words")
	public ResponseEntity<List<Token>> post(@Valid @RequestBody WordRequest r) {

		Tokenizer tokenizer = new Tokenizer();
		List<String> result = this.permutation(r.getW(), r.getMn());
		List<Token> response = new ArrayList<>();
		List<String> targets = Arrays.asList(r.getT());
		boolean vb = r.getVob() == 1;
		boolean ajb = r.getAjob() == 1;
		boolean avb = r.getAvob() == 1;

		for (String word : result) {
			List<Token> tokens = tokenizer.tokenize(word);
			if (tokens.size() == 1 && tokens.get(0).isKnown()) {
				Token token = tokens.get(0);
				switch (token.getPartOfSpeechLevel1()) {
				case "名詞":
					if (targets.contains("n")) {
						response.add(token);
					}
					break;
				case "動詞":
					if (targets.contains("v")) {
						if (vb) {
							if (token.getBaseForm().equals(token.getSurface())) {
								response.add(token);
							}
						} else {
							response.add(token);
						}
					}
					break;
				case "形容詞":
					if (targets.contains("aj")) {
						if (ajb) {
							if (token.getBaseForm().equals(token.getSurface())) {
								response.add(token);
							}
						} else {
							response.add(token);
						}
					}
					break;
				case "副詞":
					if (targets.contains("av")) {
						if (avb) {
							if (token.getBaseForm().equals(token.getSurface())) {
								response.add(token);
							}
						} else {
							response.add(token);
						}
					}
					break;
				default:
					if (targets.contains("o")) {
						response.add(token);
					}
					break;
				}
			}
		}
		return ResponseEntity.ok(response);
	}

	private List<String> permutation(String word, Integer num) {
		CombinationGenerator<String> com = new CombinationGenerator<>(word.split(""), num);
		List<String> result = new ArrayList<>();
		while (com.hasMore()) {
			String[] co = com.nextCombinationAsArray();
			PermutationGenerator<String> gen = new PermutationGenerator<>(co);
			while (gen.hasMore()) {
				String[] a = gen.nextPermutationAsArray();
				result.add(String.join("", a));
			}
		}
		return result = new ArrayList<String>(new HashSet<>(result));
	}
}