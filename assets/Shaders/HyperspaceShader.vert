uniform mat4 g_WorldViewProjectionMatrix;
attribute vec3 inPosition;
attribute vec3 inTexCoord;

varying vec3 texCoord;

void main() {
    texCoord = inTexCoord;
    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
}