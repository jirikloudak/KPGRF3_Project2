#version 150
out vec4 outColor;

in vec2 textureCoord;

uniform sampler2D texture;
uniform int matrixMode;
uniform int displayMode;

// https://www.liquisearch.com/ordered_dithering
// https://en.wikipedia.org/wiki/Ordered_dithering

// 2x2 -> 1/4
int indexMatrix2x2_1[4] = int[]
(
0, 2,
3, 1
);

//2x2 -> 1/5
int indexMatrix2x2_2[4] = int[]
(
1, 3,
4, 2
);

float indexValue(int matrixMode){
    vec2 value;
    switch (matrixMode) {
    case 0:
    value = vec2(int(mod(gl_FragCoord.x, 2)), int(mod(gl_FragCoord.y, 2)));
    return indexMatrix2x2_1[int(value.x) + int(value.y)*2] * (1 / 4.0);
    case 1:
    value = vec2(int(mod(gl_FragCoord.x, 2)), int(mod(gl_FragCoord.y, 2)));
    return indexMatrix2x2_2[int(value.x) + int(value.y)*2] * (1 / 4.0);
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
    vec3 textureColor = texture2D(texture, textureCoord).rgb;
    if (displayMode == 0) {
        outColor = vec4(textureColor, 1.0);
    } else if (displayMode == 1) {
        outColor = vec4(vec3(
                dithering(textureColor.r, matrixMode),
                dithering(textureColor.g, matrixMode),
                dithering(textureColor.b, matrixMode)
                ), 1.0);
    }
}
