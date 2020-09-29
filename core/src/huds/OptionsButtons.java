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
import scenes.MainMenu;

public class OptionsButtons {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private Label titleLabel;

    private ImageButton easy, medium, hard, backBtn;
    private Image sign;

    public OptionsButtons(GameMain game) {
        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT,
                new OrthographicCamera());

        stage = new Stage(gameViewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);

        createAndPositionButtons();
        addAllListeners();

        stage.addActor(easy);
        stage.addActor(medium);
        stage.addActor(hard);
        stage.addActor(backBtn);
        stage.addActor(sign);
    }

    void createAndPositionButtons() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Berlin Sans FB Regular.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameterTitle =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameterTitle.size = 100;

        BitmapFont titleFont = generator.generateFont(parameterTitle);

        titleLabel = new Label("Settings", new Label.LabelStyle(titleFont, Color.BLACK));

        easy = new ImageButton(new SpriteDrawable(new Sprite(new Texture("UI/Buttons/Options Menu/Easy.png"))));
        medium = new ImageButton(new SpriteDrawable(new Sprite(new Texture("UI/Buttons/Options Menu/Medium.png"))));
        hard = new ImageButton(new SpriteDrawable(new Sprite(new Texture("UI/Buttons/Options Menu/Hard.png"))));
        backBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("UI/Buttons/Options Menu/Back.png"))));

        sign = new Image(new Texture("UI/Buttons/Options Menu/Switch.png"));

        titleLabel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 240, Align.center);
        backBtn.setPosition(17, 17, Align.bottomLeft);

        easy.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 40, Align.center);
        medium.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 30, Align.center);
        hard.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 100, Align.center);

        sign.setPosition(GameInfo.WIDTH / 2f + 48, medium.getY() + 13,
                Align.bottomLeft);
    }

    void addAllListeners() {
        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
            }
        });

        easy.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sign.setY(easy.getY() + 13);
            }
        });

        medium.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sign.setY(medium.getY() + 13);
            }
        });

        hard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sign.setY(hard.getY() + 13);
            }
        });
    }

    public Stage getStage() {
        return this.stage;
    }
}
