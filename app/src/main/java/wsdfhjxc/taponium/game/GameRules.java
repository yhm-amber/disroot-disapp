package wsdfhjxc.taponium.game;

public class GameRules {
    // how often does each content type occur, chances sum should be equal to 1.0
    public static final double HAMSTER_CONTENT_OCCURRENCE_CHANCE = 0.4;
    public static final double BUNNY_CONTENT_OCCURRENCE_CHANCE = 0.1;
    public static final double EMPTY_CONTENT_OCCURRENCE_CHANCE = 0.5;

    // how long does each content type stay visible
    public static final double HAMSTER_CONTENT_MIN_DURATION = 3.0;
    public static final double HAMSTER_CONTENT_MAX_DURATION = 4.5;

    public static final double BUNNY_CONTENT_MIN_DURATION = 2.0;
    public static final double BUNNY_CONTENT_MAX_DURATION = 6.0;

    public static final double EMPTY_CONTENT_MIN_DURATION = 2.0;
    public static final double EMPTY_CONTENT_MAX_DURATION = 4.0;

    // how does the game difficulty change over time
    public static final double CONTENT_DURATION_CHANGE_INTERVAL = 10.0;
    public static final double CONTENT_DURATION_SCALING_FACTOR = 0.95;

    // how does the score change during game play
    public static final int HAMSTER_CONTENT_TAPPED_POINTS = 10;
    public static final int HAMSTER_CONTENT_MISSED_POINTS = -100;
    public static final int BUNNY_CONTENT_TAPPED_POINTS = -1000;

    // how does the content duration change after tapping
    public static final double TAPPED_CONTENT_DURATION_SCALING_FACTOR = 0.2;
}