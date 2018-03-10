package dmscreen.misc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import dmscreen.data.Data;
import dmscreen.data.creature.feature.template.AttackTemplate;
import dmscreen.data.creature.feature.template.FeatureTemplate;
import dmscreen.data.creature.feature.template.FieldType;
import dmscreen.data.creature.feature.template.InnateSpellcastingTemplate;
import dmscreen.data.creature.feature.template.SpellcastingTemplate;
import dmscreen.data.creature.feature.template.Template;
import dmscreen.data.creature.feature.template.TemplateField;

public class TestTemplates {

	private TestTemplates() {}

	public static final InnateSpellcastingTemplate innate = new InnateSpellcastingTemplate();
	public static final SpellcastingTemplate spellcasting = new SpellcastingTemplate();
	public static final AttackTemplate basicAttack = new AttackTemplate("Basic Attack", Arrays.asList(new TemplateField("description", "Additional Effects", FieldType.STRING)), "${description}");
	public static final FeatureTemplate keenSenses = new FeatureTemplate("Keen Sense", Arrays.asList(new TemplateField("shortName", "General Creature Name", FieldType.STRING), new TemplateField("senses", FieldType.LIST, FieldType.STRING, "Hearing", "Sight", "Smell")), "Keen ${senses.length == 1 ? senses[0] : senses.length == 3 || senses.length == 0 ? 'Senses' : senses[0] + ' and ' + senses[1]}", "", "${Util.sentenceCase(shortName) + ' has advantage on Wisdom (Perception) checks that rely on ' + (senses.length == 0 ? 'senses' : Util.andJoin('or', senses).toLowerCase()) + '.'}");

	public static void main(final String... args) {
		try {
			Files.write(Paths.get("resources/source/templates.json"), Data.GSON.toJson(Stream.of(innate, spellcasting, basicAttack, keenSenses).toArray(size -> new Template[size])).getBytes());
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
