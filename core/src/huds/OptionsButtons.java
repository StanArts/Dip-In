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

public class OptionsButtons implements Screen {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private Label titleLabel;

    private ImageButton easy, medium, hard, backBtn;
    private Image sign;

    private float signXPos = 66f;
    private float signYPos = 12f;

    private Sound selectSound;

    public OptionsButtons(GameMain game) {
        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT,
                new OrthographicCamera());

        stage = new Stage(gameViewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);

        createAndPositionButtons();
        addAllListeners();

        stage.addActor(titleLabel);
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

        easy.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 50, Align.center);
        medium.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 50, Align.center);
        hard.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 150, Align.center);

        positionTheSign();

        selectSound = Gdx.audio.newSound(Gdx.files.internal("SFX/select_option_sound.wav"));
    }

    void addAllListeners() {
        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
                selectSound.play();
            }
        });

        easy.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changeDifficulty(0);
                sign.setY(easy.getY() + signYPos);
                selectSound.play();
            }
        });

        medium.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changeDifficulty(1);
                sign.setY(medium.getY() + signYPos);
                selectSound.play();
            }
        });

        hard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changeDifficulty(2);
                sign.setY(hard.getY() + signYPos);
                selectSound.play();
            }
        });
    }

    void positionTheSign() {

        if (GameManager.getInstance().gameData.isEasyDifficulty()) {
            sign.setPosition(GameInfo.WIDTH / 2f + signXPos, easy.getY() + signYPos,
                    Align.bottomLeft);
        }

        if (GameManager.getInstance().gameData.isMediumDifficulty()) {
            sign.setPosition(GameInfo.WIDTH / 2f + signXPos, medium.getY() + signYPos,
                    Align.bottomLeft);
        }

        if (GameManager.getInstance().gameData.isHardDifficulty()) {
            sign.setPosition(GameInfo.WIDTH / 2f + signXPos, hard.getY() + signYPos,
                    Align.bottomLeft);
        }
    }

    void changeDifficulty(int diffictulty) {

        switch (diffictulty) {
            case 0:
                GameManager.getInstance().gameData.setEasyDifficulty(true);
                GameManager.getInstance().gameData.setMediumDifficulty(false);
                GameManager.getInstance().gameData.setHardDifficulty(false);
                break;

            case 1:
                GameManager.getInstance().gameData.setEasyDifficulty(false);
                GameManager.getInstance().gameData.setMediumDifficulty(true);
                GameManager.getInstance().gameData.setHardDifficulty(false);
                break;

            case 2:
                GameManager.getInstance().gameData.setEasyDifficulty(false);
                GameManager.getInstance().gameData.setMediumDifficulty(false);
                GameManager.getInstance().gameData.setHardDifficulty(true);
                break;
        }

        GameManager.getInstance().saveData();
    }

    public Stage getStage() {
        return stage;
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
}
