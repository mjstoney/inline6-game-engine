package dev.mstoney.core;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import test.Launcher;

public class MouseInput {
    private final Vector2d previousPos, currentPos;
    private final Vector2f displVec;
    private boolean inWindow = false, leftButtonPress = false, rightButtonPress = false;
    public MouseInput() {
        previousPos = new Vector2d(-1, -1);
        currentPos = new Vector2d(0, 0);
        displVec = new Vector2f();
    }

    public void init() {

        GLFW.glfwSetCursorPosCallback(Launcher.getWindow().getWindowHandle(), (window, xPos, yPos) -> {
            currentPos.x = xPos;
            currentPos.y = yPos;
        });

        GLFW.glfwSetCursorEnterCallback(Launcher.getWindow().getWindowHandle(), (window, entered) -> {
            inWindow = entered;
        });
        GLFW.glfwSetMouseButtonCallback(Launcher.getWindow().getWindowHandle(), (window, button, action, mode) -> {
            leftButtonPress = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS;
            rightButtonPress = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS;
        });
    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    public boolean isLeftButtonPress() {
        return leftButtonPress;
    }

    public boolean isRightButtonPress() {
        return rightButtonPress;
    }

    public void input() {
        displVec.x = 0;
        displVec.y = 0;
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            double x = currentPos.x - previousPos.x;
            double y = currentPos.y - previousPos.y;
            boolean rotateX = x != 0;
            boolean rotateY = y != 0;
            if (rotateX) {
                displVec.y = (float) x;
            }
            if (rotateY) {
                displVec.x = (float) y;
            }
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }
}
