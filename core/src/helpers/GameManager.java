package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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

    private Music music;

    private GameManager() {

    }

    public void initializeGameData() {
        if (!fileHandle.exists()) {
            gameData = new GameData();

            gameData.setHighScore(0);
            gameData.setCoinHighScore(0);

            gameData.setEasyDifficulty(false);
            gameData.setMediumDifficulty(true);
            gameData.setHardDifficulty(false);

            gameData.setMusicOn(true);

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

        if (oldHighScore < score) {

            gameData.setHighScore(score);
        }

        if (oldCoinScore < coinScore) {

            gameData.setCoinHighScore(coinScore);
        }
    }

    public void playMusic() {
        if (music == null) {
            music = Gdx.audio.newMusic(Gdx.files.internal("SFX/background_music_deep_urban.mp3"));
        }

        if (!music.isPlaying()) {
            music.play();
            music.setLooping(true);
            music.setVolume(0.5f);
        }
    }

    public void stopMusic() {
        if (music.isPlaying()) {
            music.stop();
            music.dispose();
        }
    }

    public static GameManager getInstance() {
        return ourInstance;
    }
}
