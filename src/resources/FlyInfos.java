package resources;

import libraries.Vector2;

public class FlyInfos 
{
	public static Vector2 FLY_SIZE = RoomInfos.TILE_SIZE.scalarMultiplication(0.3);
	public static double FLY_SPEED = HeroInfos.ISAAC_SPEED/8;
	
	public static int FLY_LIFE = 3;
	public static int FLY_DEGAT_PROJ = 1;
	public static int FLY_DEGATCC = 1;
}
