package wsdfhjxc.taponium.scenes;

import android.graphics.*;
import android.view.*;

import wsdfhjxc.taponium.engine.*;
import wsdfhjxc.taponium.game.*;

public class GameScene extends Scene {
    private ScoreCounter scoreCounter;
    private ScoreCounterRenderer scoreCounterRenderer;

    private Bitmap currentScoreTextBitmap;
    private Rect currentScoreTextRect;
    private Flex currentScoreTextFlex;

    private Board board;
    private BoardRenderer boardRenderer;

    private Flex boardAreaFlex;
    private Flex boardSlotFlex;
    private Flex boardSlotSpacerFlex;

    private Bitmap backSignBitmap;
    private Rect backSignRect;
    private Flex backSignFlex;

    public GameScene(SceneKeeper sceneKeeper, ResourceKeeper resourceKeeper, FlexConfig flexConfig) {
        super(sceneKeeper, resourceKeeper, flexConfig, 2, 1);
    }

    @Override
    public void load() {
        scoreCounter = new ScoreCounter();
        scoreCounterRenderer = new ScoreCounterRenderer(scoreCounter, resourceKeeper, flexConfig);

        board = new Board(scoreCounter);

        boardAreaFlex = new Flex(new PointF(0.5f, 1f), false,
                                 new PointF(814f, 714f), true,
                                 new Point(-814 / 2, -993), flexConfig);

        boardSlotFlex = new Flex(new PointF(0f, 0f), true,
                                 new PointF(183f, 156f), true,
                                 new Point(), flexConfig);

        boardSlotSpacerFlex = new Flex(new PointF(0f, 0f), true,
                                       new PointF(135f, 125f), true,
                                       new Point(), flexConfig);

        boardRenderer = new BoardRenderer(board, resourceKeeper, flexConfig,
                                          boardAreaFlex, boardSlotFlex, boardSlotSpacerFlex);

        currentScoreTextBitmap = resourceKeeper.getBitmap("current_score_text");
        currentScoreTextRect = new Rect(0, 0, currentScoreTextBitmap.getWidth(),
                                              currentScoreTextBitmap.getHeight());

        currentScoreTextFlex = new Flex(new PointF(0.5f, 0.25f), false,
                                        new PointF(currentScoreTextBitmap.getWidth(),
                                                   currentScoreTextBitmap.getHeight()), true,
                                        new Point(-currentScoreTextBitmap.getWidth() / 2,
                                                  -currentScoreTextBitmap.getHeight() - 136),
                                        flexConfig);

        backSignBitmap = resourceKeeper.getBitmap("back_sign");
        backSignRect = new Rect(0, 0, backSignBitmap.getWidth(), backSignBitmap.getHeight());

        backSignFlex = new Flex(new PointF(1f, 0f), false,
                                new PointF(backSignBitmap.getWidth(), backSignBitmap.getHeight()), true,
                                new Point(-backSignBitmap.getWidth() - 20, 20),
                                flexConfig);
    }

    @Override
    public void unload() {}

    @Override
    public void backPressed() {
        sceneKeeper.removeScene(this);
        sceneKeeper.addScene(new MainMenuScene(sceneKeeper, resourceKeeper, flexConfig));
    }

    private void handleBoardAreaInput(MotionEvent motionEvent) {
        int relativeX = (int)motionEvent.getX() - boardAreaFlex.getPosition().x;
        int relativeY = (int)motionEvent.getY() - boardAreaFlex.getPosition().y;

        int slotX = relativeX / (boardSlotFlex.getSize().x + boardSlotSpacerFlex.getSize().x);

        int slotY = relativeY / (boardSlotFlex.getSize().y + boardSlotSpacerFlex.getSize().y);

        Slot slot = board.getSlot(slotX, slotY);
        if (slot.getContentType() == SlotContentType.HAMSTER) {
            slot.setContentType(SlotContentType.DEAD_HAMSTER);
            slot.scaleDuration(GameRules.TAPPED_CONTENT_DURATION_SCALING_FACTOR);
            scoreCounter.add(GameRules.HAMSTER_CONTENT_TAPPED_POINTS);
        } else if (slot.getContentType() == SlotContentType.BUNNY) {
            slot.setContentType(SlotContentType.DEAD_BUNNY);
            slot.scaleDuration(GameRules.TAPPED_CONTENT_DURATION_SCALING_FACTOR);
            scoreCounter.add(GameRules.BUNNY_CONTENT_TAPPED_POINTS);
        }
    }

    @Override
    public void handleInput(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (boardAreaFlex.getRect().contains((int) motionEvent.getX(),
                                                 (int) motionEvent.getY())) {
                handleBoardAreaInput(motionEvent);
            } else if (backSignFlex.getRect().contains((int) motionEvent.getX(),
                                                       (int) motionEvent.getY())) {
                sceneKeeper.removeScene(this);
                sceneKeeper.addScene(new MainMenuScene(sceneKeeper, resourceKeeper, flexConfig));
            }
        }
    }

    @Override
    public void handleUpdate(double deltaTime) {
        board.update(deltaTime);

        if (scoreCounter.isNegative()) {
            sceneKeeper.removeScene(this);
            sceneKeeper.addScene(new GameOverScene(sceneKeeper, resourceKeeper, flexConfig,
                                                   scoreCounter.getMax()));
        }
    }

    @Override
    public void handleRender(Canvas canvas, Paint paint, double alpha) {
        boardRenderer.render(canvas, paint, alpha);
        scoreCounterRenderer.render(canvas, paint);

        canvas.drawBitmap(currentScoreTextBitmap, currentScoreTextRect,
                          currentScoreTextFlex.getRect(), paint);

        canvas.drawBitmap(backSignBitmap, backSignRect, backSignFlex.getRect(), paint);
    }
}