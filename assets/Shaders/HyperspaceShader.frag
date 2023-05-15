
uniform sampler2D m_Texture;
uniform vec2 m_Offset1;
uniform vec2 m_Offset2;
varying vec3 texCoord;

void main() {
    vec2 uv1 = vec2(texCoord.x+m_Offset1.x, texCoord.y*0.25+m_Offset1.y);
    vec2 uv2 = vec2(texCoord.x+m_Offset2.x, texCoord.y+m_Offset2.y);
    vec4 clrA = texture2D(m_Texture, uv1)*0.2;
    vec4 clrB = texture2D(m_Texture, uv2)*0.4;
    float brightness1 = clrA.x+clrA.y+clrA.z+clrA.w;
    float brightness2 = clrB.x+clrB.y+clrB.z+clrB.w;
    float factor = min(0.5, texCoord.y+3.0);
    if (brightness1 > brightness2) {
        gl_FragColor = clrA;
    }
    else {
        gl_FragColor = clrB;
    }
}
