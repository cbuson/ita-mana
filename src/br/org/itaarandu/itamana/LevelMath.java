// SPDX-License-Identifier: MIT
// Copyright (c) 2026 Carlos Busón Buesa
package br.org.itaarandu.itamana;

/** Camera optical axis and gravity projected onto the current screen. */
public final class LevelMath {
    private LevelMath() {}
    /** Returns azimuth, vertical angle, screen horizon rotation. NaN means undefined. */
    public static double[] angles(float[] matrix,int rotation,boolean northKnown){
        double east=-matrix[2],north=-matrix[5],up=-matrix[8];
        double az=!northKnown||Math.hypot(east,north)<.08?Double.NaN:(Math.toDegrees(Math.atan2(east,north))+360)%360;
        double pitch=Math.toDegrees(Math.asin(Math.max(-1,Math.min(1,up))));
        double gx=matrix[6],gy=matrix[7],right=gx,screenUp=gy;
        if(rotation==1){right=gy;screenUp=-gx;}
        else if(rotation==2){right=-gx;screenUp=-gy;}
        else if(rotation==3){right=-gy;screenUp=gx;}
        double roll=Math.hypot(right,screenUp)<.08?Double.NaN:Math.toDegrees(Math.atan2(right,screenUp));
        return new double[]{az,pitch,roll};
    }
}
