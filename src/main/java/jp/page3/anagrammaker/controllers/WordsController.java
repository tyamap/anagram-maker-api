package jp.page3.anagrammaker.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.uncommons.maths.combinatorics.CombinationGenerator;
import org.uncommons.maths.combinatorics.PermutationGenerator;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

@RestController
@RequestMapping("word")
public class WordsController {

	@CrossOrigin
	@GetMapping
	public List<Token> get(
			@RequestParam("w") String word,
			@RequestParam("mn") Integer max,
			@RequestParam("t") String target,
			@RequestParam("vob") Integer pvob,
			@RequestParam("ajob") Integer pajob,
			@RequestParam("avob") Integer pavob) {
		Tokenizer tokenizer = new Tokenizer();
		List<String> result = this.permutation(word, max);
		List<Token> response = new ArrayList<>();

		List<String> targets = Arrays.asList(target.split(" "));
		System.out.println(targets);
		boolean vob = pvob == 1;
		boolean ajob = pajob == 1;
		boolean avob = pavob == 1;

		for (String w : result) {
			List<Token> tokens = tokenizer.tokenize(w);
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
						if (vob) {
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
						if (ajob) {
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
						if (avob) {
							if (token.getBaseForm().equals(token.getSurface())) {
								response.add(token);
							}
						} else {
							response.add(token);
						}
					}
					break;
				}
			}
		}
		return response;
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