const fs=require('node:fs'),vm=require('node:vm'),assert=require('node:assert/strict');
const source=fs.readFileSync('assets/index.html','utf8').match(/<script id="app-code">([\s\S]*?)<\/script>/)[1];
const nodes={},calls={};
function classes(){const s=new Set();return {toggle(k,force){if(force===undefined)force=!s.has(k);force?s.add(k):s.delete(k)},contains:k=>s.has(k)}}
function node(id){return nodes[id]??={value:id==='offset'?'0':'',textContent:'',style:{},dataset:{},setAttribute(k,v){this[k]=v},toggleAttribute(k,v){if(k==='hidden')this.hidden=v},classList:classes(),hidden:false,disabled:true,replaceChildren(){},append(){},showModal(){this.open=true},close(){this.open=false},focus(){}}}
const bridge={};for(const key of ['setLanguage','setOrientation','setImmersive','startSensors','stopSensors','exitApp','saveText','savePhoto'])bridge[key]=value=>(calls[key]??=[]).push(value);
const context={document:{getElementById:node,documentElement:{},body:{classList:classes()},querySelectorAll:()=>[],createElement:()=>node('generated')},navigator:{},window:{addEventListener(){},removeEventListener(){}},screen:{},indexedDB:{open(){throw Error('Storage denied')}},localStorage:{getItem(){throw Error('Storage denied')},setItem(){}},AndroidLocal:bridge,setInterval(){},setTimeout(){return 1},clearTimeout(){},Date,Math,Number,Map,console};
vm.createContext(context);vm.runInContext(source,context);
(async()=>{
 assert.equal(nodes.grid.hidden,true);nodes.gridtoggle.onclick();assert.equal(nodes.grid.hidden,false);assert.equal(nodes.gridtoggle['aria-pressed'],'true');nodes.gridtoggle.onclick();assert.equal(nodes.grid.hidden,true);
 await nodes.start.onclick();assert.equal(calls.startSensors.length,1);
 context.window.onNativePose(123,12,-8,2,true);assert.equal(nodes.az.textContent,'123.0°');assert.equal(nodes.pitch.textContent,'12.0°');assert.equal(nodes.hudAz.textContent,'123.0°');assert.equal(nodes.hudPitch.textContent,'12.0°');assert.equal(nodes.moving.hidden,false);assert.equal(nodes.moving.style.transform,'rotate(-8deg)');
 context.window.onNativePose(123,0,.5,3,true);assert(nodes.moving.classList.contains('aligned'));
 context.window.onNativePose(null,90,null,3,true);assert.equal(nodes.az.textContent,'—');assert.equal(nodes.moving.hidden,true);
 vm.runInContext('state.poseTime=Date.now()-5000;draw()',context);assert.equal(nodes.pitch.textContent,'—');
 nodes.language.value='en';nodes.language.onchange();assert(nodes.helpContent.innerHTML.includes('First use'));assert.equal(calls.setLanguage.at(-1),'en');nodes.language.value='pt';nodes.language.onchange();assert(nodes.helpContent.innerHTML.includes('Primeiro uso'));assert.equal(calls.setLanguage.at(-1),'pt');nodes.language.value='en';nodes.language.onchange();
 nodes.rotate.onclick();nodes.rotate.onclick();nodes.rotate.onclick();assert.deepEqual(calls.setOrientation,['portrait','landscape','auto']);
 nodes.focus.onclick();assert.equal(context.document.body.classList.contains('hideui'),true);nodes.exitfocus.onclick();assert.equal(context.document.body.classList.contains('hideui'),false);assert.deepEqual(calls.setImmersive,[true,false]);
 nodes.stop.onclick();assert.equal(calls.stopSensors.length,1);assert.equal(nodes.moving.hidden,true);
 nodes.export.onclick();assert.equal(nodes.exportDialog.open,true);assert.equal(calls.saveText,undefined);nodes.exit.onclick();assert.equal(calls.exitApp.length,1);
 console.log('PASS: blocked storage, native pose, dual-level alignment, undefined and stale readings, ES/EN/PT help, export review, exit, rotation, full-screen return, grid toggle without storage and stop.');
})().catch(e=>{console.error(e);process.exitCode=1});
