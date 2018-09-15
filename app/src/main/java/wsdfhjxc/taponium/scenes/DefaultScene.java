package wsdfhjxc.taponium.scenes;

import android.graphics.*;
import android.view.*;

import wsdfhjxc.taponium.engine.*;

public class DefaultScene extends Scene {
    private final RenderHandler renderHandler;

    private Bitmap backgroundBitmap;
    private Rect backgroundBitmapRect;
    private Flex backgroundBitmapFlex;

    private int fpsCounterFontColor;
    private int fpsCounterFontSize;

    public DefaultScene(SceneKeeper sceneKeeper, ResourceKeeper resourceKeeper,
                        RenderHandler renderHandler, FlexConfig flexConfig) {
        super(sceneKeeper, resourceKeeper, flexConfig, 0, 0);
        this.renderHandler = renderHandler;
    }

    @Override
    public void load() {
        // load all game required resources here
        resourceKeeper.loadBitmap("background");
        resourceKeeper.loadBitmap("title_text");
        resourceKeeper.loadBitmap("menu_panel");
        resourceKeeper.loadBitmap("board_panel");
        resourceKeeper.loadBitmap("hamster");
        resourceKeeper.loadBitmap("bunny");
        resourceKeeper.loadBitmap("dead_hamster");
        resourceKeeper.loadBitmap("dead_bunny");
        resourceKeeper.loadBitmap("over_panel");
        resourceKeeper.loadBitmap("current_score_text");
        resourceKeeper.loadBitmap("game_over_text");
        resourceKeeper.loadBitmap("back_sign");
        resourceKeeper.loadTypeface("IndieFlower", "ttf");

        // get background image
        backgroundBitmap = resourceKeeper.getBitmap("background");
        backgroundBitmapRect = new Rect(0, 0, backgroundBitmap.getWidth(),
                                              backgroundBitmap.getHeight());

        backgroundBitmapFlex = new Flex(new PointF(0f, 0f), false,
                                        new PointF(1f, 1f), false,
                                        new Point(), flexConfig);

        fpsCounterFontColor = Color.BLACK;
        fpsCounterFontSize = 14;

        // add MainMenuScene scene after loading this
        sceneKeeper.addScene(new MainMenuScene(sceneKeeper, resourceKeeper, flexConfig));
    }

    @Override
    public void unload() {}

    @Override
    public void backPressed() {}

    @Override
    public void handleInput(MotionEvent motionEvent) {}

    @Override
    public void handleUpdate(double deltaTime) {}

    @Override
    public void handleRender(Canvas canvas, Paint paint, double alpha) {
        canvas.drawBitmap(backgroundBitmap, backgroundBitmapRect,
                          backgroundBitmapFlex.getRect(), paint);

        /*
        // draw an FPS counter in the top left corner
        paint.setColor(fpsCounterFontColor);
        paint.setTextSize(fpsCounterFontSize);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(null); // disable any previously set custom font
        canvas.drawText("FPS: " + Math.round(renderHandler.getFPS()), 2, fpsCounterFontSize, paint);
        */
    }
}