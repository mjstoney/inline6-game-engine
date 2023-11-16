package dev.mstoney.core;

import dev.mstoney.core.utils.Constants;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import test.Launcher;

public class EngineManager {
    public static final long NANOSECOND = 1000000000L;
    public static final float FRAMERATE = 60;

    private static int fps;
    private float frametime = 1.0f / FRAMERATE;

    private boolean isRunning;
    private WindowManager windowManager;
    private MouseInput mouseInput;
    private GLFWErrorCallback errorCallback;
    private ILogic gameLogic;
    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        windowManager = Launcher.getWindow();
        mouseInput = new MouseInput();
        gameLogic = Launcher.getGame();
        windowManager.init();
        mouseInput.init();
        gameLogic.init();

    }

    public void start() throws Exception {
        init();
        if (isRunning)
            return;
        else
            run();
    }

    public void run() {
        this.isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while (isRunning) {
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) NANOSECOND;
            frameCounter += passedTime;
            input();
            // input

            while (unprocessedTime > frametime) {
                render = true;
                unprocessedTime -= frametime;

                if (windowManager.windowShouldClose())
                    stop();

                if (frameCounter >= NANOSECOND) {
                    setFps(frames);
                    windowManager.setTitle(Constants.title + getFps());
                    frames = 0;
                    frameCounter = 0;
                }
            }


            if (render) {
                update(frametime, mouseInput);
                render();
                frames++;
            }
        }
        cleanup();
    }


    public void stop() {
        if (!isRunning)
            return;
        isRunning = false;
    }

    public void input() {
        gameLogic.input();
        mouseInput.input();
    }

    public void update(float interval, MouseInput mouseInput) {
        gameLogic.update(interval, mouseInput);
    }

    public void render() {
        gameLogic.render();
        windowManager.update();
    }

    public void cleanup() {
        windowManager.cleanup();
        gameLogic.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    public static int getFps() {
        return fps;
    }

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }
}
