package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width;
    private int height;
    private String title;
    private long glfwWindow;
    private float r, g, b, a;
    private boolean fadeToBlack = false;

    private static Window window = null;

    //Constructor
    private Window() {
       this.width = 1920;
       this.height = 1080;
       this.title = "Mario Bross";
       this.r = 1;
       this.g = 1;
       this.b = 1;
       this.a = 1;
    }


    public static Window get() {
        //Apply singleton
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public void run() {
        System.out.println("Hi LWJGL " + Version.getVersion() + "!");
        init();
        loop();

        //Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        //Setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW,");
        }

        //Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //Create the window
        //it returns the memory
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        if (glfwWindow  == NULL) {
            throw new IllegalStateException("Failed to create the GLFM window.");
        }

        //Note this  :: uses lambda expression in java
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);

        //set key callback
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        //Make the openGL  context current
        glfwMakeContextCurrent(glfwWindow);

        //Enable v-sync
        glfwSwapInterval(1);

        //Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
    }

    public void loop() {
       while(!glfwWindowShouldClose(glfwWindow)) {
           //Poll events
           glfwPollEvents();

           glClearColor(r, g, b, a);
           glClear(GL_COLOR_BUFFER_BIT);

           if (this.fadeToBlack) {
               this.r = Math.max(this.r - 0.01f, 0);
               this.g = Math.max(this.g - 0.01f, 0);
               this.b = Math.max(this.b - 0.01f, 0);
           }
           if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
               System.out.println("Space key is pressed");
               this.fadeToBlack = true;
           }

           glfwSwapBuffers(glfwWindow);
       }
    }

}
