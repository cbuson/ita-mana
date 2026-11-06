// SPDX-License-Identifier: MIT
package br.org.itaarandu.itamana;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.util.Base64;
import org.json.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.*;
final class NotebookFiles {
 private NotebookFiles(){}
 static byte[] archive(JSONObject book)throws Exception{
  ByteArrayOutputStream out=new ByteArrayOutputStream();
  try(ZipOutputStream zip=new ZipOutputStream(out)){
   zip.putNextEntry(new ZipEntry("notebook.json"));zip.write(book.toString(2).getBytes(StandardCharsets.UTF_8));zip.closeEntry();
   JSONArray records=book.getJSONArray("records");
   for(int i=0;i<records.length();i++){JSONObject clip=records.getJSONObject(i).optJSONObject("attachment");if(clip==null)continue;String mime=clip.optString("mime"),data=clip.optString("data");if(!java.util.Arrays.asList("audio/webm","audio/mp4","video/webm","video/mp4").contains(mime)||!data.startsWith("data:"+mime+";base64,"))continue;byte[] bytes=Base64.decode(data.substring(data.indexOf(',')+1),Base64.DEFAULT);if(bytes.length==0||bytes.length>6500000)continue;zip.putNextEntry(new ZipEntry("media/record-"+(i+1)+(mime.endsWith("mp4")?".mp4":".webm")));zip.write(bytes);zip.closeEntry();}

   for(int i=0;i<records.length();i++){String photo=records.getJSONObject(i).optString("photo","");if(!photo.startsWith("data:image/jpeg;base64,"))continue;byte[] jpg=Base64.decode(photo.substring(photo.indexOf(',')+1),Base64.DEFAULT);if(!AppPolicy.jpeg(jpg))continue;zip.putNextEntry(new ZipEntry("photos/record-"+(i+1)+".jpg"));zip.write(jpg);zip.closeEntry();}
  }return out.toByteArray();
 }
 static byte[] pdf(String text)throws IOException{
  ByteArrayOutputStream out=new ByteArrayOutputStream();PdfDocument doc=new PdfDocument();Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);paint.setTextSize(10);PdfDocument.Page page=null;int number=0,y=0;
  try{for(String raw:text.split("\\n",-1)){String line=raw.replace('\t',' ');do{if(page==null||y>790){if(page!=null)doc.finishPage(page);page=doc.startPage(new PdfDocument.PageInfo.Builder(595,842,++number).create());page.getCanvas().drawText("ITA MAÑA | "+number,30,25,paint);y=48;}int count=line.isEmpty()?0:paint.breakText(line,true,535,null);if(count==0&&!line.isEmpty())count=1;page.getCanvas().drawText(line.substring(0,count),30,y,paint);y+=14;line=line.substring(count);}while(!line.isEmpty());}if(page!=null)doc.finishPage(page);doc.writeTo(out);}finally{doc.close();}return out.toByteArray();
 }
}
