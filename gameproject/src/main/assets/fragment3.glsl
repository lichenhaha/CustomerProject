precision mediump float;
varying vec4 aCoordinate;
uniform sampler2D vTexture;
void main() {
    gl_FragColor = texture2D(vTexture,aCoordinate);
}
