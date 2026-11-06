const fs=require('node:fs'),vm=require('node:vm'),assert=require('node:assert/strict'),crypto=require('node:crypto');
const html=fs.readFileSync('assets/index.html','utf8'),source=html.match(/<script id="app-code">([\s\S]*?)<\/script>/)[1],nodes={},saved=[];const classes=()=>({toggle(){},contains(){return false}});
function node(id){return nodes[id]??={value:id==='offset'?'0':'',style:{},dataset:{},classList:classes(),setAttribute(){},toggleAttribute(k,v){this[k]=v},replaceChildren(){},append(){},showModal(){this.open=true},close(){this.open=false},focus(){}}}
const canvas={width:0,height:0,getContext:()=>new Proxy({},{get:()=>()=>{}}),toDataURL:()=> 'data:image/jpeg;base64,/9j/2Q=='};
const ctx={document:{getElementById:node,documentElement:{},body:{classList:classes()},querySelectorAll:()=>[],createElement:tag=>tag==='canvas'?canvas:node(Math.random())},navigator:{},window:{addEventListener(){},removeEventListener(){}},screen:{},indexedDB:{open(){throw Error('blocked')}},localStorage:{getItem(){return null},setItem(){}},AndroidLocal:{setLanguage(){},startSensors(){},stopSensors(){},saveText:(text)=>saved.push(JSON.parse(text)),exitApp(){ctx.exited=true}},setInterval(){},setTimeout(){},clearTimeout(){},Date,Math,Number,Map,crypto:crypto.webcrypto,console};
vm.createContext(ctx);vm.runInContext(source,ctx);
(async()=>{
 vm.runInContext("state.active=true;state.camera=true;state.position={latitude:1,longitude:2,accuracy:3,timestamp:new Date().toISOString()}",ctx);node('video').videoWidth=640;node('video').videoHeight=480;node('note').value='<img src=x onerror=alert(1)>';
 assert.equal(node('saveLocation').checked,false);await node('capture').onclick();
 let records=await vm.runInContext('allRecords()',ctx);assert.equal(records[0].position,null);assert.equal(records[0].note,'<img src=x onerror=alert(1)>');
 node('saveLocation').checked=true;node('saveLocation').onchange();await node('capture').onclick();records=await vm.runInContext('allRecords()',ctx);assert.equal(records[1].position.latitude,1);
 node('export').onclick();assert.equal(saved.length,0);assert(node('exportDialog').open);await node('confirmExport').onclick();assert.equal(saved.length,1);assert.equal(saved[0].records.length,2);
 node('exit').onclick();assert(node('exitDialog').open);assert.equal(ctx.exited,undefined);node('confirmExit').onclick();assert.equal(ctx.exited,true);
 for(const locale of ['es','en','pt']){node('language').value=locale;node('language').onchange();assert.equal(ctx.document.documentElement.lang,locale)}
 const keys=vm.runInContext('Object.keys(texts.es).sort().join()===Object.keys(texts.en).sort().join()&&Object.keys(texts.es).sort().join()===Object.keys(texts.pt).sort().join()',ctx);assert(keys);
 console.log('PASS: location excluded by default and included only on opt-in; export review; unsaved-record exit guard; ES/EN/PT coverage; notes remain data.');
})().catch(e=>{console.error(e);process.exitCode=1});
