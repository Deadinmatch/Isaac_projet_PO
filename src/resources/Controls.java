package resources;

import libraries.Keybinding;

public class Controls
{
	public static int goUp = Keybinding.keycodeOf('z');
	public static int goDown = Keybinding.keycodeOf('s');
	public static int goRight = Keybinding.keycodeOf('d');
	public static int goLeft = Keybinding.keycodeOf('q');
	
	public static int goUpLeft = goUp + goLeft;
	public static int goUpRight = goUp + goRight;
	public static int goDownLeft = goDown + goLeft;
	public static int goDownRight = goDown + goRight;
	
	public static int shootUp = Keybinding.keycodeOf(Keybinding.SpecialKeys.UP);
	public static int shootDown = Keybinding.keycodeOf(Keybinding.SpecialKeys.DOWN);
	public static int shootRight = Keybinding.keycodeOf(Keybinding.SpecialKeys.RIGHT);
	public static int shootLeft = Keybinding.keycodeOf(Keybinding.SpecialKeys.LEFT);
	
	/*
	 * TRICHE
	 */
	public static int KillAllMonster = Keybinding.keycodeOf('k');
	public static int Invincible = Keybinding.keycodeOf('i');
	public static int SpeedUp = Keybinding.keycodeOf('l');
	public static int Money = Keybinding.keycodeOf('o');
	public static int Puissance = Keybinding.keycodeOf('p');
}
