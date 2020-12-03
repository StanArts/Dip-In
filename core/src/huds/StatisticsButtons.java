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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.stangvel.dipin.GameMain;

import helpers.GameInfo;
import helpers.GameManager;
import scenes.MainMenu;

public class StatisticsButtons {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private Label titleLabel, scoreLabelText, scoreLabel, coinCounterLabel;

    private ImageButton backBtn;

    private Image coinCounterImage;

    public StatisticsButtons(GameMain game) {
        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT,
                new OrthographicCamera());

        stage = new Stage(gameViewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);

        createAndPositionUIElements();

        stage.addActor(backBtn);
        stage.addActor(coinCounterImage);
        stage.addActor(titleLabel);
        stage.addActor(scoreLabelText);
        stage.addActor(scoreLabel);
        stage.addActor(coinCounterLabel);
    }

    void createAndPositionUIElements() {

        backBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("UI/Buttons/Options Menu/Back.png"))));

        coinCounterImage = new Image(new Texture("Collectables/Coin.png"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Berlin Sans FB Regular.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameterTitle =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameterTitle.size = 100;
        parameter.size = 60;

        BitmapFont titleFont = generator.generateFont(parameterTitle);
        BitmapFont scoreFont = generator.generateFont(parameter);
        BitmapFont scoreFontText = generator.generateFont(parameter);
        BitmapFont coinFont = generator.generateFont(parameter);

        titleLabel = new Label("Statistics", new Label.LabelStyle(titleFont, Color.BLACK));

        scoreLabelText = new Label("High score", new Label.LabelStyle(scoreFontText, Color.BLACK));
        scoreLabel = new Label(String.valueOf(GameManager.getInstance().gameData.getHighScore()), new Label.LabelStyle(scoreFont, Color.BLACK));
        coinCounterLabel = new Label(":" + GameManager.getInstance().gameData.getCoinHighScore(), new Label.LabelStyle(coinFont, Color.BLACK));

        backBtn.setPosition(17, 17, Align.bottomLeft);

        coinCounterImage.setPosition(GameInfo.WIDTH / 2f - 140, GameInfo.HEIGHT / 2f - 240);

        titleLabel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 240, Align.center);
        scoreLabelText.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 10, Align.center);
        scoreLabel.setPosition(GameInfo.WIDTH / 2f - 50, GameInfo.HEIGHT / 2f - 120);
        coinCounterLabel.setPosition(GameInfo.WIDTH / 2f - 50, GameInfo.HEIGHT / 2f - 240);

        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
            }
        });
    }

    public Stage getStage() {
        return this.stage;
    }
}
