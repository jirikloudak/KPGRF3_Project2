package p01simple;

import lwjglutils.OGLTextRenderer;
import lwjglutils.OGLUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author PGRF FIM UHK
 * @version 2.0
 * @since 2019-09-02
 */
public abstract class AbstractRenderer {

    private int pass;
    protected int width;
    protected int height;
    protected OGLTextRenderer textRenderer;

    public void init() {
        OGLUtils.printOGLparameters();
        OGLUtils.printLWJLparameters();
        OGLUtils.printJAVAparameters();
        OGLUtils.shaderCheck();
        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        textRenderer = new OGLTextRenderer(width, height);
    }

    public void display() {
        glViewport(0, 0, width, height);
        String text = new String(this.getClass().getName() + ": look at console and try keys, mouse, wheel and window interaction ");

        pass++;
        // Set the clear color
        glClearColor(
                (float) (Math.sin(pass / 100.) / 2 + 0.5),
                (float) (Math.cos(pass / 200.) / 2 + 0.5),
                (float) (Math.sin(pass / 300.) / 2 + 0.5),
                0.0f
        );
        // clear the framebuffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        //create and draw text
        textRenderer.addStr2D(3, 20, text);
        textRenderer.addStr2D(3, 50, "pass " + pass);
        textRenderer.addStr2D(width - 90, height - 3, " (c) PGRF UHK");
    }

    protected GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                // We will detect this in our rendering loop
                glfwSetWindowShouldClose(window, true);
            if (action == GLFW_RELEASE) {
            }
            if (action == GLFW_PRESS) {
            }
        }
    };

    protected GLFWWindowSizeCallback wsCallback = new GLFWWindowSizeCallback() {
        @Override
        public void invoke(long window, int w, int h) {
            if (w > 0 && h > 0) {
                width = w;
                height = h;
                System.out.println("Windows resize to [" + w + ", " + h + "]");
                if (textRenderer != null) {
                    textRenderer.resize(width, height);
                }
            }
        }
    };

    protected GLFWMouseButtonCallback mbCallback = new GLFWMouseButtonCallback() {
        @Override
        public void invoke(long window, int button, int action, int mods) {
            //mouseButton1 = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS;
            DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
            glfwGetCursorPos(window, xBuffer, yBuffer);
            double x = xBuffer.get(0);
            double y = yBuffer.get(0);

            if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {

            }

            if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_RELEASE) {

            }
        }
    };

    protected GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {
        @Override
        public void invoke(long window, double x, double y) {
        }
    };

    protected GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
        @Override
        public void invoke(long window, double dx, double dy) {
        }
    };

    public GLFWKeyCallback getKeyCallback() {
        return keyCallback;
    }

    public GLFWWindowSizeCallback getWsCallback() {
        return wsCallback;
    }

    public GLFWMouseButtonCallback getMouseCallback() {
        return mbCallback;
    }

    public GLFWCursorPosCallback getCursorCallback() {
        return cursorPosCallback;
    }

    public GLFWScrollCallback getScrollCallback() {
        return scrollCallback;
    }

}
