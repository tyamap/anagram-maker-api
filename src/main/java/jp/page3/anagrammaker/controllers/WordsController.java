package jp.page3.anagrammaker.controllers;


import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.uncommons.maths.combinatorics.CombinationGenerator;
import org.uncommons.maths.combinatorics.PermutationGenerator;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

@RestController
@RequestMapping("sentence")
public class WordsController {
	@GetMapping
	public List<Token> get(
			@RequestParam("w") String word,
			@RequestParam("mn") Integer max) {
				Tokenizer tokenizer = new Tokenizer();
		List<String> result = this.permutation(word, max);
		List<Token> response = new ArrayList<>();

		for (String w : result) {
			List<Token> tokens = tokenizer.tokenize(w);
			if (tokens.size() == 1 && tokens.get(0).isKnown()) {
				response.add(tokens.get(0));
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
		return result;
	}
}