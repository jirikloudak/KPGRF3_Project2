package p01simple;

import lwjglutils.OGLBuffers;

public class GridFactory {

    public static OGLBuffers generateGrid() {
        float[] vbData = {
                -1, -1, 0, 1,
                1, -1, 1, 1,
                1, 1, 1, 0,
                -1, 1, 0, 0};
        int[] ibData = {0, 1, 2, 0, 2, 3};

        OGLBuffers.Attrib[] attributes = {
                new OGLBuffers.Attrib("inPosition", 2),
                new OGLBuffers.Attrib("inTextureCoord", 2)
        };

        return new OGLBuffers(vbData, 4, attributes, ibData);
    }

}
