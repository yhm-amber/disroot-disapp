package wsdfhjxc.taponium.game;

import android.graphics.*;

import wsdfhjxc.taponium.engine.*;

public class BoardRenderer {
    private final Board board;

    private Bitmap boardPanelBitmap;
    private Rect boardPanelRect;
    private Flex boardPanelFlex;

    private Flex boardAreaFlex;
    private Flex boardSlotFlex;
    private Flex boardSlotSpacerFlex;

    // screw sprite sheets, spare images are enough for this
    private Bitmap hamsterBitmap, deadHamsterBitmap;
    private Bitmap bunnyBitmap, deadBunnyBitmap;

    private Flex hamsterFlex;
    private Flex bunnyFlex;

    private Rect srcRect = new Rect();
    private Rect dstRect = new Rect();

    public BoardRenderer(Board board, ResourceKeeper resourceKeeper, FlexConfig flexConfig,
                         Flex boardAreaFlex, Flex boardSlotFlex, Flex boardSlotSpacerFlex) {
        this.board = board;

        this.boardAreaFlex = boardAreaFlex;
        this.boardSlotFlex = boardSlotFlex;
        this.boardSlotSpacerFlex = boardSlotSpacerFlex;

        boardPanelBitmap = resourceKeeper.getBitmap("board_panel");
        boardPanelRect = new Rect(0, 0, boardPanelBitmap.getWidth(), boardPanelBitmap.getHeight());
        boardPanelFlex = new Flex(new PointF(0.5f, 1f), false,
                                  new PointF(boardPanelBitmap.getWidth(), boardPanelBitmap.getHeight()), true,
                                  new Point(-boardPanelBitmap.getWidth() / 2, -boardPanelBitmap.getHeight()),
                                  flexConfig);

        hamsterBitmap = resourceKeeper.getBitmap("hamster");
        bunnyBitmap = resourceKeeper.getBitmap("bunny");
        deadHamsterBitmap = resourceKeeper.getBitmap("dead_hamster");
        deadBunnyBitmap = resourceKeeper.getBitmap("dead_bunny");

        hamsterFlex = new Flex(new PointF(0f, 0f), true,
                            new PointF(182f, 207f), true,
                            new Point(), flexConfig);

        bunnyFlex = new Flex(new PointF(0f, 0f), true,
                             new PointF(182f, 302f), true,
                             new Point(), flexConfig);
    }

    public void render(Canvas canvas, Paint paint, double alpha) {
        canvas.drawBitmap(boardPanelBitmap, boardPanelRect, boardPanelFlex.getRect(), paint);

        int beginX = boardAreaFlex.getPosition().x;
        int beginY = boardAreaFlex.getPosition().y;
        int stepX = boardSlotFlex.getSize().x + boardSlotSpacerFlex.getSize().x;
        int stepY = boardSlotFlex.getSize().y + boardSlotSpacerFlex.getSize().y;

        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                int x = beginX + stepX * j;
                int y = beginY + stepY * i + boardSlotFlex.getSize().y;

                Slot slot = board.getSlot(j, i);

                if (slot.isFree()) {
                    continue;
                }

                Bitmap bitmap;
                Flex flex;

                switch (slot.getContentType()) {
                    case HAMSTER: {
                        bitmap = hamsterBitmap;
                        flex = hamsterFlex;
                    } break;
                    case DEAD_HAMSTER: {
                        bitmap = deadHamsterBitmap;
                        flex = hamsterFlex;
                    } break;
                    case BUNNY: {
                        bitmap = bunnyBitmap;
                        flex = bunnyFlex;
                    } break;
                    case DEAD_BUNNY: {
                        bitmap = deadBunnyBitmap;
                        flex = bunnyFlex;
                    } break;
                    default: {
                        continue;
                    }
                }

                double angled = Math.toRadians(slot.getInterpolatedDurationRatio(alpha) * 180.0);
                int offsetR = (int) (Math.sin(angled) * bitmap.getHeight());
                int offset = (int) (Math.sin(angled) * flex.getSize().y);

                srcRect.set(0, 0, bitmap.getWidth(), offsetR);
                dstRect.set(x, y - flex.getSize().y + (flex.getSize().y - offset),
                            x + flex.getSize().x, y);

                canvas.drawBitmap(bitmap, srcRect, dstRect, paint);
            }
        }
    }
}