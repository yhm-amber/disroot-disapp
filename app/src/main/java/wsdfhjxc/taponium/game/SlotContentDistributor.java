package wsdfhjxc.taponium.game;

public class SlotContentDistributor {
    private final SlotContent hamsterContent, bunnyContent, emptyContent;
    private SlotContent mostCommonContent, lessCommonContent, leastCommonContent;

    public SlotContentDistributor(SlotContent hamsterContent, SlotContent bunnyContent, SlotContent emptyContent) {
        this.hamsterContent = hamsterContent;
        this.bunnyContent = bunnyContent;
        this.emptyContent = emptyContent;

        update();
    }

    public final void update() {
        double hamsterOccurrenceChance = hamsterContent.getOccurrenceChance();
        double bunnyOccurrenceChance = bunnyContent.getOccurrenceChance();
        double emptyOccurrenceChance = emptyContent.getOccurrenceChance();

        if (hamsterOccurrenceChance < bunnyOccurrenceChance) {
            if (hamsterOccurrenceChance < emptyOccurrenceChance) {
                leastCommonContent = hamsterContent;

                if (bunnyOccurrenceChance < emptyOccurrenceChance) {
                    lessCommonContent = bunnyContent;
                    mostCommonContent = emptyContent;
                } else {
                    lessCommonContent = emptyContent;
                    mostCommonContent = bunnyContent;
                }
            } else {
                leastCommonContent = emptyContent;
                lessCommonContent = hamsterContent;
                mostCommonContent = bunnyContent;
            }
        } else if (bunnyOccurrenceChance < emptyOccurrenceChance) {
            leastCommonContent = bunnyContent;
            lessCommonContent = hamsterContent;
            mostCommonContent = emptyContent;
        } else {
            leastCommonContent = emptyContent;
            lessCommonContent = bunnyContent;
            mostCommonContent = hamsterContent;
        }
    }

    private double randomizeContentDuration(SlotContent slotContent) {
        double minDuration = slotContent.getMinDuration();
        double maxDuration = slotContent.getMaxDuration();
        return minDuration + Math.random() * (maxDuration - minDuration);
    }

    public void distributeContent(Slot slot) {
        SlotContent contentToDistribute;

        double random = Math.random();
        if (random > leastCommonContent.getOccurrenceThreshold()) {
            contentToDistribute = leastCommonContent;
        } else if (random > lessCommonContent.getOccurrenceThreshold()) {
            contentToDistribute = lessCommonContent;
        } else {
            contentToDistribute = mostCommonContent;
        }

        slot.set(contentToDistribute.getType(), randomizeContentDuration(contentToDistribute));
    }
}