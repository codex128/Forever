
uniform mat4 g_WorldViewProjectionMatrix;
uniform vec3 m_VertOffset;
attribute vec3 inPosition;
attribute vec2 inTexCoord;

varying vec2 texCoord;

void main() {
    texCoord = inTexCoord;
    vec3 pos = inPosition + m_VertOffset;
    gl_Position = g_WorldViewProjectionMatrix * vec4(pos, 1.0);
}
