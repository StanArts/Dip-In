package platforms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import collectables.Collectable;
import helpers.GameInfo;
import helpers.GameManager;
import player.Player;

public class PlatformsController {

    private World world;

    private Array<Platform> platforms = new Array<Platform>();
    private Array<Collectable> collectables = new Array<Collectable>();

    private final float DISTANCE_BETWEEN_PLATFORMS = 250f;
    private float minX, maxX;

    private float lastPlatformPositionY;
    private float cameraY;

    private Random random = new Random();

    public PlatformsController(World world) {
        this.world = world;
        minX = GameInfo.WIDTH / 2f - 110;
        maxX = GameInfo.WIDTH / 2f + 110;
        createPlatforms();
        positionPlatforms(true);
    }

    void createPlatforms() {

        for (int i = 0; i < 2; i++) {
            platforms.add(new Platform(world, "Platform_O"));
        }

        int index = 1;

        for (int i = 0; i < 6; i++) {
            platforms.add(new Platform(world, "Platform_" + index));

            index++;

            if (index == 3) {
                index = 1;
            }
        }

        platforms.shuffle();
    }

    public void positionPlatforms(boolean firstTimeArranging) {

        while (platforms.get(0).getPlatformType() == "Platform_O") {
            platforms.shuffle();
        }

        float positionY = 0;

        if (firstTimeArranging) {
            positionY = GameInfo.HEIGHT / 2f;
        } else {
            positionY = lastPlatformPositionY;
        }

        int controlX = 0;

        for (Platform p : platforms) {

            if (p.getX() == 0 && p.getY() == 0) {

                float positionX = 0;

                if (controlX == 0) {
                    positionX = randomBetweenNumbers(maxX - 40, maxX);
                    controlX = 1;
                    p.setDrawLeft(false);
                } else if (controlX == 1) {
                    positionX = randomBetweenNumbers(minX + 40, minX);
                    controlX = 0;
                    p.setDrawLeft(true);
                }

                p.setSpritePosition(positionX, positionY);

                positionY -= DISTANCE_BETWEEN_PLATFORMS;
                lastPlatformPositionY = positionY;

                if (!firstTimeArranging && p.getPlatformType() != "Platform_O") {

                    int rand = random.nextInt(10);

                    if (rand > 5) {

                        int randomCollectable = random.nextInt(2);

                        if (randomCollectable == 0) {

                            if (GameManager.getInstance().lifeScore < 2) {

                                Collectable life = new Collectable(world, "Life");

                                life.setCollectablePosition(p.getX(),
                                        p.getY() + 40);
                                collectables.add(life);
                            }
                        } else {

                            Collectable coin = new Collectable(world, "Life");

                            coin.setCollectablePosition(p.getX(),
                                    p.getY() + 40);
                            collectables.add(coin);
                        }
                    }
                }
            }
        }
    }

    public void drawPlatforms (SpriteBatch batch) {

        for (Platform p : platforms) {
            if (p.getDrawLeft()) {
                batch.draw(p, p.getX() - p.getWidth() / 2f - 20,
                        p.getY() - p.getHeight() / 2f);
            } else {
                batch.draw(p, p.getX() - p.getWidth() / 2f + 10,
                        p.getY() - p.getHeight() / 2f);
            }
        }
    }

    public void drawCollectables(SpriteBatch batch) {

        for (Collectable c: collectables) {
            batch.draw(c, c.getX(), c.getY());
        }
    }

    public void removeCollectables() {
        for (int i = 0; i < collectables.size; i++) {
            if (collectables.get(i).getFixture().getUserData() == "Remove") {
                collectables.get(i).changeFilter();
                collectables.get(i).getTexture().dispose();
                collectables.removeIndex(i);
            }
        }
    }

    public void createAndArrangeNewPlatforms() {
        for (int i = 0; i < platforms.size; i++) {
            if ((platforms.get(i).getY() - GameInfo.HEIGHT / 2f - 10) > cameraY) {
                platforms.get(i).getTexture().dispose();
                platforms.removeIndex(i);
            }
        }

        if (platforms.size == 4) {
            createPlatforms();
            positionPlatforms(false);
        }
    }

    public void removeOffScreenCollectables() {
        for (int i = 0; i < collectables.size; i++) {
            if ((collectables.get(i).getY() - GameInfo.HEIGHT / 2f -15) > cameraY) {
                collectables.get(i).getTexture().dispose();
                collectables.removeIndex(i);
            }
        }
    }

    public void setCameraY (float cameraY) {
        this.cameraY = cameraY;
    }

    private float randomBetweenNumbers (float min, float max) {
        random.nextInt(10);
        return random.nextFloat() * (max - min) + min;
    }

    // It does position the player on the first
    // platform appearing on the screen:
    public Player positionThePlayer(Player player) {
        player = new Player(world, platforms.get(0).getX(),
                platforms.get(0).getY() + 100);
        return player;
    }
}
