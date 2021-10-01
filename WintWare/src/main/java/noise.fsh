#ifdef GL_ES
precision mediump float;
#endif

#extension GL_OES_standard_derivatives : enable

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;

void main( void ) {

    vec2 p = ( gl_FragCoord.xy / resolution.xy );

    p *= 4.0;

    float color = 80.0;

    if(-2.0+p.x>sin( p.y+time*1.0)*.15*sin(time*1.)*.8 && (-7.0+p.x<sin( p.y +time*0.)*.4*sin(time*1.)*.8))
    gl_FragColor =  vec4( vec3( 2.8*p.y/8.0, 0.0 , p.x+sin( color + time / 1.0 ) * 1.0 ), 5.0 );

    else

    gl_FragColor = vec4( vec3( 1.0,  1.0, 1.0 ), 1.0 );

}