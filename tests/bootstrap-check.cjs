const fs=require('node:fs'),vm=require('node:vm'),assert=require('node:assert/strict');
const html=fs.readFileSync('assets/index.html','utf8');const source=html.match(/<script id="native-bootstrap">([\s\S]*?)<\/script>/)[1];
function setup(origin,embedded=false){const listeners={};const win={addEventListener:(name,fn)=>listeners[name]=fn};win.top=embedded?{}:win;const ctx={window:win,location:{origin},Object,JSON};vm.createContext(ctx);vm.runInContext(source,ctx);return {win,listeners,ctx}}
assert.equal(setup('https://evil.example').win.AndroidLocal,undefined);assert.equal(setup('https://appassets.androidplatform.net',true).win.AndroidLocal,undefined);
const good=setup('https://appassets.androidplatform.net');assert(Object.isFrozen(good.win.AndroidLocal));assert.equal(Object.getOwnPropertyDescriptor(good.win,'AndroidLocal').writable,false);
good.win.AndroidLocal.setLanguage('pt');const messages=[];const port={start(){},postMessage:s=>messages.push(JSON.parse(s))};
good.listeners.message({data:'wrong-protocol',ports:[port]});assert.equal(messages.length,0);
good.listeners.message({data:'ITA_MANA_PORT_V1',ports:[port]});assert.equal(messages.length,1);assert.equal(messages[0].method,'setLanguage');assert.deepEqual(messages[0].args,['pt']);
good.win.AndroidLocal.exitApp();assert.equal(messages[1].method,'exitApp');
let hijacked=false;good.listeners.message({data:'ITA_MANA_PORT_V1',ports:[{start(){hijacked=true},postMessage(){}}]});assert.equal(hijacked,false);
console.log('PASS: native facade restricted to app origin and top frame, immutable methods, deferred commands and single port handshake.');
