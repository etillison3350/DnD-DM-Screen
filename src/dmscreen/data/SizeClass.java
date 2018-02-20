package dmscreen.data;

public enum SizeClass {
	TINY(4),
	SMALL(6),
	MEDIUM(8),
	LARGE(10),
	HUGE(12),
	GARGANTUAN(20);

	public transient final int hitDie;

	private SizeClass(final int hitDie) {
		this.hitDie = hitDie;
	}
}