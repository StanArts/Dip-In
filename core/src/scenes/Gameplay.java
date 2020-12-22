package scenes;

import com.stangvel.dipin.GameMain;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import backgrounds.Background;
import platforms.PlatformsController;
import helpers.GameInfo;
import helpers.GameManager;
import huds.UIHud;
import player.Player;

public class Gameplay implements Screen, ContactListener {

    private GameMain game;

    private OrthographicCamera mainCamera;
    private Viewport gameViewport;

    private OrthographicCamera box2DCamera;
    private Box2DDebugRenderer debugRenderer;

    private World world;

    private float cameraSpeed = 10;
    private float maxSpeed = 10;
    private float acceleration = 10;

    private boolean touchedForTheFirstTime;

    private UIHud hud;

    private PlatformsController platformsController;

    private Player player;
    private float lastPlayerY;

    private Background bg;

    private Sound coinSound, lifeSound;

    public Gameplay(GameMain game) {
        this.game = game;

        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT,
                mainCamera);

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

        setCameraSpeed();

        coinSound = Gdx.audio.newSound(Gdx.files.internal("SFX/coin_sound.wav"));
        lifeSound = Gdx.audio.newSound(Gdx.files.internal("SFX/life_sound.wav"));
    }

    void handleInput(float dt) {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.movePlayer(-2f);
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.movePlayer(2f);
        }
    }

    void handleInputAndroid() {
        if(Gdx.input.isTouched()) {
            if(Gdx.input.getX() > (GameInfo.WIDTH / 2)) {
                player.movePlayer(2f);
            } else {
                player.movePlayer(-2f);
            }
        }
    }

    void checkForFirstTouch() {
        if(!touchedForTheFirstTime) {
            if(Gdx.input.justTouched()) {
                touchedForTheFirstTime = true;
                GameManager.getInstance().isPaused = false;
                lastPlayerY = player.getY();
            }
        }
    }

    void update(float dt) {

        checkForFirstTouch();

        if(!GameManager.getInstance().isPaused) {
//            handleInput(dt);
            handleInputAndroid();
            moveCamera(dt);
            platformsController.setCameraY(mainCamera.position.y);
            platformsController.createAndArrangeNewPlatforms();
            platformsController.removeOffScreenCollectables();
            checkPlayersBounds();
            countScore();
        }
    }

    void moveCamera(float delta) {
        mainCamera.position.y -= cameraSpeed * delta;

        cameraSpeed += acceleration * delta;

        if(cameraSpeed > maxSpeed) {
            cameraSpeed = maxSpeed;
        }
    }

    void setCameraSpeed() {
        if(GameManager.getInstance().gameData.isEasyDifficulty()) {
            cameraSpeed = 80;
            maxSpeed = 100;
        }

        if(GameManager.getInstance().gameData.isMediumDifficulty()) {
            cameraSpeed = 100;
            maxSpeed = 120;
        }

        if(GameManager.getInstance().gameData.isHardDifficulty()) {
            cameraSpeed = 140;
            maxSpeed = 140;
        }
    }

    void checkPlayersBounds() {
        if(player.getY() - GameInfo.HEIGHT / 2f - player.getHeight() / 2f
                > mainCamera.position.y) {
            if(!player.isDead()) {
                playedDied();
            }
        }

        if(player.getY() + GameInfo.HEIGHT / 2f + player.getHeight() / 2f
                < mainCamera.position.y) {
            if(!player.isDead()) {
                playedDied();
            }
        }

        if(player.getX() - 25 > GameInfo.WIDTH || player.getX() + 70 < 0) {
            if(!player.isDead()) {
                playedDied();
            }
        }
    }

    void countScore() {
        if(lastPlayerY > player.getY()) {
            hud.incrementScore(1);
            lastPlayerY = player.getY();
        }
    }

    void playedDied() {

        GameManager.getInstance().isPaused = true;

        hud.decrementLife();

        player.setDead(true);

        player.setPosition(1000, 1000);

        if(GameManager.getInstance().lifeScore < 0) {
            GameManager.getInstance().checkForNewHighScores();

            hud.createGameOverPanel();

            RunnableAction run = new RunnableAction();
            run.setRunnable(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new MainMenu(game));
                }
            });

            SequenceAction sa = new SequenceAction();
            sa.addAction(Actions.delay(3f));
            sa.addAction(Actions.fadeOut(1f));
            sa.addAction(run);

            hud.getStage().addAction(sa);
        } else {
            RunnableAction run = new RunnableAction();
            run.setRunnable(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new Gameplay(game));
                }
            });

            SequenceAction sa = new SequenceAction();
            sa.addAction(Actions.delay(3f));
            sa.addAction(Actions.fadeOut(1f));
            sa.addAction(run);

            hud.getStage().addAction(sa);
        }
    }

    @Override
    public void show() {

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

//        debugRenderer.render(world, box2DCamera.combined);

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
        coinSound.dispose();
        lifeSound.dispose();
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture body1, body2;

        if(contact.getFixtureA().getUserData() == "Player") {
            body1 = contact.getFixtureA();
            body2 = contact.getFixtureB();
        } else {
            body1 = contact.getFixtureB();
            body2 = contact.getFixtureA();
        }

        if(body1.getUserData() == "Player" && body2.getUserData() == "Coin") {
            hud.incrementCoins();
            coinSound.play();
            body2.setUserData("Remove");
            platformsController.removeCollectables();
        }

        if(body1.getUserData() == "Player" && body2.getUserData() == "Life") {
            hud.incrementLifes();
            lifeSound.play();
            body2.setUserData("Remove");
            platformsController.removeCollectables();
        }

        if(body1.getUserData() == "Player" && body2.getUserData() == "Platform_O") {
            if(!player.isDead()) {
                playedDied();
            }
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


































