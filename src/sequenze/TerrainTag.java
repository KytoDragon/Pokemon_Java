package sequenze;

import util.Logger;

import static sequenze.Direction.*;

public enum TerrainTag implements util.Enum {
	/** normal land */
	FREE(0x00),

	/** can not be entered */
	BLOCKED(0x01),
	/** can not be entered or left to the right */
	BLOCKADEDOWN(0x04),
	/** can not be entered or left to the left */
	BLOCKADERIGHT(BLOCKADEDOWN.getID() + 1),
	/** can not be entered or left to the top */
	BLOCKADEUP(BLOCKADEDOWN.getID() + 2),
	/** can not be entered or left to the bottom */
	BLOCKADELEFT(BLOCKADEDOWN.getID() + 3),
	/** can not be entered or left to the top or bottom */
	BLOCKADEUPDOWN(BLOCKADEDOWN.getID() + 4),
	/** can not be entered or left to the left or right */
	BLOCKADELEFTRIGHT(BLOCKADEDOWN.getID() + 5),

	/** wild pokemon */
	POKEMON(0x02),

	/** tree that headbutt can be used on, wild pokemon */
	TREE(0x03),

	/** hole in the floor that can be fallen into (map specifies where to) */
	HOLE(0x51),
	/** cracked tiles that will turn into a hole upon entering, can only be passed with a fast bike */
	CRACKEDFLOOR(0x50),

	/** lawn on the floor */
	GRASS(0x13),
	/** normal grass, wild pokemon */
	LOWGRASS(0x20),
	/** higher grass, biking disallowed, traversed slowly by foot, wild pokemon */
	MEDIUMGRASS(0x22),
	/** very high grass, biking and running disallowed, traversed very slowly by foot, wild pokemon */
	HIGHGRASS(0x24),
	/** collects ash on entering, turn into normal grass, wild pokemon */
	ASHGRASS(0x26),

	/** water that can be surfed across, wild pokemon */
	WATER(0x60),
	/** reflecting water that can be surfed across, wild pokemon */
	CALMWATER(0x61),
	/** water that can be crossed by whirlpool, return the player if entered otherwise */
	WHIRLPOOL(0x63),
	/** water that automatically moves the player to the right */
	WATERSTREAMDOWN(WHIRLPOOL.getID() + 1),
	/** water that automatically moves the player to the left */
	WATERSTREAMRIGHT(WHIRLPOOL.getID() + 2),
	/** water that automatically moves the player to the top */
	WATERSTREAMUP(WHIRLPOOL.getID() + 3),
	/** water that automatically moves the player to the bottom */
	WATERSTREAMLEFT(WHIRLPOOL.getID() + 4),

	/** a waterfall in a direction given by the map heights, can be climbed by cascade, internal */
	WATERFALL(0x6B),
	/** a waterfall to the right, can be climbed by cascade */
	WATERFALLDOWN(WATERFALL.getID() + 1),
	/** a waterfall to the left, can be climbed by cascade */
	WATERFALLRIGHT(WATERFALL.getID() + 2),
	/** a waterfall to the top, can be climbed by cascade */
	WATERFALLUP(WATERFALL.getID() + 3),
	/** a waterfall to the bottom, can be climbed by cascade */
	WATERFALLLEFT(WATERFALL.getID() + 4),

	/** very deep water that dive can be used on (map specifies where to), wild pokemon */
	UNDERWATERENTRY(0x62),

	/** underwater tile */
	UNDERWATER(0x70),
	/** underwater, grass with wild pokemon */
	UNDERWATERGRASS(0x71),
	/** underwater, ability to dive up (map specifies where to) */
	UNDERWATEREXIT(0x72),
	/** underwater, ability to dive down (map specifies where to) */
	UNDERWATERDEEPER(0x73),

	/** puddle of water with reflection */
	PUDDLE(0x12),
	/** shallow water creating water splashes at a persons feet */
	SPLASHES(0x10),

	/** a bike ramp pointed in all directions, internal */
	RAMP(0x5B),
	/** a bike ramp pointed to the right */
	RAMPDOWN(RAMP.getID() + 1),
	/** a bike ramp pointed to the left */
	RAMPRIGHT(RAMP.getID() + 2),
	/** a bike ramp pointed to the top */
	RAMPUP(RAMP.getID() + 3),
	/** a bike ramp pointed to the bottom */
	RAMPLEFT(RAMP.getID() + 4),

	/** slippery ice, angled based on map height */
	ICE(0x3B),
	/** ice angled to the right */
	ICEDOWN(ICE.getID() + 1),
	/** ice angled to the left */
	ICERIGHT(ICE.getID() + 2),
	/** ice angled to the top */
	ICEUP(ICE.getID() + 3),
	/** ice angled to the bottom */
	ICELEFT(ICE.getID() + 4),
	THINICE(0),
	ICECRACKED(0),

	/** footprints are left behind in */
	SAND(0x30),
	/** slowed movement with bike, footprints */
	LOWSAND(0x31),
	/** slowed movement, wild pokemon */
	DEEPSAND(0x32),

	/** reflecting ground */
	REFLECTION(0x11),

	/** events can be activated over a a gap */
	COUNTER(0xA0),
	/** accesses the computer */
	PC(0xA1),
	/** accesses the region map */
	MAP(0xA2),
	/** uses the television */
	TV(0xA3),
	/** uses the bike */
	CYCLESTAND(0xA4),
	/** prints a standard answer for bookcases */
	BOOKCASE1(0xA5),
	/** prints a standard answer for bookcases */
	BOOKCASE2(0xA6),
	/** prints a standard answer for bins */
	TRASHCAN(0xA7),
	/** prints a standard answer for shelfs in a market */
	GOODSSHELF1(0xA8),
	/** prints a standard answer for shelfs in a market */
	GOODSSHELF2(0xA9),

	/** slowed movement */
	MUD(0x2C),
	/** slowed movement, wild pokemon */
	MUDGRASS(0x2D),
	/** getting stuck upon entering, turn around to get free */
	DEEPMUD(0x2E),
	/** getting stuck upon entering, turn around to get free, wild pokemon */
	DEEPMUDGRASS(0x2F),

	/** leaves footprints behind */
	SNOW(0x34),
	/** slightly sunk, slowed movement, wild pokemon */
	LIGHTSNOW(0x35),
	/** half sunk, slowed movement, wild pokemon */
	MEDIUMSNOW(0x36),
	/** completely sunk, slowed movement, wild pokemon */
	DEEPSNOW(0x37),
	/** slightly sunk, wild pokemon, wild pokemon */
	SNOWGRASS(0x38),

	/** land over land, bridge bit determines who is on top */
	BRIDGE(0x80),
	/** movement only to bridge or free, sets bridge bit */
	BRIDGESTART(0x81),
	/** land over an obstacle */
	BRIDGEBLOCKED(0x82),
	/** land over land, wild pokemon */
	BRIDGEPOKEMON(0x83),
	/** land over sand */
	BRIDGESAND(0x84),
	/** land over snow */
	BRIDGESNOW(0x85),
	/** land over water */
	BRIDGEOVERWATER(0x86),
	/** water over land */
	BRIDGEUNDERWATER(0x87),

	/** a beam over land, internal */
	BEAM(0),

	/** a beam over land only traversable by bike going up-down */
	BEAMUPDOWN(0x88),
	/** a beam over an obstacle only traversable by bike going up-down */
	BEAMUPDOWNBLOCKED(0x81),
	/** a beam over land only traversable by bike going up-down, wild pokemon */
	BEAMUPDOWNPOKEMON(0x82),
	/** a beam over water only traversable by bike going up-down */
	BEAMUPDOWNWATER(0x83),
	/** a beam over sand only traversable by bike going up-down */
	BEAMUPDOWNSAND(0x84),

	/** a beam over land only traversable by bike going left-right */
	BEAMLEFTRIGHT(0x90),
	/** a beam over an obstacle only traversable by bike going left-right */
	BEAMLEFTRIGHTBLOCKED(0x91),
	/** a beam over land only traversable by bike going left-right, wild pokemon */
	BEAMLEFTRIGHTPOKEMON(0x92),
	/** a beam over water only traversable by bike going left-right */
	BEAMLEFTRIGHTWATER(0x93),
	/** a beam over sand, only traversable by bike going left-right */
	BEAMLEFTRIGHTSAND(0x94),

	/** a ledge in all directions, can be jumped over, internal */
	LEDGE(0x0B),
	/** a ledge to the right, can be jumped over */
	LEDGEDOWN(LEDGE.getID() + 1),
	/** a ledge to the left, can be jumped over */
	LEDGERIGHT(LEDGE.getID() + 2),
	/** a ledge to the top, can be jumped over */
	LEDGEUP(LEDGE.getID() + 3),
	/** a ledge to the bottom, can be jumped over */
	LEDGELEFT(LEDGE.getID() + 4),

	/** flings the player in the opposite to the one entered, internal */
	FLING(0x4B),
	/** flings the player down */
	FLINGDOWN(FLING.getID() + 1),
	/** flings the player to the right */
	FLINGRIGHT(FLING.getID() + 2),
	/** flings the player up */
	FLINGUP(FLING.getID() + 3),
	/** flings the player to the left */
	FLINGLEFT(FLING.getID() + 4),
	/** stops a flung player */
	FLINGSTOP(0),

	/** can be rock climbed in all directions, internal */
	CLIMB(0x53),
	/** can be rock climbed to the right */
	CLIMBDOWN(CLIMB.getID() + 1),
	/** can be rock climbed to the left */
	CLIMBRIGHT(CLIMB.getID() + 2),
	/** can be rock climbed to the top */
	CLIMBUP(CLIMB.getID() + 3),
	/** can be rock climbed to the bottom */
	CLIMBLEFT(CLIMB.getID() + 4),

	/** can be climbed by hoping with a bike */
	BIKECLIMB(0x58),

	/** slowed movement, internal */
	STAIRS(0x43),
	/** slowed movement left-right */
	STAIRSDOWN(STAIRS.getID() + 1),
	/** slowed movement up-down */
	STAIRSRIGHT(STAIRS.getID() + 2),
	/** slowed movement up-down */
	STAIRSUP(STAIRS.getID() + 3),
	/** slowed movement left-right */
	STAIRSLEFT(STAIRS.getID() + 4),

	/** flowers during spring, low grass during summer, left pile during autumn and light snow during winter, wild pokemon */
	SEASON1(0x28),
	/** puddle during spring, free during summer, mud during autumn and ice during winter */
	SEASON2(0x29);

	private final int id;

	private TerrainTag(int id) {
		this.id = id;
	}

	public static TerrainTag get(int id) {
		for (TerrainTag t : values()) {
			if (t.id == id) {
				return t;
			}
		}
		return null;
	}

	@Override
	public int getID() {
		return id;
	}

	public boolean isA(TerrainTag t) {
		switch (this) {
			case FREE:
				return false;
			case BLOCKED:
				return t == BLOCKED;
			case BLOCKADEDOWN:
			case BLOCKADERIGHT:
			case BLOCKADEUP:
			case BLOCKADELEFT:
			case BLOCKADEUPDOWN:
			case BLOCKADELEFTRIGHT:
				return t == FREE;
			case POKEMON:
				return t == POKEMON;
			case TREE:
				return t == BLOCKED || t == COUNTER;
			case HOLE:
				return false;
			case CRACKEDFLOOR:
				return false;
			case GRASS:
			case LOWGRASS:
			case MEDIUMGRASS:
			case HIGHGRASS:
			case ASHGRASS:
				return t == POKEMON;
			case WATER:
				return t == WATER || t == POKEMON;
			case CALMWATER:
				return t == WATER || t == POKEMON || t == REFLECTION;
			case WHIRLPOOL:
				return t == WATER;
			case WATERSTREAMDOWN:
			case WATERSTREAMRIGHT:
			case WATERSTREAMUP:
			case WATERSTREAMLEFT:
				return t == WATER || t == WHIRLPOOL;
			case WATERFALL:
			case WATERFALLDOWN:
			case WATERFALLRIGHT:
			case WATERFALLUP:
			case WATERFALLLEFT:
				return t == WATER || t == WATERFALL;
			case UNDERWATERENTRY:
				return t == WATER || t == POKEMON;
			case UNDERWATER:
				return t == WATER || t == UNDERWATER;
			case UNDERWATERGRASS:
				return t == WATER || t == UNDERWATER || t == POKEMON;
			case UNDERWATEREXIT:
			case UNDERWATERDEEPER:
				return t == WATER || t == UNDERWATER;
			case PUDDLE:
				return t == REFLECTION;
			case SPLASHES:
			case RAMP:
				return false;
			case RAMPDOWN:
			case RAMPRIGHT:
			case RAMPUP:
			case RAMPLEFT:
				return t == RAMP;
			case ICE:
				return false;
			case ICEDOWN:
			case ICERIGHT:
			case ICEUP:
			case ICELEFT:
				return t == ICE;
			case THINICE:
			case ICECRACKED:
			case SAND:
			case LOWSAND:
				return false;
			case DEEPSAND:
				return t == POKEMON;
			case REFLECTION:
				return t == REFLECTION;
			case COUNTER:
				return t == COUNTER;
			case PC:
			case MAP:
			case TV:
			case CYCLESTAND:
			case BOOKCASE1:
			case BOOKCASE2:
			case TRASHCAN:
			case GOODSSHELF1:
			case GOODSSHELF2:
				return t == COUNTER;
			case MUD:
				return false;
			case MUDGRASS:
				return t == POKEMON;
			case DEEPMUD:
				return t == DEEPMUD;
			case DEEPMUDGRASS:
				return t == DEEPMUD || t == POKEMON;
			case SNOW:
				return false;
			case LIGHTSNOW:
				return false;
			case MEDIUMSNOW:
				return false;
			case DEEPSNOW:
				return false;
			case SNOWGRASS:
				return t == POKEMON;
			case BRIDGE:
			case BRIDGESTART:
			case BRIDGEBLOCKED:
				return t == BRIDGE;
			case BRIDGEPOKEMON:
				return t == BRIDGE || t == POKEMON;
			case BRIDGESAND:
			case BRIDGESNOW:
				return t == BRIDGE;
			case BRIDGEOVERWATER:
			case BRIDGEUNDERWATER:
				return t == BRIDGE || t == WATER || t == POKEMON;
			case BEAM:
				return t == BRIDGE;
			case BEAMUPDOWN:
			case BEAMUPDOWNBLOCKED:
				return t == BRIDGE || t == BEAM;
			case BEAMUPDOWNPOKEMON:
				return t == BRIDGE || t == BEAM || t == POKEMON;
			case BEAMUPDOWNWATER:
				return t == BRIDGE || t == BEAM || t == POKEMON || t == WATER;
			case BEAMUPDOWNSAND:
			case BEAMLEFTRIGHT:
			case BEAMLEFTRIGHTBLOCKED:
				return t == BRIDGE || t == BEAM;
			case BEAMLEFTRIGHTPOKEMON:
				return t == BRIDGE || t == BEAM || t == POKEMON;
			case BEAMLEFTRIGHTWATER:
				return t == BRIDGE || t == BEAM || t == POKEMON || t == WATER;
			case BEAMLEFTRIGHTSAND:
				return t == BRIDGE || t == BEAM;
			case LEDGE:
				return false;
			case LEDGEDOWN:
			case LEDGERIGHT:
			case LEDGEUP:
			case LEDGELEFT:
				return t == LEDGE;
			case FLING:
			case FLINGDOWN:
			case FLINGRIGHT:
			case FLINGUP:
			case FLINGLEFT:
				return t == FLING;
			case FLINGSTOP:
				return false;
			case CLIMB:
			case CLIMBDOWN:
			case CLIMBRIGHT:
			case CLIMBUP:
			case CLIMBLEFT:
				return t == CLIMB || t == COUNTER;
			case BIKECLIMB:
				return false;
			case STAIRS:
			case STAIRSDOWN:
			case STAIRSRIGHT:
			case STAIRSUP:
			case STAIRSLEFT:
				return t == STAIRS;
			case SEASON1:
				return t == POKEMON;
			case SEASON2:
				return false;
			default:
				assert (false) : ("null");
				return false;
		}
	}

	public Direction getOrientation() {
		switch (this) {
			case BLOCKADEDOWN:
			case WATERSTREAMDOWN:
			case WATERFALLDOWN:
			case RAMPDOWN:
			case ICEDOWN:
			case LEDGEDOWN:
			case CLIMBDOWN:
			case FLINGDOWN:
				return SOUTH;
			case BLOCKADERIGHT:
			case WATERSTREAMRIGHT:
			case WATERFALLRIGHT:
			case RAMPRIGHT:
			case ICERIGHT:
			case LEDGERIGHT:
			case CLIMBRIGHT:
			case FLINGRIGHT:
				return EAST;
			case BLOCKADEUP:
			case WATERSTREAMUP:
			case WATERFALLUP:
			case RAMPUP:
			case ICEUP:
			case LEDGEUP:
			case CLIMBUP:
			case FLINGUP:
				return NORTH;
			case BLOCKADELEFT:
			case WATERSTREAMLEFT:
			case WATERFALLLEFT:
			case RAMPLEFT:
			case ICELEFT:
			case LEDGELEFT:
			case CLIMBLEFT:
			case FLINGLEFT:
				return WEST;
			case BLOCKADEUPDOWN:
			case BEAMUPDOWN:
			case BEAMUPDOWNBLOCKED:
			case BEAMUPDOWNPOKEMON:
			case BEAMUPDOWNWATER:
			case BEAMUPDOWNSAND:
				return NORTHSOUTH;
			case BLOCKADELEFTRIGHT:
			case BEAMLEFTRIGHT:
			case BEAMLEFTRIGHTBLOCKED:
			case BEAMLEFTRIGHTPOKEMON:
			case BEAMLEFTRIGHTWATER:
			case BEAMLEFTRIGHTSAND:
				return EASTWEST;
			default:
				Logger.add(Logger.GAME, "There is no orientation for the terrain tag: ", this.name());
				return null;
		}
	}

	public static int length() {
		return 0xFF + 1;
	}

}