package dmscreen.data.creature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import dmscreen.data.base.Ability;
import dmscreen.data.base.DamageType;
import dmscreen.data.base.DiceRoll;
import dmscreen.data.base.Size;
import dmscreen.data.base.Skill;
import dmscreen.data.creature.feature.Action;
import dmscreen.data.creature.feature.Feature;
import dmscreen.data.creature.feature.LegendaryAction;

public class Creature {

	/**
	 * Indexed by {@link #challengeRating} + 5
	 */
	public static final int[] XP = {0, 10, 25, 50, 100, 0, 200, 450, 700, 1100, 1800, 2300, 2900, 3900, 5000, 5900, 7200, 8400, 10000, 11500, 13000, 15000, 18000, 20000, 22000, 25000, 33000, 41000, 50000, 62000, 75000, 90000, 105000, 120000, 135000, 155000};

	public String name;
	public String shortName;
	public Size size;
	public CreatureType type;
	public String subtype;
	public Alignment alignment;
	public int ac;
	public String armorNote;
	public DiceRoll hitDice;
	public int speed;
	public Map<MovementType, Integer> speeds;
	public Map<Ability, Integer> abilityScores;
	public Map<Ability, Integer> savingThrows;
	public Map<Skill, Integer> skills;
	public Map<String, Set<DamageType>> vulnerabilities;
	public Map<String, Set<DamageType>> resistances;
	public Map<String, Set<DamageType>> immunities;
	public Set<Condition> conditionImmunities;
	public Map<VisionType, Integer> senses;
	public Set<String> languages;

	/**
	 * Values of -1, -2, -3, -4, and -5 correspond to 1/2, 1/4, 1/8, 0 (10XP) and 0 (0XP),
	 * respectively. 0 also corresponds to 0 (0 XP)
	 * @see {@link Creature#XP}
	 */
	public int challengeRating;

	public List<? extends Feature> features;
	public List<? extends Action> actions;
	public List<? extends Action> reactions;
	public List<? extends LegendaryAction> legendaryActions;

	public Creature() {
		this("", Size.MEDIUM, CreatureType.MONSTROSITY, null, Alignment.NEUTRAL, 10, null, new DiceRoll(1, 10), 30, new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new TreeSet<>(), new HashMap<>(), new TreeSet<>(), 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
	}

	public Creature(final String name, final Size size, final CreatureType type, final String subtype, final Alignment alignment, final int ac, final String armorNote, final DiceRoll hitDice, final int speed, final Map<MovementType, Integer> speeds, final Map<Ability, Integer> abilityScores, final Map<Ability, Integer> savingThrows, final Map<Skill, Integer> skills, final Map<String, Set<DamageType>> vulnerabilities, final Map<String, Set<DamageType>> resistances,
			final Map<String, Set<DamageType>> immunities, final Set<Condition> conditionImmunities, final Map<VisionType, Integer> senses, final Set<String> languages, final int challengeRating, final List<? extends Feature> features, final List<? extends Action> actions, final List<? extends Action> reactions, final List<? extends LegendaryAction> legendaryActions) {
		this.name = name;
		shortName = name;
		this.size = size;
		this.type = type;
		this.subtype = subtype;
		this.alignment = alignment;
		this.ac = ac;
		this.armorNote = armorNote;
		this.hitDice = hitDice;
		this.speed = speed;
		this.speeds = speeds;
		this.abilityScores = abilityScores;
		for (final Ability a : Ability.values()) {
			if (!this.abilityScores.containsKey(a)) this.abilityScores.put(a, 10);
		}
		this.savingThrows = savingThrows;
		this.skills = skills;
		this.vulnerabilities = vulnerabilities;
		if (this.vulnerabilities.isEmpty()) this.vulnerabilities.put(null, new TreeSet<>());
		this.resistances = resistances;
		if (this.resistances.isEmpty()) this.resistances.put(null, new TreeSet<>());
		this.immunities = immunities;
		if (this.immunities.isEmpty()) this.immunities.put(null, new TreeSet<>());
		this.conditionImmunities = conditionImmunities;
		this.senses = senses;
		this.languages = languages;
		this.challengeRating = challengeRating;
		this.features = features;
		this.actions = actions;
		this.reactions = reactions;
		this.legendaryActions = legendaryActions;
	}

}
