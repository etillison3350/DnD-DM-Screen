package dmscreen.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import dmscreen.data.Data;
import dmscreen.data.spell.Bullet;
import dmscreen.data.spell.Spell;
import dmscreen.data.spell.SpellFeature;
import dmscreen.data.spell.SpellParagraph;
import dmscreen.data.spell.SpellType;

public class SpellLoader {

	public static void main(final String[] args) {
		try {
			// System.setOut(new PrintStream(Paths.get("out.txt").toFile()));

			final URL spellList = new URL("https://www.dnd-spells.com/spells");
			final BufferedReader in = new BufferedReader(new InputStreamReader(spellList.openStream(), Charset.forName("UTF-8")));

			String line;
			int state = 0;
			final StringBuffer output = new StringBuffer();
			while ((line = in.readLine()) != null) {
				if (line.contains("<table id=\"example\"")) state = 1;
				if (state == 1 && line.contains("<tbody>")) state = 2;
				if (state == 2) {
					output.append(line);
					if (line.contains("</tbody>")) break;
				}
			}
			in.close();

			final Set<String> spells = new HashSet<>();
			final Matcher m = Pattern.compile("td\\>\\s*\\<a href=\"(.+?)\"").matcher(output);
			while (m.find()) {
				spells.add(m.group(1));
			}

			final Pattern spellPattern = Pattern.compile("<h1.+?><span>(.+?)(\\(Ritual\\))?<\\/span>[\\s\\S]+?<p>(.+?)<\\/p>[\\s\\S]+?Level:\\s*<strong>\\s*(\\d|Cantrip)[\\s\\S]+?time:\\s*<strong>(.+?)<\\/strong>[\\s\\S]+?Range:\\s*<strong>(.+?)<\\/strong>[\\s\\S]+?Components:\\s*<strong>([VSM\\s,]+)(?:\\((.+?)\\))?[\\s\\S]+?Duration:\\s*<strong>(Concentration, up to)?(.+?)<\\/strong>[\\s\\S]+?<\\/p>\\s*([\\s\\S]+?)<hr>");
			final Pattern paragraphPattern = Pattern.compile("<(p|h\\d|div).*?>([\\s\\S]*?)<\\/\\1>");
			final Pattern htmlTags = Pattern.compile("<(.+?)>([\\s\\S]*?)<\\/\\1>");
			final Pattern htmlTagOpen = Pattern.compile(".*?<([^\\/]+?)>");
			Files.write(Paths.get("spells.json"), Data.GSON.toJson(spells.stream().map(s -> {
				try {
					final URL spell = new URL(s);
					final BufferedReader r = new BufferedReader(new InputStreamReader(spell.openStream(), Charset.forName("UTF-8")));
					String l;
					boolean i = false;
					final StringBuffer out = new StringBuffer();
					while ((l = r.readLine()) != null) {
						if (!i && l.contains("<h1 class=\"classic-title\">")) {
							out.append(l.replace("&nbsp;", " ").replace("&rsquo;", "\u2019").replace("&#8217;", "\u2019").replace("&#039;", "'"));
							i = true;
						} else if (i) {
							out.append(l.replace("&nbsp;", " ").replace("&rsquo;", "\u2019").replace("&#8217;", "\u2019").replace("&#039;", "'"));
							if (l.contains("Create and save")) break;
						}
					}

					// System.out.println(out);

					// if (Math.sqrt(2) > 1) return;

					final Matcher matcher = spellPattern.matcher(out);
					if (matcher.find()) {
						final Spell sp = new Spell();
						sp.name = matcher.group(1).trim();
						sp.ritual = matcher.group(2) != null;
						sp.type = SpellType.valueOf(matcher.group(3).toUpperCase());
						try {
							sp.level = Integer.parseInt(matcher.group(4));
						} catch (final NumberFormatException e) {
							sp.level = 0;
						}
						sp.castingTime = matcher.group(5);
						try {
							sp.range = Integer.parseInt(matcher.group(6).replaceAll("\\D+", ""));
						} catch (final NumberFormatException e) {
							sp.range = 0;
						}
						sp.verbal = matcher.group(7).contains("V");
						sp.somatic = matcher.group(7).contains("S");
						sp.materialComponents = matcher.group(8);
						sp.concentration = matcher.group(9) != null;
						sp.duration = matcher.group(10).trim();

						final Matcher segments = paragraphPattern.matcher(matcher.group(11).replaceAll("<\\/b>", "<\\/b><br>"));
						final List<String> paragraphs = new ArrayList<>();
						while (segments.find()) {
							String seg = segments.group(2);
							if (seg.contains("div")) {
								final Matcher div = paragraphPattern.matcher(seg);
								if (div.find()) seg = div.group(2);
							}
							for (final String str : seg.split("<br(?:\\s*\\/)?>")) {
								final String trim = str.trim();
								if (!trim.isEmpty()) paragraphs.add(trim);
							}
						}

						System.out.println(paragraphs);

						if (paragraphs.get(0).matches("\\(.+?\\)")) {
							sp.materialComponents = paragraphs.remove(0).replaceAll("[\\(\\)]", "");
						} else if (sp.castingTime.equalsIgnoreCase("Special") && paragraphs.get(0).matches("\\d+.+?")) {
							sp.castingTime = paragraphs.remove(0);
						}

						final List<SpellParagraph> spellParagraphs = new ArrayList<>();
						while (!paragraphs.isEmpty()) {
							final String str = paragraphs.remove(0);
							if (str.startsWith("\u2022") || str.startsWith("*") || str.startsWith("-")) {
								spellParagraphs.add(new Bullet(str.substring(2).trim()));
							} else {
								final Matcher html = htmlTags.matcher(str);
								if (html.find()) {
									String title = html.group(2);
									if (title.equalsIgnoreCase("at higher level")) title = "At Higher Levels";
									spellParagraphs.add(new SpellFeature(title, paragraphs.remove(0)));
								} else {
									final Matcher openTag = htmlTagOpen.matcher(str);
									if (openTag.find()) {
										String move = "";
										int sub = 0;
										while (true) {
											System.out.println("\t" + paragraphs);
											System.out.println("\t\t" + paragraphs.get(0));
											final String regex = String.format("^(.*?<\\/%s>)[\\s\\S]*?$", openTag.group(1));
											System.out.println("\t\t" + regex);
											if (paragraphs.get(0).matches(regex)) {
												sub = move.length();
												move += paragraphs.get(0).replaceFirst(regex, "$1");
												sub = move.length() - sub;
												break;
											}
											move += paragraphs.remove(0);
										}
										System.out.println("Move: " + move + "\n");

										paragraphs.add(0, str + move);
										paragraphs.set(1, paragraphs.get(1).substring(sub));
									} else {
										spellParagraphs.add(new SpellParagraph(str));
									}
								}
							}
						}
						sp.description = spellParagraphs;
						return sp;
					}
				} catch (final IOException e) {
					e.printStackTrace();
				}
				return null;
			}).collect(Collectors.toCollection(HashSet::new))).getBytes());
		} catch (

		final IOException e) {
			e.printStackTrace();
		}
	}

}
