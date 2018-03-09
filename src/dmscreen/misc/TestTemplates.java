package dmscreen.misc;

import java.util.Arrays;

import dmscreen.data.creature.feature.template.AttackTemplate;
import dmscreen.data.creature.feature.template.FieldType;
import dmscreen.data.creature.feature.template.InnateSpellcastingTemplate;
import dmscreen.data.creature.feature.template.SpellcastingTemplate;
import dmscreen.data.creature.feature.template.TemplateField;

public class TestTemplates {

	private TestTemplates() {}

	public static final InnateSpellcastingTemplate innate = new InnateSpellcastingTemplate();
	public static final SpellcastingTemplate spellcasting = new SpellcastingTemplate();
	public static final AttackTemplate basicAttack = new AttackTemplate("Basic Attack", Arrays.asList(new TemplateField("description", "Additional Effects", FieldType.STRING)), "${description}");
// public static final SimpleFeatureTemplate keenSenses = new SimpleFeatureTemplate("Keen Sense", Arrays.asList(new
// TemplateField(LIST, type, args)), nameFormat, noteFormat, descFormat)

}
