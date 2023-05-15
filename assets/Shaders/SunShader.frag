
uniform vec4 m_Color;
uniform sampler2D m_BaseTexture;
uniform sampler2D m_OverlayTexture;
uniform float m_Alpha;
uniform float m_Theta;

varying vec2 texCoord;


vec2 rotateUV(vec2 uv, vec2 pivot, float rotation) {
    float cosAngle = cos(rotation);
    float sinAngle = sin(rotation);
    return vec2(
        cosAngle * (uv.x - pivot.x) + sinAngle * (uv.y - pivot.x) + pivot.x,
        cosAngle * (uv.y - pivot.y) - sinAngle * (uv.x - pivot.y) + pivot.y
    );
}

void main() {
    vec4 primary = texture2D(m_BaseTexture, texCoord);
    vec4 secondary = texture2D(m_OverlayTexture, rotateUV(texCoord, vec2(0.5, 0.5), m_Theta));
    if (secondary.w < 0.9) {
        secondary.w = 0.0;
    }
    else {
        secondary.w = 1.0;
    }
    float dist = distance(texCoord, vec2(0.5, 0.5));
    vec4 outcolor = m_Color;
    outcolor.w = primary.w*m_Alpha*(0.5-dist);
    gl_FragColor = outcolor;
}
