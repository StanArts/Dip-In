package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

public class GameManager {
    private static GameManager ourInstance = new GameManager();

    public GameData gameData;
    private Json json = new Json();
    private FileHandle fileHandle = Gdx.files.local("bin/GameData.json");

    public boolean gameStartedFromMainMenu, isPaused = true;
    public int lifeScore, coinScore, score;

    private GameManager() {

    }

    public void initializeGameData() {
        if (!fileHandle.exists()) {
            gameData = new GameData();

            gameData.setHighScore(0);
            gameData.setCoinHighScore(0);

            gameData.setEasyDifficulty(false);
            gameData.setEasyDifficulty(true);
            gameData.setEasyDifficulty(false);

            gameData.setMusicOn(false);

            saveData();
        } else {
            loadData();
        }
    }

    public void saveData() {
        if (gameData != null) {

            fileHandle.writeString(Base64Coder.encodeString(json.prettyPrint(gameData)), false);
        }
    }

    public void loadData() {
        gameData = json.fromJson(GameData.class, Base64Coder.decodeString(fileHandle.readString()));
    }

    public void checkForNewHighScores() {

        int oldHighScore = gameData.getHighScore();
        int oldCoinScore = gameData.getCoinHighScore();

        if (oldCoinScore < score) {

            gameData.setCoinHighScore(score);
        }
    }

    public static GameManager getInstance() {

        return ourInstance;
    }
}
