import br.org.itaarandu.itamana.LevelMath;
public class LevelMathCheck {
    static void equal(double expected,double actual){if(Double.isNaN(expected)){if(!Double.isNaN(actual))throw new AssertionError("Expected undefined");}else if(Math.abs(expected-actual)>.0001)throw new AssertionError(expected+" != "+actual);}
    public static void main(String[] args){
        float[][] level={ {1,0,0,0,0,-1,0,1,0}, {0,1,0,0,0,-1,-1,0,0}, {-1,0,0,0,0,-1,0,-1,0}, {0,-1,0,0,0,-1,1,0,0} };
        for(int rotation=0;rotation<4;rotation++){double[] result=LevelMath.angles(level[rotation],rotation,true);equal(0,result[0]);equal(0,result[1]);equal(0,result[2]);}
        double theta=Math.toRadians(10);float c=(float)Math.cos(theta),s=(float)Math.sin(theta);
        double[] tilted=LevelMath.angles(new float[]{c,s,0,0,0,-1,-s,c,0},0,true);equal(0,tilted[0]);equal(-10,tilted[2]);
        double[] sky=LevelMath.angles(new float[]{1,0,0,0,1,0,0,0,1},0,true);equal(Double.NaN,sky[0]);equal(-90,sky[1]);equal(Double.NaN,sky[2]);
        equal(Double.NaN,LevelMath.angles(level[0],0,false)[0]);
        double[] up=LevelMath.angles(new float[]{1,0,0,0,-.5f,-.8660254f,0,.8660254f,-.5f},0,true);equal(30,up[1]);equal(0,up[2]);
        System.out.println("PASS: four screen rotations, optical heading invariance, tilt sign, vertical singularity, missing magnetic heading, elevation.");
    }
}
