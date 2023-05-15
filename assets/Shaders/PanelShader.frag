
uniform vec4 m_BaseColor;
uniform sampler2D m_AlphaMap;
uniform sampler2D m_NoiseMap;
uniform vec2 m_NoiseOffset;
uniform float m_NoiseFactor;
uniform float m_Alpha;

varying vec3 texCoord;

void main() {
    vec2 uv1 = vec2(texCoord.x, texCoord.y+0.19);
    vec2 uv2 = vec2(texCoord.x+m_NoiseOffset.x, texCoord.y+m_NoiseOffset.y);
    float grey1 = texture2D(m_AlphaMap, uv1).x;
    float grey2 = 1.0f;
    float nfac = 1.0f;
    #ifdef NOISE
        grey2 = texture2D(m_NoiseMap, uv2).x;
        nfac = m_NoiseFactor;
    #endif
    float factor = grey1+(grey2-grey1)*m_NoiseFactor;
    vec4 outcolor = m_BaseColor*factor*m_Alpha;
    outcolor.w = m_Alpha;
    gl_FragColor = outcolor;
}
