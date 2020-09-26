package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.stangvel.dipin.GameMain;

import java.util.Random;

import helpers.GameInfo;
import platforms.PlatformsController;
import player.Player;

public class Gameplay implements Screen {

    private GameMain game;

    Color color;
    Color endColor;
    Color activeColor;
    ColorAction actionStE = new ColorAction();
    ColorAction actionEtS = new ColorAction();
    SequenceAction sequenceAction;
    ShapeRenderer testRenderer;
    RepeatAction repeatAction = new RepeatAction();

    private Actor actionManager = new Actor();

    private OrthographicCamera mainCamera;
    private Viewport gameViewport;

    private OrthographicCamera box2DCamera;
    private Box2DDebugRenderer debugRenderer;

    private World world;

    private PlatformsController platformsController;
    private Player player;

    Color[] colors = {Color.YELLOW, Color.GREEN, Color.BLUE};

    Random generator = new Random();

    public Gameplay (GameMain game) {
        this.game = game;

        testRenderer = new ShapeRenderer();
        color = new Color(Color.RED);
        endColor = new Color(randomColor());
        activeColor = new Color(Color.RED);

        actionStE.setColor(activeColor);
        actionStE.setDuration(5);
        actionStE.setEndColor(endColor);

        actionEtS.setColor(activeColor);
        actionEtS.setDuration(5);
        actionEtS.setEndColor(color);

        sequenceAction = new SequenceAction(actionStE, actionEtS);
        repeatAction.setAction(sequenceAction);
        repeatAction.setCount(RepeatAction.FOREVER);
        actionManager.addAction(repeatAction);

        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);

        box2DCamera = new OrthographicCamera();
        box2DCamera.setToOrtho(false, GameInfo.WIDTH / GameInfo.PPM,
                GameInfo.HEIGHT / GameInfo.PPM);
        box2DCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        debugRenderer = new Box2DDebugRenderer();

        world = new World(new Vector2(0, -9.8f), true);

        platformsController = new PlatformsController(world);

        player = platformsController.positionThePlayer(player);
    }

    @Override
    public void show() {

    }

    void handleInput (float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.movePlayer(-2);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.movePlayer(2);
        }
    }

    void update(float dt) {
        handleInput(dt);
//        moveCamera();
        platformsController.setCameraY(mainCamera.position.y);
        platformsController.createAndArrangeNewPlatforms();
    }

    void moveCamera() {
        mainCamera.position.y -= 1f;
    }

    Color randomColor() {

        int randomIndex = generator.nextInt(colors.length);
        return colors[randomIndex];
    }

//    void changeColor(float dt) {
//
//        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        colorAction.act(dt);
//
//    }

    @Override
    public void render(float delta) {
        update(delta);

//        changeColor(delta);

        Gdx.gl.glClearColor(activeColor.r,activeColor.g,activeColor.b,activeColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        repeatAction.act(delta);


//        testRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        testRenderer.setColor(activeColor);
//        testRenderer.rect(100, 100, 40, 40);
//        testRenderer.end();

        actionManager.act(delta);
        game.getBatch().begin();

        platformsController.drawPlatforms(game.getBatch());

        player.drawPlayer(game.getBatch());

        game.getBatch().end();

        debugRenderer.render(world, box2DCamera.combined);

        game.getBatch().setProjectionMatrix(mainCamera.combined);
        mainCamera.update();

        player.updatePlayer();

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
