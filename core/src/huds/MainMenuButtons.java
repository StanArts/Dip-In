package huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.stangvel.dipin.GameMain;

import helpers.GameInfo;
import helpers.GameManager;
import scenes.Gameplay;
import scenes.Options;
import scenes.Statistics;

public class MainMenuButtons implements Screen {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private Label titleLabel;
    private Label rightsLabel;

    private ImageButton playBtn;
    private ImageButton statisticsBtn;
    private ImageButton optionsBtn;
    private ImageButton aboutBtn;
    private ImageButton quitBtn;
    private ImageButton musicBtn;

    private Sound selectSound;

    public MainMenuButtons(GameMain game) {
        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT,
                new OrthographicCamera());

        stage = new Stage(gameViewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);

        createAndPositionButton();
        addAllListeners();

        stage.addActor(titleLabel);
        stage.addActor(rightsLabel);
        stage.addActor(playBtn);
        stage.addActor(statisticsBtn);
        stage.addActor(optionsBtn);
        stage.addActor(aboutBtn);
        stage.addActor(quitBtn);
        stage.addActor(musicBtn);

        checkMusic();
    }

    void createAndPositionButton() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Berlin Sans FB Regular.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameterTitle =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameterTitle.size = 120;

        FreeTypeFontGenerator.FreeTypeFontParameter parameterRights =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameterRights.size = 20;

        BitmapFont titleFont = generator.generateFont(parameterTitle);
        BitmapFont rightsFont = generator.generateFont(parameterRights);

        titleLabel = new Label("DipIn", new Label.LabelStyle(titleFont, Color.BLACK));
        rightsLabel = new Label("© All rights reserved, StanArts, 2020", new Label.LabelStyle(rightsFont, Color.BLACK));

        playBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("UI/Buttons/Main Menu/Start.png"))));
        statisticsBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("UI/Buttons/Main Menu/Statistics.png"))));
        optionsBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("UI/Buttons/Main Menu/Settings.png"))));
        aboutBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("UI/Buttons/Main Menu/More_Games.png"))));
        quitBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("UI/Buttons/Main Menu/Quit.png"))));
        musicBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("UI/Buttons/Main Menu/Music_On.png"))));

        titleLabel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 240, Align.center);
        rightsLabel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 380, Align.center);

        playBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 50, Align.center);
        statisticsBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 20, Align.center);
        optionsBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 90, Align.center);
        aboutBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 160, Align.center);
        quitBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 230, Align.center);

        musicBtn.setPosition(GameInfo.WIDTH - 13, 13, Align.bottomRight);

        selectSound = Gdx.audio.newSound(Gdx.files.internal("SFX/select_option_sound.wav"));
    }

    void addAllListeners() {
        playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectSound.play();

                GameManager.getInstance().gameStartedFromMainMenu = true;

                RunnableAction run = new RunnableAction();
                run.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new Gameplay(game));
                    }
                });

                SequenceAction sa = new SequenceAction();
                sa.addAction(Actions.fadeOut(1f));
                sa.addAction(run);

                stage.addAction(sa);
            }
        });

        statisticsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectSound.play();

                RunnableAction run = new RunnableAction();
                run.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new Statistics(game));
                    }
                });

                SequenceAction sa = new SequenceAction();
                sa.addAction(Actions.fadeOut(1f));
                sa.addAction(run);

                stage.addAction(sa);
            }
        });

        optionsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectSound.play();

                RunnableAction run = new RunnableAction();
                run.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new Options(game));
                    }
                });

                SequenceAction sa = new SequenceAction();
                sa.addAction(Actions.fadeOut(1f));
                sa.addAction(run);

                stage.addAction(sa);
            }
        });

        aboutBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectSound.play();
                Gdx.net.openURI("https://stan-arts.itch.io/");
            }
        });

        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectSound.play();
                Gdx.app.exit();
            }
        });

        musicBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                selectSound.play();

                if (GameManager.getInstance().gameData.isMusicOn()) {
                    GameManager.getInstance().gameData.setMusicOn(false);
                    GameManager.getInstance().stopMusic();
                } else {
                    GameManager.getInstance().gameData.setMusicOn(true);
                    GameManager.getInstance().playMusic();
                }
                GameManager.getInstance().saveData();
            }
        });
    }

    void checkMusic() {
        if (GameManager.getInstance().gameData.isMusicOn()) {
            GameManager.getInstance().playMusic();
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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
        selectSound.dispose();
    }

    public Stage getStage() {
        return stage;
    }
}
