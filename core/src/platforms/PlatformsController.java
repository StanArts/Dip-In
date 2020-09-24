package platforms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import helpers.GameInfo;

public class PlatformsController {

    private World world;

    private Array<Platform> platforms = new Array<Platform>();

    private final float DISTANCE_BETWEEN_PLATFORMS = 250f;

    public PlatformsController(World world) {
        this.world = world;
        createPlatforms();
        positionPlatforms();
    }

    void createPlatforms() {

        for (int i = 0; i < 2; i++) {
            platforms.add(new Platform(world, "Platform_O"));
        }

        int index = 1;

        for (int i = 0; i < 6; i++) {
            platforms.add(new Platform(world, "Platform_" + index));

            index++;

            if (index == 4) {
                index = 1;
            }
        }

        platforms.shuffle();
    }

    public void positionPlatforms() {

        while (platforms.get(0).getPlatformType() == "Platform_O") {
            platforms.shuffle();
        }

        float positionY = GameInfo.HEIGHT / 2f;
        float positionX = GameInfo.WIDTH / 2f;

        for (Platform p : platforms) {

            p.setSpritePosition(positionX, positionY);

            positionY -= DISTANCE_BETWEEN_PLATFORMS;
        }
    }

    public void drawPlatforms (SpriteBatch batch) {

        for (Platform p : platforms) {
            batch.draw(p, p.getX() - p.getWidth() / 2f,
                    p.getY() / p.getHeight() / 2f);
        }
    }
}
