package test;

import dev.mstoney.core.EngineManager;
import dev.mstoney.core.WindowManager;
import dev.mstoney.core.utils.Constants;
import org.lwjgl.Version;

public class Launcher {
    private static WindowManager window;
    private static TestGame game;

    public static void main(String[] args) {
        window = new WindowManager(Constants.title, 1600, 900, false);
        game = new TestGame();

        EngineManager engine = new EngineManager();
        try {
            engine.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static TestGame getGame() {
        return game;
    }

    public static WindowManager getWindow() {
        return window;
    }
}
