//#extension GL_OES_EGL_image_external : require
precision mediump float;
varying vec2 v_texCoord;
//uniform samplerExternalOES  u_samplerTexture;
uniform sampler2D  u_samplerTexture;

void main()
{
  gl_FragColor = texture2D(u_samplerTexture, v_texCoord);
}
