package p01simple;
//package lvl2advanced.p01gui.p01simple;

import lwjglutils.OGLBuffers;
import lwjglutils.OGLTexture2D;
import lwjglutils.ShaderUtils;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author PGRF FIM UHK
 * @version 2.0
 * @since 2019-09-02
 */
public class Renderer extends AbstractRenderer {

    private int shaderProgram;
    private int pictureId = 0, matrixMode = 0, displayMode = 0;
    private boolean changeTexture = false;
    private OGLBuffers buffers;
    private OGLTexture2D texture;


    @Override
    public void init() {
        super.init();

        glEnable(GL_DEPTH_TEST);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        shaderProgram = ShaderUtils.loadProgram("/start");

        buffers = GridFactory.generateGrid();

        try {
            texture = new OGLTexture2D("./textures/duck.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void display() {
        glEnable(GL_DEPTH_TEST);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        int locDisplayMode = glGetUniformLocation(shaderProgram, "displayMode");
        int locMatrixMode = glGetUniformLocation(shaderProgram, "matrixMode");

        glUniform1i(locDisplayMode, displayMode);
        glUniform1i(locMatrixMode, matrixMode);

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0, 0, width, height);
        glClearColor(0.5f, 0.1f, 0.1f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glUseProgram(shaderProgram);
        texture.bind(shaderProgram, "texture", 0);

        buffers.draw(GL_TRIANGLES, shaderProgram);

        String textureChangeHint = "Use arrows[<-][->] to change picture, [SPACE] to change display mode, [M] to change dithering matrix";

        switch (pictureId) {
            case 0 -> changeTexture("./textures/duck.jpg");
            case 1 -> changeTexture("./textures/cat.jpg");
            case 2 -> changeTexture("./textures/woman.jpg");
            case 3 -> changeTexture("./textures/orchard.jpg");
            case 4 -> changeTexture("./textures/coffee.jpg");
            case 5 -> changeTexture("./textures/dog.jpg");
            case 6 -> changeTexture("./textures/horse.jpg");
            case 7 -> changeTexture("./textures/cake.jpg");
            case 8 -> changeTexture("./textures/parrot.jpg");
        }

        String mode = "";
        switch (displayMode) {
            case 0 -> mode = "Clean picture";
            case 1 -> mode = "Color dithering";
            case 2 -> mode = "Grayscale dithering";
            case 3 -> mode = "Negative dithering";
        }

        String matrix = "";
        switch (matrixMode) {
            case 0 -> matrix = "2x2 -> 1/4";
            case 1 -> matrix = "3x3 -> 1/9";
            case 2 -> matrix = "4x4 -> 1/16";
            case 3 -> matrix = "8x8 -> 1/64";
        }

        textRenderer.addStr2D(3, 20, textureChangeHint);
        textRenderer.addStr2D(3, 40, "Display mode: " + mode);
        if (displayMode != 0) {
            textRenderer.addStr2D(3, 60, "Matrix mode: " + matrix);
        }
        textRenderer.addStr2D(width - 180, height - 3, " Jiri Klouda - PGRF UHK 2022");
    }

    private final GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            if (action == GLFW_PRESS || action == GLFW_REPEAT) {
                switch (key) {
                    case GLFW_KEY_RIGHT -> {
                        if (pictureId < 8) {
                            pictureId++;
                        } else pictureId = 0;
                        changeTexture = true;
                    }
                    case GLFW_KEY_LEFT -> {
                        if (pictureId > 0) {
                            pictureId--;
                        } else pictureId = 8;
                        changeTexture = true;
                    }
                    case GLFW_KEY_SPACE -> {
                        if (displayMode < 3) {
                            displayMode++;
                        } else displayMode = 0;
                        matrixMode = 0;
                    }
                    case GLFW_KEY_M -> {
                        if (matrixMode < 3) {
                            matrixMode++;
                        } else matrixMode = 0;
                        System.out.println(matrixMode);
                    }
                }
            }
        }
    };

    @Override
    public GLFWKeyCallback getKeyCallback() {
        return keyCallback;
    }

    public void changeTexture(String texturePath) {
        if (changeTexture) {
            try {
                texture = new OGLTexture2D(texturePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            changeTexture = false;
        }
    }

}
