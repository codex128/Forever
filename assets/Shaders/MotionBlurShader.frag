
uniform sampler2D m_Texture;
uniform float m_MinimumBlurRadius;

varying vec2 texCoord;

void main() {
    vec2 center = vec2(0.5, 0.5);
    float dist = distance(texCoord, center);
    float factor = dist-m_MinimumBlurRadius;
    vec4 color;
    if (factor > 0.0) {
        vec2 dir = normalize(center-texCoord);
        dir *= factor/5.0;
        color = texture2D(m_Texture, texCoord+dir);
    }
    //else {
        color = texture2D(m_Texture, texCoord);
    //}
    gl_FragColor = color;
}
