import br.org.itaarandu.itamana.AppPolicy;
public class AppPolicyCheck {
 public static void main(String[] args){
  if(!AppPolicy.trustedDocument("https://appassets.androidplatform.net/assets/index.html"))throw new AssertionError();
  for(String s:new String[]{null,"https://appassets.androidplatform.net.evil.org/assets/index.html","https://evil.org/","file:///etc/passwd","content://media/external/1","https://appassets.androidplatform.net/assets/index.html?x=1","https://user@appassets.androidplatform.net/assets/index.html","https://appassets.androidplatform.net/assets/../index.html"})if(AppPolicy.trustedDocument(s))throw new AssertionError(s);
  if(!AppPolicy.fileName("visada-123.jpg",".jpg")||!AppPolicy.fileName("itamana.json",".json"))throw new AssertionError();
  for(String s:new String[]{null,"../secret.json","/tmp/data.json","a/b.json",".json","file.apk","a\\b.json","bad\n.json"})if(AppPolicy.fileName(s,".json"))throw new AssertionError(s);
  if(!AppPolicy.jpeg(new byte[]{(byte)255,(byte)216,(byte)255,0})||AppPolicy.jpeg(new byte[]{1,2,3,4}))throw new AssertionError();
  if(!AppPolicy.language("pt")||AppPolicy.language("xx")||!AppPolicy.orientation("landscape")||AppPolicy.orientation("file:///"))throw new AssertionError();
  System.out.println("PASS: exact document, origin/path confusion, export filenames, payload limits and locale/orientation allowlists.");
 }
}
