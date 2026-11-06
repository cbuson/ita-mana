import br.org.itaarandu.itamana.ProjectLinks;
public class ProjectLinksCheck {
    public static void main(String[] args){
        String[] keys={"ita","pag","pih"};String[] paths={"atlas-geocientifico-ms","pag-etr-ms","pih-ms"};
        for(int i=0;i<keys.length;i++)if(!("https://cbuson.github.io/"+paths[i]+"/").equals(ProjectLinks.forKey(keys[i])))throw new AssertionError(keys[i]);
        if(!"https://doi.org/10.5281/zenodo.24012745".equals(ProjectLinks.forKey("doi")))throw new AssertionError("doi");
        for(String key:new String[]{null,"","javascript:alert(1)","https://example.com","../ita","file:///etc/passwd"})if(ProjectLinks.forKey(key)!=null)throw new AssertionError(key);
        System.out.println("PASS: only the three project destinations and the fixed Zenodo DOI can be opened through the native bridge.");
    }
}
