#version 150
out vec4 outColor;

in vec2 textureCoord;

uniform sampler2D texture;

void main() {
    vec3 textureColor = texture2D(texture, textureCoord).rgb;
    outColor = vec4(textureColor, 1.0);
}
