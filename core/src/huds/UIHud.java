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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.stangvel.dipin.GameMain;

import helpers.GameInfo;
import scenes.MainMenu;

public class UIHud {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private Image coinImg, lifeImg, scoreImg, pausePanel;
    private Label coinLabel, lifeLabel, scoreLabel;

    private ImageButton pauseBtn, resumeBtn, quitBtn;

    public UIHud(GameMain game) {
        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT,
                new OrthographicCamera());

        stage = new Stage(gameViewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);

        createLabels();
        createImages();
        createBtnAndAddListener();

        Table lifeAndCoinTable = new Table();
        lifeAndCoinTable.top().left();
        lifeAndCoinTable.setFillParent(true);

        lifeAndCoinTable.add(lifeImg).padLeft(10).padTop(10);
        lifeAndCoinTable.add(lifeLabel).padLeft(10).padTop(10);
        lifeAndCoinTable.row();

        lifeAndCoinTable.add(coinImg).padLeft(10).padTop(10);
        lifeAndCoinTable.add(coinLabel).padLeft(10).padTop(10);

        Table scoreLabel = new Table();
        scoreLabel.top().right();
        scoreLabel.setFillParent(true);

        scoreLabel.add(scoreImg).padLeft(20).padTop(15);
        scoreLabel.row();
        scoreLabel.add(scoreLabel).padRight(20).padTop(15);

        stage.addActor(lifeAndCoinTable);
        stage.addActor(scoreLabel);
        stage.addActor(pauseBtn);
    }

    void createLabels() {

        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Berlin Sans FB Regular.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 40;

        BitmapFont font = generator.generateFont(parameter);

        coinLabel = new Label("0",
                new Label.LabelStyle(font, Color.WHITE));

        lifeLabel = new Label("x2",
                new Label.LabelStyle(font, Color.WHITE));

        scoreLabel = new Label("0",
                new Label.LabelStyle(font, Color.WHITE));
    }

    void createImages() {
        coinImg = new Image(new Texture("UI/Buttons/Gameplay/Coin_Counter.png"));
        lifeImg = new Image(new Texture("Collectables/Life.png"));
        scoreImg = new Image(new Texture("UI/Buttons/Gameplay/Score.png"));
    }

    void createBtnAndAddListener() {

        pauseBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("UI/Buttons/Gameplay/Pause.png")
        )));

        pauseBtn.setPosition(470, 17, Align.bottomRight);

        pauseBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // pauses the game
                createPausePanel();
            }
        });
    }

    void createPausePanel() {

        pausePanel = new Image(new Texture("UI/Buttons/Gameplay/Panel.png"));
        resumeBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("UI/Buttons/Gameplay/Resume.png"))));
        quitBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("UI/Buttons/Main Menu/Quit.png"))));

        pausePanel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, Align.center);
        resumeBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 50, Align.center);
        quitBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 80, Align.center);

        resumeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });

        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
            }
        });

        stage.addActor(pausePanel);
        stage.addActor(resumeBtn);
        stage.addActor(quitBtn);
    }

    void removePausePanel() {
        pausePanel.remove();
        resumeBtn.remove();
        quitBtn.remove();
    }

    public Stage getStage() {
        return this.stage;
    }
}
