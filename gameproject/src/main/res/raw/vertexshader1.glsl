attribute vec4 vPosition;
varying vec2 vCoordinate;
uniform mat4 vMatrix;
attribute vec2 aCoordinate;
void main() {
    gl_Position = vMatrix*vPosition;
    vCoordinate = aCoordinate;
}
