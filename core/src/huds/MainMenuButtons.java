package huds;

import com.badlogic.gdx.Gdx;
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
import scenes.MainMenu;
import scenes.Options;
import scenes.Statistics;

public class MainMenuButtons {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private Label titleLabel;

    private ImageButton playBtn;
    private ImageButton statisticsBtn;
    private ImageButton optionsBtn;
    private ImageButton quitBtn;
    private ImageButton musicBtn;

    public MainMenuButtons(GameMain game) {
        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT,
                new OrthographicCamera());

        stage = new Stage(gameViewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);

        createAndPositionButton();
        addAllListeners();

        stage.addActor(titleLabel);
        stage.addActor(playBtn);
        stage.addActor(statisticsBtn);
        stage.addActor(optionsBtn);
        stage.addActor(quitBtn);
        stage.addActor(musicBtn);
    }

    void createAndPositionButton() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Berlin Sans FB Regular.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameterTitle =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameterTitle.size = 120;

        BitmapFont titleFont = generator.generateFont(parameterTitle);

        titleLabel = new Label("DipIn", new Label.LabelStyle(titleFont, Color.BLACK));

        playBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("UI/Buttons/Main Menu/Start.png"))));
        statisticsBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("UI/Buttons/Main Menu/Statistics.png"))));
        optionsBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("UI/Buttons/Main Menu/Settings.png"))));
        quitBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("UI/Buttons/Main Menu/Quit.png"))));
        musicBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("UI/Buttons/Main Menu/Music_On.png"))));

        titleLabel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 240, Align.center);

        playBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 50, Align.center);
        statisticsBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 50, Align.center);
        optionsBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 150, Align.center);
        quitBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 250, Align.center);

        musicBtn.setPosition(GameInfo.WIDTH - 13, 13, Align.bottomRight);
    }

    void addAllListeners() {
        playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                System.out.println("The play button was pressed.");
                GameManager.getInstance().gameStartedFromMainMenu = true;
                //game.setScreen(new Gameplay(game));

                RunnableAction run = new RunnableAction();
                run.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new Gameplay(game));
                    }
                });

                SequenceAction sa = new SequenceAction();
                //sa.addAction(Actions.delay(3f));
                sa.addAction(Actions.fadeOut(1f));
                sa.addAction(run);

                stage.addAction(sa);
            }
        });

        statisticsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //game.setScreen(new Statistics(game));
                RunnableAction run = new RunnableAction();
                run.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new Statistics(game));
                    }
                });

                SequenceAction sa = new SequenceAction();
                //sa.addAction(Actions.delay(3f));
                sa.addAction(Actions.fadeOut(1f));
                sa.addAction(run);

                stage.addAction(sa);
            }
        });

        optionsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new Options(game));
            }
        });

        optionsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });

        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });

        musicBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
    }

    public Stage getStage() {
        return stage;
    }
}
