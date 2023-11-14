package dev.mstoney.core;

import dev.mstoney.core.utils.Constants;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import test.Launcher;

public class EngineManager {
    public static final long NANOSECOND = 1000000000L;
    public static final float FRAMERATE = 1000;

    private static int fps;
    private float frametime = 1.0f / FRAMERATE;

    private boolean isRunning;
    private WindowManager window;
    private GLFWErrorCallback errorCallback;
    private ILogic gameLogic;
    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = Launcher.getWindow();
        gameLogic = Launcher.getGame();
        window.init();
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

                if (window.windowShouldClose())
                    stop();

                if (frameCounter >= NANOSECOND) {
                    setFps(frames);
                    window.setTitle(Constants.title + getFps());
                    frames = 0;
                    frameCounter = 0;
                }
            }


            if (render) {
                update();
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
    }

    public void update() {
        gameLogic.update();
    }

    public void render() {
        gameLogic.render();
        window.update();
    }

    public void cleanup() {
        window.cleanup();
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
