#version 150
out vec4 outColor;

in vec2 textureCoord;

uniform sampler2D targetTexture;
uniform int matrixMode;
uniform int displayMode;

// https://www.liquisearch.com/ordered_dithering
// https://en.wikipedia.org/wiki/Ordered_dithering
// https://gist.github.com/MehdiNS/bd41bbc6db780c9409157d35d331ac80

// 2x2 -> 1/4
int indexMatrix2x2[4] = int[]
(
0, 2,
3, 1
);

// 3x3 -> 1/9
int indexMatrix3x3[9] = int[]
(0, 7, 3,
6, 5, 2,
4, 1, 8);

// 4x4 -> 1/16
int indexMatrix4x4[16] = int[]
(0, 8, 2, 10,
12, 4, 14, 6,
3, 11, 1, 9,
15, 7, 13, 5);

// 8x8 -> 1/64
int indexMatrix8x8[64] = int[]
(0, 48, 12, 60, 3, 51, 15, 63,
32, 16, 44, 28, 35, 19, 47, 31,
8, 56, 4, 52, 11, 59, 7, 55,
40, 24, 36, 20, 43, 27, 39, 23,
2, 50, 24, 62, 1, 49, 13, 61,
34, 18, 46, 30, 33, 17, 45, 29,
10, 58, 6, 54, 9, 57, 5, 53,
42, 26, 38, 22, 41, 25, 37, 21);

float indexValue(int matrixMode){
    vec2 value;
    switch (matrixMode) {
    case 0:
    value = vec2(int(mod(gl_FragCoord.x, 2)), int(mod(gl_FragCoord.y, 2)));
    return indexMatrix2x2[int(value.x) + int(value.y)*2] * (1 / 4.0);
    case 1:
    value = vec2(int(mod(gl_FragCoord.x, 3)), int(mod(gl_FragCoord.y, 3)));
    return indexMatrix3x3[int(value.x) + int(value.y)*3] * (1 / 9.0);
    case 2:
    value = vec2(int(mod(gl_FragCoord.x, 4)), int(mod(gl_FragCoord.y, 4)));
    return indexMatrix4x4[int(value.x) + int(value.y)*4] * (1 / 16.0);
    case 3:
    value = vec2(int(mod(gl_FragCoord.x, 8)), int(mod(gl_FragCoord.y, 8)));
     return indexMatrix8x8[int(value.x) + int(value.y)*8] * (1 / 64.0);
    }
};

float dithering(float color, int matrixMode){
    float closestColor = (color < 0.5) ? 0 : 1;
    float secondClosest = 1 - closestColor;
    float d = indexValue(matrixMode);
    float hueDiff = abs(closestColor - color);
    return (hueDiff < d) ? closestColor : secondClosest;
}

void main() {
    vec4 textureColor = texture(targetTexture, textureCoord);
    if (displayMode == 0) {
        outColor = vec4(textureColor.rgb, 1.0);
    } else if (displayMode == 1) {
        outColor = vec4(
            dithering(textureColor.r, matrixMode),
            dithering(textureColor.g, matrixMode),
            dithering(textureColor.b, matrixMode),
            1.0);
    } else if (displayMode == 2){
        float grey = 0.21 * textureColor.r + 0.71 * textureColor.g + 0.07 * textureColor.b;
        float u_colorFactor = 0;
        textureColor = vec4(textureColor.r * u_colorFactor + grey * (1.0 - u_colorFactor), textureColor.g * u_colorFactor + grey * (1.0 - u_colorFactor), textureColor.b * u_colorFactor + grey * (1.0 - u_colorFactor), 1.0);
        outColor = vec4(
            dithering(textureColor.r, matrixMode),
            dithering(textureColor.g, matrixMode),
            dithering(textureColor.b, matrixMode),
            1.0);
    } else if (displayMode == 3){
        textureColor = vec4(1 - textureColor.rgb, 1.0);
        outColor = vec4(
            dithering(textureColor.r, matrixMode),
            dithering(textureColor.g, matrixMode),
            dithering(textureColor.b, matrixMode),
            1.0);
    }
}
