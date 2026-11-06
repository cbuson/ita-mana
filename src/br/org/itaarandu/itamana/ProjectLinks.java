// SPDX-License-Identifier: MIT
// Copyright (c) 2026 Carlos Busón Buesa
package br.org.itaarandu.itamana;

public final class ProjectLinks {
    private ProjectLinks() {}
    public static String forKey(String key){
        if("ita".equals(key))return "https://cbuson.github.io/atlas-geocientifico-ms/";
        if("pag".equals(key))return "https://cbuson.github.io/pag-etr-ms/";
        if("pih".equals(key))return "https://cbuson.github.io/pih-ms/";
        if("doi".equals(key))return "https://doi.org/10.5281/zenodo.24012745";
        return null;
    }
}
