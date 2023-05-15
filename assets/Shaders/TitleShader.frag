
uniform vec4 m_Color;
uniform sampler2D m_Texture;
uniform float m_AlphaFactor;

varying vec2 texCoord;

void main() {
    vec2 uv = texCoord;
    vec4 texcolor = texture2D(m_Texture, uv);
    vec4 mcolor = m_Color;
    float lerp = 0.04;
    float maximum = 0.2;
    mcolor.w = texcolor.w * 0.4 * m_AlphaFactor;
    gl_FragColor = mcolor;
}
