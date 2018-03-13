package dmscreen.data.base;

public enum Size {
	TINY(4),
	SMALL(6),
	MEDIUM(8),
	LARGE(10),
	HUGE(12),
	GARGANTUAN(20);

	public transient final int hitDie;

	private Size(final int hitDie) {
		this.hitDie = hitDie;
	}
}
