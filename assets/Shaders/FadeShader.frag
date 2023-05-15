
uniform float m_Alpha;
#ifdef COLOR
    uniform vec4 m_Color;
#endif
#ifdef TEXTURE
    uniform sampler2D m_Texture;
#endif
varying vec2 texCoord;

void main() {
    vec4 use = vec4(1.0, 1.0, 1.0, 1.0);
    #ifdef COLOR
        use = m_Color;
    #endif
    #ifdef TEXTURE
        use = texture2D(m_Texture, texCoord);
    #endif
    use = use*0.75;
    use.x *= .5f;
    use.w = use.w*m_Alpha;
    if (use.w < .1f) {
        discard;
    }
    gl_FragColor = use;
}
