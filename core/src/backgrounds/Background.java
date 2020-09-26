package backgrounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.stangvel.dipin.GameMain;

import java.util.Random;

public class Background implements Screen {

    private GameMain game;

    Color startColor;
    Color endColor;
    Color activeColor;
    ColorAction actionStE = new ColorAction();
    ColorAction actionEtS = new ColorAction();
    SequenceAction sequenceAction;
    ShapeRenderer testRenderer;
    RepeatAction repeatAction = new RepeatAction();

    private Actor actionManager = new Actor();

    Color[] colors = {Color.YELLOW, Color.GREEN, Color.BLUE};

    Random generator = new Random();

    public Background (GameMain game) {
        this.game = game;

        testRenderer = new ShapeRenderer();
        startColor = new Color(Color.RED);
        endColor = new Color(randomColor());
        activeColor = new Color(Color.RED);

        actionStE.setColor(activeColor);
        actionStE.setDuration(5);
        actionStE.setEndColor(endColor);

        actionEtS.setColor(activeColor);
        actionEtS.setDuration(5);
        actionEtS.setEndColor(startColor);

        sequenceAction = new SequenceAction(actionStE, actionEtS);
        repeatAction.setAction(sequenceAction);
        repeatAction.setCount(RepeatAction.FOREVER);
        actionManager.addAction(repeatAction);
    }

    Color randomColor() {
        int randomIndex = generator.nextInt(colors.length);
        return colors[randomIndex];
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(activeColor.r, activeColor.g, activeColor.b, activeColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        actionManager.act(delta);
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
