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
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.stangvel.dipin.GameMain;

import java.util.Random;

import backgrounds.Background;
import helpers.GameInfo;
import helpers.GameManager;
import huds.UIHud;
import platforms.PlatformsController;
import player.Player;

public class Gameplay implements Screen, ContactListener {

    private GameMain game;

    private OrthographicCamera mainCamera;
    private Viewport gameViewport;

    private OrthographicCamera box2DCamera;
    private Box2DDebugRenderer debugRenderer;

    private boolean initialTouch;

    private World world;

    private UIHud hud;

    private PlatformsController platformsController;
    private Player player;
    private Background bg;

    public Gameplay (GameMain game) {
        this.game = game;

        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);

        box2DCamera = new OrthographicCamera();
        box2DCamera.setToOrtho(false, GameInfo.WIDTH / GameInfo.PPM,
                GameInfo.HEIGHT / GameInfo.PPM);
        box2DCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        debugRenderer = new Box2DDebugRenderer();

        hud = new UIHud(game);

        world = new World(new Vector2(0, -9.8f), true);
        world.setContactListener(this);

        platformsController = new PlatformsController(world);

        player = platformsController.positionThePlayer(player);

        bg = new Background(game);
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

    void checkForFirstTouch() {
        if (!initialTouch) {
            if (Gdx.input.justTouched()) {
                initialTouch = true;
                GameManager.getInstance().isPaused = false;
            }
        }
    }

    void update(float dt) {

        checkForFirstTouch();

        if (!GameManager.getInstance().isPaused) {
            handleInput(dt);
            moveCamera();

            platformsController.setCameraY(mainCamera.position.y);
            platformsController.createAndArrangeNewPlatforms();
            platformsController.removeOffScreenCollectables();
        }
    }

    void moveCamera() {
        mainCamera.position.y -= 1f;
    }

    @Override
    public void render(float delta) {
        update(delta);

        bg.render(delta);

        game.getBatch().begin();

        platformsController.drawPlatforms(game.getBatch());
        platformsController.drawCollectables(game.getBatch());

        player.drawPlayer(game.getBatch());

        game.getBatch().end();

        debugRenderer.render(world, box2DCamera.combined);

        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
        hud.getStage().act();

        game.getBatch().setProjectionMatrix(mainCamera.combined);
        mainCamera.update();

        player.updatePlayer();

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
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
        world.dispose();
        bg.dispose();
        player.getTexture().dispose();
        debugRenderer.dispose();
    }

    @Override
    public void beginContact(Contact contact) {

        Fixture body1, body2;

        if (contact.getFixtureA().getUserData() == "Player") {
            body1 = contact.getFixtureA();
            body2 = contact.getFixtureB();
        } else {
            body1 = contact.getFixtureB();
            body2 = contact.getFixtureA();
        }

        if (body1.getUserData() == "Player" && body2.getUserData() == "Coin") {
            hud.incrementCoins();
            body2.setUserData("Remove");
            platformsController.removeCollectables();
        }

        if (body1.getUserData() == "Player" && body2.getUserData() == "Life") {
            hud.incrementLifes();
            body2.setUserData("Remove");
            platformsController.removeCollectables();
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
