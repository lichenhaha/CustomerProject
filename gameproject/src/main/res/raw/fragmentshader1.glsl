precision mediump float;
varying vec2 vCoordinate;
uniform sampler2D vTexture;
//uniform vec4 v_Color;
void main()
{
  gl_FragColor = texture2D(vTexture, vCoordinate);
//    gl_FragColor = v_Color;
}
