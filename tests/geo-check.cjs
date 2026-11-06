const assert=require('node:assert/strict'),G=require('../logic/geo-core.cjs'),d=Math.PI/180;
const near=(a,b)=>assert(Math.abs(a-b)<1e-5,`${a} != ${b}`);
function plane(az,dip,spin=0,flipped=false){const a=az*d,b=dip*d,t=spin*d,n=[Math.sin(b)*Math.sin(a),Math.sin(b)*Math.cos(a),Math.cos(b)],x=[Math.cos(a),-Math.sin(a),0],y=[Math.cos(b)*Math.sin(a),Math.cos(b)*Math.cos(a),-Math.sin(b)],xx=x.map((v,i)=>v*Math.cos(t)+y[i]*Math.sin(t)),yy=y.map((v,i)=>v*Math.cos(t)-x[i]*Math.sin(t));return [xx[0]*(flipped?-1:1),yy[0],n[0]*(flipped?-1:1),xx[1]*(flipped?-1:1),yy[1],n[1]*(flipped?-1:1),xx[2]*(flipped?-1:1),yy[2],n[2]*(flipped?-1:1)]}
for(const az of [0,1,90,120,180,270,359])for(const dip of [10,35,70])for(const spin of [0,45,90,180,270])for(const flip of [false,true]){const q=G.measure(plane(az,dip,spin,flip),true);near(q.dip,dip);near(q.dipDirection,az);near(q.strikeRHR,G.wrap(az-90));}
let q=G.measure(plane(120,35),true);near(q.edgeInclination,-35);near(q.trend,120);near(q.plunge,35);q=G.measure(plane(120,35,180),true);near(q.trend,120);near(q.plunge,35);
q=G.measure(plane(40,0),true);assert.equal(q.dipDirection,null);assert.equal(q.planeAmbiguity,'horizontal');assert.equal(q.trend,null);assert.equal(q.lineAmbiguity,'horizontal');
q=G.measure(plane(90,90),true);assert.equal(q.dipDirection,null);assert.equal(q.strikeRHR,null);near(q.strikeAxis,0);assert.equal(q.trend,null);assert.equal(q.lineAmbiguity,'vertical');assert.equal(q.percent,null);
q=G.measure(plane(120,35),false);near(q.dip,35);assert.equal(q.dipDirection,null);assert.equal(q.edgeHeading,null);near(q.edgeInclination,-35);
assert.equal(G.measure(null,true),null);assert.equal(G.measure(Array(9).fill(0),true),null);assert.equal(G.measure([1,0,0,0,1,0,0,0,NaN],true),null);
console.log('PASS: plane normal, quadrants, in-plane rotation, flipped face, line reversal, singularities, no north and invalid input');
