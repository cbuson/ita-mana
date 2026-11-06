// SPDX-License-Identifier: MIT
// Copyright (c) 2026 Carlos Busón Buesa
package br.org.itaarandu.itamana;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.*;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Base64;
import android.view.*;
import android.webkit.*;
import android.widget.Toast;
import org.json.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

public class MainActivity extends Activity implements SensorEventListener {
    private WebView web;
    private WebMessagePort nativePort;
    private SensorManager sensors;
    private float[] gravity,magnetic;
    private boolean running=false,immersive=true,documentReady=false,foreground=false,exiting=false;
    private long lastPose=0;
    private int magneticAccuracy=SensorManager.SENSOR_STATUS_UNRELIABLE;
    private String language="es",sensorMode="magnetic";
    private byte[] pendingBytes;
    private boolean notebookExporting=false;
    private final ExecutorService io=Executors.newSingleThreadExecutor();

    @Override public void onCreate(Bundle saved){
        super.onCreate(saved);
        sensors=(SensorManager)getSystemService(SENSOR_SERVICE);
        WebView.setWebContentsDebuggingEnabled(false);
        web=new WebView(this);
        web.setOnApplyWindowInsetsListener((v,i)->{v.setPadding(i.getSystemWindowInsetLeft(),i.getSystemWindowInsetTop(),i.getSystemWindowInsetRight(),i.getSystemWindowInsetBottom());return i;});
        setContentView(web);setImmersiveMode(true);
        WebSettings settings=web.getSettings();
        settings.setJavaScriptEnabled(true);settings.setDomStorageEnabled(true);settings.setGeolocationEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);settings.setAllowFileAccess(false);settings.setAllowContentAccess(false);
        settings.setAllowFileAccessFromFileURLs(false);settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);settings.setSupportMultipleWindows(false);settings.setSaveFormData(false);
        settings.setBlockNetworkImage(true);
        CookieManager.getInstance().setAcceptCookie(false);CookieManager.getInstance().setAcceptThirdPartyCookies(web,false);
        web.setDownloadListener((url,userAgent,disposition,mime,length)->{});
        web.setWebViewClient(new WebViewClient(){
            @Override public WebResourceResponse shouldInterceptRequest(WebView view,WebResourceRequest request){
                if(request.isForMainFrame()&&"GET".equals(request.getMethod())&&AppPolicy.trustedDocument(request.getUrl().toString())){
                    try{String policy;try(InputStream in=getAssets().open("security-policy.txt")){ByteArrayOutputStream out=new ByteArrayOutputStream();byte[] b=new byte[2048];int n;while((n=in.read(b))!=-1)out.write(b,0,n);policy=new String(out.toByteArray(),StandardCharsets.UTF_8);}
                        Map<String,String> headers=new HashMap<>();headers.put("Content-Security-Policy",policy.trim());headers.put("X-Content-Type-Options","nosniff");headers.put("Cache-Control","no-store");headers.put("Referrer-Policy","no-referrer");
                        return new WebResourceResponse("text/html","UTF-8",200,"OK",headers,getAssets().open("index.html"));
                    }catch(IOException ignored){}
                }
                return new WebResourceResponse("text/plain","UTF-8",403,"Blocked",Collections.emptyMap(),new ByteArrayInputStream(new byte[0]));
            }
            @Override public boolean shouldOverrideUrlLoading(WebView view,WebResourceRequest request){return true;}
            @Override public void onPageStarted(WebView view,String url,android.graphics.Bitmap favicon){documentReady=false;closePort();}
            @Override public void onPageFinished(WebView view,String url){if(!AppPolicy.trustedDocument(url)||!AppPolicy.trustedDocument(view.getUrl())||exiting)return;documentReady=true;openPort();}
            @Override public void onReceivedSslError(WebView view,SslErrorHandler handler,SslError error){handler.cancel();}
            @Override public boolean onRenderProcessGone(WebView view,RenderProcessGoneDetail detail){releaseSensors();documentReady=false;closePort();view.destroy();web=null;finishAndRemoveTask();return true;}
        });
        web.setWebChromeClient(new WebChromeClient(){
            @Override public void onPermissionRequest(PermissionRequest request){runOnUiThread(()->{
                if(exiting||!foreground||!documentReady||!AppPolicy.trustedOrigin(request.getOrigin().toString())){request.deny();return;}
                ArrayList<String> allowed=new ArrayList<>();
                for(String resource:request.getResources()){if(PermissionRequest.RESOURCE_VIDEO_CAPTURE.equals(resource)&&checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED)allowed.add(resource);if(PermissionRequest.RESOURCE_AUDIO_CAPTURE.equals(resource)&&checkSelfPermission(Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED)allowed.add(resource);}
                if(allowed.isEmpty())request.deny();else request.grant(allowed.toArray(new String[0]));
            });}
            @Override public void onGeolocationPermissionsShowPrompt(String origin,GeolocationPermissions.Callback callback){
                boolean granted=checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED||checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED;
                callback.invoke(origin,!exiting&&foreground&&documentReady&&AppPolicy.trustedOrigin(origin)&&granted,false);
            }
        });
        requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},11);
    }
    @Override public void onRequestPermissionsResult(int code,String[] permissions,int[] results){super.onRequestPermissionsResult(code,permissions,results);if(code==11&&web!=null&&!exiting)web.loadUrl(AppPolicy.DOCUMENT);if(code==12&&web!=null&&!exiting)web.evaluateJavascript("window.onMicrophonePermission&&window.onMicrophonePermission("+(checkSelfPermission(Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED)+")",null);}
    private void openPort(){
        closePort();if(web==null||!documentReady||exiting)return;
        WebMessagePort[] channel=web.createWebMessageChannel();nativePort=channel[0];
        nativePort.setWebMessageCallback(new WebMessagePort.WebMessageCallback(){@Override public void onMessage(WebMessagePort port,WebMessage message){if(port!=nativePort||!foreground||!documentReady||exiting)return;dispatch(message.getData());}});
        web.postWebMessage(new WebMessage("ITA_MANA_PORT_V1",new WebMessagePort[]{channel[1]}),Uri.parse(AppPolicy.ORIGIN));
        web.evaluateJavascript("window.onNativeCapabilities&&window.onNativeCapabilities("+(sensors.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)!=null)+")",null);
    }
    private void closePort(){if(nativePort!=null){nativePort.close();nativePort=null;}}
    private void dispatch(String raw){
        if(raw==null||raw.length()>AppPolicy.MAX_MESSAGE)return;
        try{JSONObject call=new JSONObject(raw);Object methodObject=call.opt("method"),argsObject=call.opt("args");if(!(methodObject instanceof String)||!(argsObject instanceof JSONArray))return;
            String method=(String)methodObject;JSONArray args=(JSONArray)argsObject;
            if("requestMicrophone".equals(method)&&args.length()==0){if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED)web.evaluateJavascript("window.onMicrophonePermission&&window.onMicrophonePermission(true)",null);else requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},12);return;}
            if("importNotebook".equals(method)&&args.length()==0){if(pendingBytes!=null)return;Intent pick=new Intent(Intent.ACTION_OPEN_DOCUMENT);pick.addCategory(Intent.CATEGORY_OPENABLE);pick.setType("application/json");try{startActivityForResult(pick,21);}catch(Exception e){fileResult("error");}return;}
            if("exportNotebook".equals(method)&&args.length()==3&&args.get(0) instanceof String&&args.get(1) instanceof String&&args.get(2) instanceof String){
                String content=args.getString(0),name=args.getString(1),format=args.getString(2);if(content.length()>AppPolicy.MAX_TEXT){fileResult("large");return;}
                if(!Arrays.asList("csv","geojson","zip","pdf").contains(format)||!AppPolicy.fileName(name,"."+format))return;
                if(notebookExporting||pendingBytes!=null){fileResult("busy");return;}if("pdf".equals(format)&&content.length()>500000){fileResult("large");return;}notebookExporting=true;io.execute(()->{try{byte[] bytes;String mime;
                    if("zip".equals(format)){JSONObject book=new JSONObject(content);if(!"ITA MAÑA".equals(book.optString("tool"))||!(book.opt("records") instanceof JSONArray))return;bytes=NotebookFiles.archive(book);mime="application/zip";}
                    else if("pdf".equals(format)){bytes=NotebookFiles.pdf(content);mime="application/pdf";}
                    else{bytes=content.getBytes(StandardCharsets.UTF_8);mime="csv".equals(format)?"text/csv":"application/geo+json";}
                    if(bytes.length>AppPolicy.MAX_TEXT){runOnUiThread(()->fileResult("large"));return;}final byte[] resultBytes=bytes;final String resultMime=mime;runOnUiThread(()->{if(!exiting)prepareSave(resultBytes,name,resultMime);});
                }catch(Exception e){runOnUiThread(()->fileResult("error"));}finally{runOnUiThread(()->notebookExporting=false);}});return;
            }
            if("updateLocation".equals(method)&&running&&args.length()==3&&args.get(0) instanceof Number&&args.get(1) instanceof Number&&args.get(2) instanceof Number){
                double lat=args.getDouble(0),lon=args.getDouble(1),alt=args.getDouble(2);
                if(Double.isNaN(lat)||Double.isNaN(lon)||Double.isNaN(alt)||Math.abs(lat)>90||Math.abs(lon)>180||Math.abs(alt)>100000)return;
                float decl=new GeomagneticField((float)lat,(float)lon,(float)alt,System.currentTimeMillis()).getDeclination();
                if(!Float.isNaN(decl)&&!Float.isInfinite(decl))web.evaluateJavascript("window.onNativeDeclination&&window.onNativeDeclination("+Float.toString(decl)+")",null);return;
            }
            if("startSensors".equals(method)&&args.length()==0){startSensors();return;}
            if("stopSensors".equals(method)&&args.length()==0){releaseSensors();return;}
            if("exitApp".equals(method)&&args.length()==0){exitApp();return;}
            if("setLanguage".equals(method)&&oneString(args)){String lang=args.getString(0);if(AppPolicy.language(lang))language=lang;return;}
            if("setSensorMode".equals(method)&&oneString(args)){String mode=args.getString(0);if(!AppPolicy.sensorMode(mode)||("relative".equals(mode)&&sensors.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)==null))return;releaseSensors();sensorMode=mode;return;}
            if("setOrientation".equals(method)&&oneString(args)){String mode=args.getString(0);if(!AppPolicy.orientation(mode))return;setRequestedOrientation("portrait".equals(mode)?ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT:"landscape".equals(mode)?ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE:ActivityInfo.SCREEN_ORIENTATION_USER);return;}
            if("setImmersive".equals(method)&&args.length()==1&&args.get(0) instanceof Boolean){setImmersiveMode(args.getBoolean(0));return;}
            if("openProject".equals(method)&&oneString(args)){String url=ProjectLinks.forKey(args.getString(0));if(url!=null){try{startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));}catch(Exception e){toast("No hay navegador disponible.","No browser available.","Não há navegador disponível.");}}return;}
            if(("saveText".equals(method)||"savePhoto".equals(method))&&args.length()==2&&args.get(0) instanceof String&&args.get(1) instanceof String){
                String data=args.getString(0),name=args.getString(1);if(data.length()>AppPolicy.MAX_TEXT){fileResult("large");return;}
                if("saveText".equals(method)){
                    if(!AppPolicy.fileName(name,".json")){fileResult("error");return;}
                    JSONObject export=new JSONObject(data);if(!"ITA MAÑA".equals(export.optString("tool"))||!(export.opt("records") instanceof JSONArray)){fileResult("error");return;}
                    prepareSave(data.getBytes(StandardCharsets.UTF_8),name,"application/json");
                }else{
                    if(!AppPolicy.fileName(name,".jpg")||!data.startsWith("data:image/jpeg;base64,")){fileResult("error");return;}
                    byte[] bytes=Base64.decode(data.substring(data.indexOf(',')+1),Base64.DEFAULT);if(!AppPolicy.jpeg(bytes)){fileResult("error");return;}prepareSave(bytes,name,"image/jpeg");
                }
            }
        }catch(JSONException|IllegalArgumentException ignored){}
    }
    private boolean oneString(JSONArray args)throws JSONException{return args.length()==1&&args.get(0) instanceof String;}
    private void startSensors(){
        if(running||!foreground||exiting)return;running=true;gravity=null;magnetic=null;magneticAccuracy=SensorManager.SENSOR_STATUS_UNRELIABLE;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Sensor rotation=sensors.getDefaultSensor("relative".equals(sensorMode)?Sensor.TYPE_GAME_ROTATION_VECTOR:Sensor.TYPE_ROTATION_VECTOR);
        if(rotation!=null)sensors.registerListener(this,rotation,SensorManager.SENSOR_DELAY_UI);
        else{Sensor accel=sensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),mag=sensors.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);if(accel!=null)sensors.registerListener(this,accel,SensorManager.SENSOR_DELAY_UI);if(mag!=null)sensors.registerListener(this,mag,SensorManager.SENSOR_DELAY_UI);}
    }
    private void releaseSensors(){running=false;if(sensors!=null)sensors.unregisterListener(this);getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);gravity=null;magnetic=null;}
    private void exitApp(){exiting=true;releaseSensors();documentReady=false;closePort();pendingBytes=null;if(web!=null){web.stopLoading();web.onPause();web.destroy();web=null;}finishAndRemoveTask();}
    private void prepareSave(byte[] bytes,String name,String mime){
        if(pendingBytes!=null){fileResult("busy");return;}pendingBytes=bytes;
        Intent intent=new Intent(Intent.ACTION_CREATE_DOCUMENT);intent.addCategory(Intent.CATEGORY_OPENABLE);intent.setType(mime);intent.putExtra(Intent.EXTRA_TITLE,name);
        try{startActivityForResult(intent,20);}catch(Exception e){pendingBytes=null;fileResult("error");}
    }
    @Override protected void onActivityResult(int request,int result,Intent data){super.onActivityResult(request,result,data);
        if(request==21){if(result!=RESULT_OK||data==null||data.getData()==null)return;final Uri selected=data.getData();if(!"content".equals(selected.getScheme()))return;io.execute(()->{try(InputStream in=getContentResolver().openInputStream(selected)){if(in==null)throw new IOException();ByteArrayOutputStream out=new ByteArrayOutputStream();byte[] chunk=new byte[8192];int n;while((n=in.read(chunk))!=-1){if(out.size()+n>AppPolicy.MAX_TEXT)throw new IOException();out.write(chunk,0,n);}String raw=new String(out.toByteArray(),StandardCharsets.UTF_8);new JSONObject(raw);runOnUiThread(()->{if(web!=null&&!exiting&&documentReady)web.evaluateJavascript("window.onNotebookImport&&window.onNotebookImport("+JSONObject.quote(raw)+")",null);});}catch(Exception e){runOnUiThread(()->fileResult("error"));}});return;}if(request!=20)return;
        final byte[] bytes=pendingBytes;pendingBytes=null;
        if(result!=RESULT_OK||data==null||data.getData()==null){fileResult("cancelled");return;}
        final Uri uri=data.getData();if(bytes==null||!"content".equals(uri.getScheme())){fileResult("error");return;}
        io.execute(()->{try(OutputStream out=getContentResolver().openOutputStream(uri)){if(out==null)throw new IOException();out.write(bytes);runOnUiThread(()->fileResult("saved"));}catch(IOException|SecurityException e){runOnUiThread(()->fileResult("error"));}});
    }
    @Override public void onSensorChanged(SensorEvent event){
        if(!running||!foreground||web==null||exiting)return;
        float[] matrix=new float[9];boolean ok=false,northKnown=true;int quality=event.accuracy;
        boolean relative=event.sensor.getType()==Sensor.TYPE_GAME_ROTATION_VECTOR;
        if(event.sensor.getType()==Sensor.TYPE_ROTATION_VECTOR||relative){SensorManager.getRotationMatrixFromVector(matrix,event.values);ok=true;if(relative){northKnown=false;quality=-1;}}
        else{if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)gravity=event.values.clone();if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){magnetic=event.values.clone();magneticAccuracy=event.accuracy;}
            if(gravity!=null&&magnetic!=null){ok=SensorManager.getRotationMatrix(matrix,null,gravity,magnetic);quality=magneticAccuracy;}
            else if(gravity!=null){double length=Math.sqrt(gravity[0]*gravity[0]+gravity[1]*gravity[1]+gravity[2]*gravity[2]);if(length>0){matrix[6]=(float)(gravity[0]/length);matrix[7]=(float)(gravity[1]/length);matrix[8]=(float)(gravity[2]/length);ok=true;northKnown=false;}}}
        if(!ok||System.currentTimeMillis()-lastPose<100)return;lastPose=System.currentTimeMillis();
        double[] angles=LevelMath.angles(matrix,getWindowManager().getDefaultDisplay().getRotation(),northKnown||relative);
        String az=Double.isNaN(angles[0])?"null":Double.toString(angles[0]),roll=Double.isNaN(angles[2])?"null":Double.toString(angles[2]);
        StringBuilder matrixJson=new StringBuilder("[");for(int i=0;i<9;i++){if(i>0)matrixJson.append(",");matrixJson.append(Float.isNaN(matrix[i])||Float.isInfinite(matrix[i])?"null":Float.toString(matrix[i]));}matrixJson.append("]");
        web.evaluateJavascript("window.onNativePose&&window.onNativePose("+az+","+angles[1]+","+roll+","+quality+","+northKnown+",\""+(relative?"relative":"magnetic")+"\","+matrixJson+","+getWindowManager().getDefaultDisplay().getRotation()+")",null);
    }
    @Override public void onAccuracyChanged(Sensor sensor,int accuracy){if(sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD)magneticAccuracy=accuracy;}
    private void setImmersiveMode(boolean enabled){immersive=enabled;
        if(android.os.Build.VERSION.SDK_INT>=30){WindowInsetsController controller=getWindow().getInsetsController();if(controller!=null){controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);if(enabled)controller.hide(WindowInsets.Type.systemBars());else controller.show(WindowInsets.Type.systemBars());}}
        else getWindow().getDecorView().setSystemUiVisibility(enabled?View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_LAYOUT_STABLE:View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if(web!=null)web.requestApplyInsets();
    }
    @Override public void onWindowFocusChanged(boolean focus){super.onWindowFocusChanged(focus);if(focus)setImmersiveMode(immersive);}
    @Override public void onConfigurationChanged(Configuration config){super.onConfigurationChanged(config);setImmersiveMode(immersive);if(web!=null)web.requestLayout();}
    @Override public void onBackPressed(){if(web!=null)web.evaluateJavascript("window.onNativeBack&&window.onNativeBack()",null);else finishAndRemoveTask();}
    private void fileResult(String result){if(exiting)return;if(web!=null)web.evaluateJavascript("window.onFileResult&&window.onFileResult('"+result+"')",null);
        if("saved".equals(result))toast("Archivo guardado.","File saved.","Arquivo salvo.");
        else if("cancelled".equals(result))toast("Guardado cancelado.","Save cancelled.","Salvamento cancelado.");
        else if("busy".equals(result))toast("Termina el guardado anterior.","Finish the previous save first.","Conclua o salvamento anterior.");
        else if("large".equals(result))toast("Archivo grande: guarda las fotos por separado.","File too large: save photos separately.","Arquivo grande: salve as fotos separadamente.");
        else toast("No se pudo guardar el archivo.","Could not save file.","Não foi possível salvar o arquivo.");
    }
    private void toast(String es,String en,String pt){Toast.makeText(this,"pt".equals(language)?pt:"en".equals(language)?en:es,Toast.LENGTH_LONG).show();}
    @Override protected void onResume(){super.onResume();foreground=true;if(web!=null)web.onResume();}
    @Override protected void onPause(){foreground=false;releaseSensors();if(web!=null){web.evaluateJavascript("window.pauseNotebookRecording&&window.pauseNotebookRecording();typeof stop==='function'&&stop()",null);web.onPause();}super.onPause();}
    @Override protected void onDestroy(){releaseSensors();documentReady=false;closePort();pendingBytes=null;io.shutdownNow();if(web!=null){web.destroy();web=null;}super.onDestroy();}
}
