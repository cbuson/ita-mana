// SPDX-License-Identifier: MIT
// Copyright (c) 2026 Carlos Busón Buesa
const TripodMath=(()=>{
 const deltaH=(a,b)=>Number.isFinite(a)&&Number.isFinite(b)?((b-a+540)%360+360)%360-180:null;
 function differences(a,b){if(!a||!b||a.session!==b.session||a.sensorMode!==b.sensorMode||a.source!==b.source)return null;const h=deltaH(a.rawHeading,b.rawHeading),v=b.verticalAngle-a.verticalAngle;if(h===null||!Number.isFinite(v))return null;return {horizontal:h,vertical:v,units:'degrees',horizontalConvention:'shortest signed arc [-180,180)',referenceTime:a.timestamp,currentTime:b.timestamp};}
 function stability(samples,now){const recent=samples.filter(s=>now-s.time<=1700&&now>=s.time);const empty={stable:false,count:recent.length,durationMs:0,horizontalSpan:null,verticalSpan:null,lateralSpan:null};if(recent.length<8||now-recent[recent.length-1].time>500)return empty;const first=recent[0],duration=recent[recent.length-1].time-first.time;const hs=recent.map(s=>deltaH(first.h,s.h)),vs=recent.map(s=>s.v),rs=recent.map(s=>s.r);if(hs.some(x=>x===null)||vs.some(x=>!Number.isFinite(x))||rs.some(x=>!Number.isFinite(x)))return empty;const span=x=>Math.max(...x)-Math.min(...x),h=span(hs),v=span(vs),r=span(rs);return {stable:duration>=1200&&h<=.35&&v<=.35&&r<=.5,count:recent.length,durationMs:duration,horizontalSpan:h,verticalSpan:v,lateralSpan:r,thresholds:{minimumWindowMs:1200,maxHorizontalSpan:.35,maxVerticalSpan:.35,maxLateralSpan:.5},note:'Heuristic stillness indicator, not measurement accuracy'};}
 return Object.freeze({deltaH,differences,stability});
})();
if(typeof module!=='undefined'&&module.exports)module.exports=TripodMath;
