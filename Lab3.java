

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Lab3 {

	public static void main(String[] args) throws IOException {
		HashMap<String, ArrayList<String>> followMap = getFollowing(args[0]);
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		// read first line
		String[] nonTs = br.readLine().split(" ");
		// System.out.println(followMap);
		for (int i = 0; i < nonTs.length; i++) {
			StringBuilder sb = new StringBuilder();
			sb.append("FOLLOW(").append(nonTs[i]).append(")={");
			ArrayList<String> follows = followMap.get(nonTs[i]);
			if (!follows.isEmpty()) {

				sb.append(follows.get(0));
				int j = 1;
				while (j < follows.size()) {
					sb.append(",").append(follows.get(j));
					j++;
				}
			}
			sb.append("}");
			System.out.println(sb);

		}

	}

	public static HashMap<String, ArrayList<String>> getFollowing(String filePath) throws IOException {
		HashMap<String, ArrayList<String>> firstMap = getFirsts(filePath);
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		// read first line
		String[] nonTs = br.readLine().split(" ");
		List<String> nonTAL = Arrays.asList(nonTs);

		// read txt file by lines
		String line;
		ArrayList<String> lines = new ArrayList<>();
		while ((line = br.readLine()) != null) {
			lines.add(line);
			// System.out.println(line);
		}
		br.close();
		HashMap<String, ArrayList<String>> followMap = new HashMap<>();
		// initialize each nonTerminal's follow symbol collection with an empty
		// ArrayList
		for (int i = 0; i < nonTs.length; i++) {
			followMap.put(nonTs[i], new ArrayList<String>());
		}

		for (int i = 0; i < lines.size(); i++) {
			// splits the line into symbols
			String[] symbols = lines.get(i).split(" -> | ");
			// left refers to the symbol left to the arrow
			String left = symbols[0];
			// in case of eps
			if (symbols[1].equals("->")) {
				continue;
			}

			for (int j = 1; j < symbols.length - 1; j++) {
				// if a non-terminal symbol is followed by a terminal, add this terminal to its
				// following ArrayList
				if (nonTAL.contains(symbols[j])) {
					if (!nonTAL.contains(symbols[j + 1])) {
						ArrayList<String> follows = followMap.get(symbols[j]);
						if (!follows.contains(symbols[j + 1])) {
							follows.add(symbols[j + 1]);
						}
						followMap.put(symbols[j], follows);
					} else {
						// if a non-terminal symbol is followed by another non-terminal, add the firsts
						// of following non terminal to the followed non terminal
						ArrayList<String> follows = followMap.get(symbols[j]);
						ArrayList<String> firsts = firstMap.get(symbols[j + 1]);
						for (String first : firsts) {
							// if this symbol is not included and it's not eps
							if (!follows.contains(first) && first != "eps") {
								follows.add(first);
							}
						}
						followMap.put(symbols[j], follows);
					}
				}

			}

		}
		
		//traverse the texts again, this time apply follow(X) belongs to follow(Y) rule 
		for (int i = 0; i < lines.size(); i++) {
			// splits the line into symbols
			String[] symbols = lines.get(i).split(" -> | ");
			// left refers to the symbol left to the arrow
			String left = symbols[0];
			// in case of eps
			if (symbols[1].equals("->")) {
				continue;
			}

			for (int j = 1; j < symbols.length - 1; j++) {
				// examine whether the last symbol of the line is a non terminal
				String last = symbols[symbols.length - 1];
				// if last symbol is non terminal
				if (nonTAL.contains(last)) {
					ArrayList<String> leftFollows = followMap.get(left);
					ArrayList<String> lastFollows = followMap.get(last);

					for (String leftFollow : leftFollows) {
						if (!lastFollows.contains(leftFollow)) {
							lastFollows.add(leftFollow);
						}
					}
					followMap.put(last, lastFollows);
					// in case last non terminal symbol could be eps
					String secondLast = symbols[symbols.length - 2];
					if (firstMap.get(last).contains("eps") && symbols.length > 2 && nonTAL.contains(secondLast)) {
						ArrayList<String> secondLastFollows = followMap.get(secondLast);
						for (String leftFollow : leftFollows) {
							if (!secondLastFollows.contains(leftFollow)) {
								secondLastFollows.add(leftFollow);
							}
						}
						followMap.put(secondLast, secondLastFollows);
					}

				}
			}

		}

		return followMap;
	}

	public static HashMap<String, ArrayList<String>> getFirsts(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		// read first line
		String[] nonTs = br.readLine().split(" ");

		// read txt file by lines
		String line;
		ArrayList<String> lines = new ArrayList<>();
		while ((line = br.readLine()) != null) {
			lines.add(line);
			// System.out.println(line);
		}
		br.close();
		// HashMap stores a terminal symbol and its non-terminals first symbols by a
		// ArrayList
		HashMap<String, ArrayList<String>> hm = new HashMap<>();

		// starts from last line
		for (int i = lines.size() - 1; i >= 0; i--) {
			// splits each line into two parts
			String[] splits = lines.get(i).split(" ->");
			ArrayList<String> firsts = new ArrayList<>();

			// If this terminal symbol has been read before, then get its already-have first
			// terminal symbols
			if (hm.containsKey(splits[0])) {
				firsts = hm.get(splits[0]);
			}

			// This means it is a eps symbol
			if (splits.length == 1) {
				firsts.add("eps");
				hm.put(splits[0], firsts);
			} else {

				// if not empty, parse those symbols
				String[] symbols = splits[1].split(" ");
				// get the first symbol, symbols[0] is an empty string
				String first = symbols[1];

				// Check this symbol is terminal or not, if it is terminal, look up its
				// already-have non-terminal first symbols and add them to this symbol's
				// non-terminal first symbol arraylist
				if (hm.containsKey(first)) {
					ArrayList<String> temp = hm.get(first);
					for (String string : temp) {
						firsts.add(string);
					}
				} else {
					// if this symbol is non-terminal, add it
					firsts.add(first);
				}

				// store those non-terminal first symbols
				hm.put(splits[0], firsts);
			}

		}
		return hm;
	}

}
