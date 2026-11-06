const fs=require('node:fs'),vm=require('node:vm'),assert=require('node:assert/strict'),crypto=require('node:crypto');
const html=fs.readFileSync('assets/index.html','utf8'),source=html.match(/<script id="app-code">([\s\S]*?)<\/script>/)[1],nodes={},saved=[];const classes=()=>({toggle(){},contains(){return false}});
function node(id){return nodes[id]??={value:id==='offset'?'0':'',style:{},dataset:{},classList:classes(),setAttribute(){},toggleAttribute(k,v){this[k]=v},replaceChildren(){},append(){},showModal(){this.open=true},close(){this.open=false},focus(){}}}
const canvas={width:0,height:0,getContext:()=>new Proxy({},{get:()=>()=>{}}),toDataURL:()=> 'data:image/jpeg;base64,/9j/2Q=='};
const ctx={document:{getElementById:node,documentElement:{},body:{classList:classes()},querySelectorAll:()=>[],createElement:tag=>tag==='canvas'?canvas:node(Math.random())},navigator:{},window:{addEventListener(){},removeEventListener(){}},screen:{},indexedDB:{open(){throw Error('blocked')}},localStorage:{getItem(){return null},setItem(){}},AndroidLocal:{setLanguage(){},startSensors(){},stopSensors(){},saveText:(text)=>saved.push(JSON.parse(text)),exitApp(){ctx.exited=true}},setInterval(){},clearInterval(){},setTimeout(){},clearTimeout(){},Date,Math,Number,Map,crypto:crypto.webcrypto,console};
vm.createContext(ctx);vm.runInContext(source,ctx);

(async()=>{
 const run=code=>vm.runInContext(code,ctx);
 await run("nbEditStation()");node('nbf_code').value='EST-001';node('nbf_name').value='<outcrop>';node('nbf_observer').value='Student';await run('nbSaveStation()');
 assert.equal((await run('allStations()')).length,1);assert.equal((await run('allRecords()')).length,0);
 await run("saveRecord({id:'legacy',createdAt:new Date().toISOString(),note:'observation',azimuth:359,photo:null})");let records=await run('allRecords()');assert.equal(records[0].stationId,(await run('allStations()'))[0].id);
 await run("nbEditRecord('legacy')");node('nbf_element').value='stratification';node('nbf_note').value='observed';node('changeReason').value='Add context';await run('nbSaveEntry()');records=await run('allRecords()');assert.equal(records.length,1);assert.equal(records[0].history[0].previous.azimuth,359);assert.equal(records[0].field.element,'stratification');
 await run("nbEditRecord(null,'sample')");node('nbf_sampleCode').value='S-001';await run('nbSaveEntry()');assert.equal((await run('allRecords()')).length,2);
 let bundle=await run('nbBundle()');assert.equal(bundle.stations.length,1);assert.equal(bundle.records.length,2);
 assert.equal(run("nbStrip({position:{latitude:20},reading:{position:{longitude:3}},photo:'jpeg'}).photo"),undefined);
 assert.equal(run("nbGeoJSON({stations:[{id:'s',position:{latitude:-20,longitude:-54}}],records:[]}).features[0].geometry.coordinates[0]"),-54);
 assert.throws(()=>run("nbValidateImport({tool:'Other',records:[]})"));assert.throws(()=>run("nbValidateImport({tool:'ITA MAÑA',records:[{id:'x',createdAt:'bad'}]})"));
 const before=await run('allEntries()');ctx.payload=JSON.stringify(bundle);run('window.onNotebookImport(payload)');await run('nbImport()');assert.equal((await run('allStations()')).length,2);assert.equal((await run('allRecords()')).length,4);assert.equal(new Set((await run('allEntries()')).map(x=>x.id)).size,6);
 const imported=(await run('allRecords()')).find(x=>x.importOriginalId==='legacy');assert.notEqual(imported.stationId,records[0].stationId);assert.equal(imported.history[0].previous.azimuth,359);
 assert(run("nbCSV({stations:[],records:[{id:'=SUM(1)',createdAt:'x'}]})").includes("' =SUM(1)"));

 ctx.Blob=Blob;ctx.atob=x=>Buffer.from(x,'base64').toString('binary');ctx.URL={createObjectURL:()=> 'blob:test',revokeObjectURL(){}};
 ctx.FileReader=class{readAsDataURL(blob){blob.arrayBuffer().then(bytes=>{this.result='data:'+blob.type+';base64,'+Buffer.from(bytes).toString('base64');this.onload()})}};
 let stopped=0;ctx.navigator.mediaDevices={getUserMedia:async()=>({getTracks:()=>[{stop(){stopped++}}]})};
 ctx.MediaRecorder=class{static isTypeSupported(){return true}constructor(stream,options){this.state='inactive';this.options=options}start(){this.state='recording'}stop(){this.state='inactive';this.ondataavailable({data:new Blob([new Uint8Array([26,69,223,163,1,2,3,4])])});this.onstop()}};
 await run('nbEditRecord()');node('nbf_note').value='';await run("nbRecordMedia('audio')");assert.equal(node('saveEntry').disabled,true);run('nbStopRecording()');await new Promise(setImmediate);assert(stopped>0);assert.equal(node('saveEntry').disabled,false);assert.equal(run('nbMediaDraft.kind'),'audio');assert.equal(run('nbValidAttachment(nbMediaDraft)'),true);
 await run('nbSaveEntry()');const withAudio=(await run('allRecords()')).find(r=>r.attachment);assert.equal(withAudio.attachment.mime,'audio/webm');assert.equal(withAudio.note,'');
 assert.equal(run("nbStrip({attachment:{data:'sensitive'},history:[{previous:{attachment:{data:'old'}}}]}).history[0].previous.attachment"),undefined);
 assert.equal(run("nbValidAttachment({kind:'audio',mime:'text/html',data:'data:text/html;base64,WA==',bytes:1})"),false);
 ctx.AndroidLocal.requestMicrophone=()=>ctx.window.onMicrophonePermission(false);await run('nbEditRecord()');await run("nbRecordMedia('audio')");assert.equal(run('nbRecorder'),null);assert.equal(node('saveEntry').disabled,false);assert(node('entryError').textContent.includes('Micrófono'));
 console.log('PASS: station creation, shared measurements, revision preservation, samples, privacy, GeoJSON, validated import, remapped links and CSV formula protection');
})().catch(e=>{console.error(e);process.exitCode=1});
