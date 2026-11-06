// SPDX-License-Identifier: MIT
// Copyright (c) 2026 Carlos Busón Buesa
package br.org.itaarandu.itamana;

public final class AppPolicy {
    public static final String ORIGIN="https://appassets.androidplatform.net";
    public static final String DOCUMENT=ORIGIN+"/assets/index.html";
    public static final int MAX_MESSAGE=18*1024*1024;
    public static final int MAX_TEXT=12*1024*1024;
    public static final int MAX_JPEG=8*1024*1024;
    private AppPolicy(){}
    public static boolean trustedDocument(String url){return DOCUMENT.equals(url);}
    public static boolean trustedOrigin(String origin){return ORIGIN.equals(origin)||(ORIGIN+"/").equals(origin);}
    public static boolean language(String lang){return "es".equals(lang)||"en".equals(lang)||"pt".equals(lang);}
    public static boolean sensorMode(String mode){return "magnetic".equals(mode)||"relative".equals(mode);}
    public static boolean orientation(String mode){return "auto".equals(mode)||"portrait".equals(mode)||"landscape".equals(mode);}
    public static boolean fileName(String name,String extension){return name!=null&&name.length()<=100&&!name.startsWith(".")&&name.matches("[A-Za-z0-9_-][A-Za-z0-9_.-]*")&&name.endsWith(extension);}
    public static boolean jpeg(byte[] data){return data!=null&&data.length>=4&&data.length<=MAX_JPEG&&(data[0]&255)==255&&(data[1]&255)==216&&(data[2]&255)==255;}
}
