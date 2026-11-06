// SPDX-License-Identifier: MIT
// Copyright (c) 2026 Carlos Busón Buesa
const GeoMath=(()=>{
 const clamp=x=>Math.max(-1,Math.min(1,x)),wrap=x=>(x%360+360)%360,deg=180/Math.PI;
 // Matrix maps physical device x/right, y/top, z/out of screen to East/North/Up.
 // Screen rotation is deliberately not used: the contact axes remain physical.
 function measure(m,northKnown){
  if(!Array.isArray(m)||m.length!==9||m.some(x=>!Number.isFinite(x)))return null;
  const g=Math.hypot(m[6],m[7],m[8]);if(g<.8||g>1.2)return null;
  const up=m[8]/g,dip=Math.acos(clamp(Math.abs(up)))*deg,edge=Math.asin(clamp(m[7]/g))*deg;
  const horizontal=dip<.5,vertical=dip>89.5,s=up<0?-1:1;
  const planeValid=northKnown&&!horizontal&&!vertical;
  const dd=planeValid?wrap(Math.atan2(s*m[2],s*m[5])*deg):null;
  const strike=northKnown&&!horizontal?wrap(Math.atan2(s*m[2],s*m[5])*deg-90):null;
  const yHorizontal=Math.hypot(m[1],m[4]),heading=northKnown&&yHorizontal>.08?wrap(Math.atan2(m[1],m[4])*deg):null;
  const lineHorizontal=Math.abs(edge)<.5,lineVertical=Math.abs(edge)>89.5;
  const trend=northKnown&&!lineHorizontal&&!lineVertical&&yHorizontal>.0087?wrap(Math.atan2((edge>0?-1:1)*m[1],(edge>0?-1:1)*m[4])*deg):null;
  return {dip,dipDirection:dd,strikeRHR:planeValid?strike:null,strikeAxis:northKnown&&!horizontal?strike%180:null,planeAmbiguity:horizontal?'horizontal':vertical?'vertical':null,edgeInclination:edge,edgeHeading:heading,plunge:Math.abs(edge),trend,lineAxis:northKnown&&!lineVertical&&yHorizontal>.0087?wrap(Math.atan2(m[1],m[4])*deg)%180:null,lineAmbiguity:lineHorizontal?'horizontal':lineVertical?'vertical':null,screenTilt:dip,percent:Math.abs(edge)>=89.5?null:100*Math.tan(edge/deg),northKnown:!!northKnown,physicalAxis:'device +Y: bottom to original top/earpiece; plane parallel to screen',singularityThresholdDegrees:.5};
 }
 return Object.freeze({measure,wrap});
})();
if(typeof module!=='undefined'&&module.exports)module.exports=GeoMath;
