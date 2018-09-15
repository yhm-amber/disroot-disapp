package wsdfhjxc.taponium.game;

public class Board {
    private final int width = 3;
    private final Slot[][] slots = new Slot[width][width];
    private final SlotContent hamsterContent, bunnyContent, emptyContent;
    private final SlotContentDistributor slotContentDistributor;

    private final ScoreCounter scoreCounter;

    private double currentChangeDuration = 0.0;

    public Board(ScoreCounter scoreCounter) {
        this.scoreCounter = scoreCounter;

        hamsterContent = new SlotContent(SlotContentType.HAMSTER,
                                         GameRules.HAMSTER_CONTENT_OCCURRENCE_CHANCE,
                                         GameRules.HAMSTER_CONTENT_MIN_DURATION,
                                         GameRules.HAMSTER_CONTENT_MAX_DURATION);

        bunnyContent = new SlotContent(SlotContentType.BUNNY,
                                       GameRules.BUNNY_CONTENT_OCCURRENCE_CHANCE,
                                       GameRules.BUNNY_CONTENT_MIN_DURATION,
                                       GameRules.BUNNY_CONTENT_MAX_DURATION);

        emptyContent = new SlotContent(SlotContentType.EMPTY,
                                       GameRules.EMPTY_CONTENT_OCCURRENCE_CHANCE,
                                       GameRules.EMPTY_CONTENT_MIN_DURATION,
                                       GameRules.EMPTY_CONTENT_MAX_DURATION);

        slotContentDistributor = new SlotContentDistributor(hamsterContent, bunnyContent, emptyContent);

        initializeSlots();
    }

    private void initializeSlots() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                slots[j][i] = new Slot(this);
            }
        }
    }

    private void updateSlots(double deltaTime) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                Slot slot = slots[j][i];
                slot.update(deltaTime);
            }
        }
    }

    private void fillSlots() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                Slot slot = slots[j][i];
                if (slot.isFree()) {
                    slotContentDistributor.distributeContent(slot);
                }
            }
        }
    }

    public void update(double deltaTime) {
        currentChangeDuration += deltaTime;
        if (currentChangeDuration >= GameRules.CONTENT_DURATION_CHANGE_INTERVAL) {
            currentChangeDuration = 0.0;

            double hamsterMin = hamsterContent.getMinDuration();
            double hamsterMax = hamsterContent.getMaxDuration();
            hamsterContent.setMinDuration(hamsterMin * GameRules.CONTENT_DURATION_SCALING_FACTOR);
            hamsterContent.setMaxDuration(hamsterMax * GameRules.CONTENT_DURATION_SCALING_FACTOR);

            double bunnyMin = bunnyContent.getMinDuration();
            double bunnyMax = bunnyContent.getMaxDuration();
            bunnyContent.setMinDuration(bunnyMin * GameRules.CONTENT_DURATION_SCALING_FACTOR);
            bunnyContent.setMaxDuration(bunnyMax * GameRules.CONTENT_DURATION_SCALING_FACTOR);

            double emptyMin = emptyContent.getMinDuration();
            double emptyMax = emptyContent.getMaxDuration();
            emptyContent.setMinDuration(emptyMin * GameRules.CONTENT_DURATION_SCALING_FACTOR);
            emptyContent.setMaxDuration(emptyMax * GameRules.CONTENT_DURATION_SCALING_FACTOR);
        }

        updateSlots(deltaTime);
        fillSlots();
    }

    public Slot getSlot(int x, int y) {
        return slots[x][y];
    }

    public int getWidth() {
        return width;
    }

    public void slotTotalDurationPassed(Slot slot) {
        if (slot.getContentType() == SlotContentType.HAMSTER) {
            scoreCounter.add(GameRules.HAMSTER_CONTENT_MISSED_POINTS);
        }
    }
}