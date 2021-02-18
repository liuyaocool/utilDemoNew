package com.liuyao.demo.gis;

/**
 * @author Lyu
 * @date 2019/11/6  17:00
 * @Description: 离散点生成三角网, 插值生成等值线
 **/
public class Conrec {

    private double  [] h   =  new double [5];
    private int    []  sh  =  new int   [5];
    private double  []  xh =  new double [5];
    private double  []  yh =  new double [5];

    // Object that knows how to draw the contour
    private Render render = null;


    public  Conrec(Render render) throws Exception {
        if (render == null){
            throw new Exception ("Render null");
        }
        this.render = render;
    }



    public void contour(double [][] d, int ilb, int iub, int jlb, int jub, double [] x, double [] y, int nc, double [] z) {
        int        m1;
        int        m2;
        int        m3;
        int        case_value;
        double     dmin;
        double     dmax;
        double     x1 = 0.0;
        double     x2 = 0.0;
        double     y1 = 0.0;
        double     y2 = 0.0;
        int        i,j,k,m;

        // The indexing of im and jm should be noted as it has to start from zero
        // unlike the fortran counter part
        int    [] im   = {0,1,1,0};
        int    [] jm   = {0,0,1,1};

        // Note that castab is arranged differently from the FORTRAN code because
        // Fortran and C/C++ arrays are transposed of each other, in this case
        // it is more tricky as castab is in 3 dimension
        int [][][] castab=
                {
                        {
                                {0,0,8},{0,2,5},{7,6,9}
                        },
                        {
                                {0,3,4},{1,3,1},{4,3,0}
                        },
                        {
                                {9,6,7},{5,2,0},{8,0,0}
                        }
                };

        for (j=(jub-1);j>=jlb;j--) {
            for (i=ilb;i<=iub-1;i++) {
                double temp1,temp2;
                temp1 = Math.min(d[i][j],d[i][j+1]);
                temp2 = Math.min(d[i+1][j],d[i+1][j+1]);
                dmin  = Math.min(temp1,temp2);
                temp1 = Math.max(d[i][j],d[i][j+1]);
                temp2 = Math.max(d[i+1][j],d[i+1][j+1]);
                dmax  = Math.max(temp1,temp2);

                if (dmax>=z[0]&&dmin<=z[nc-1]) {
                    for (k=0;k<nc;k++) {
                        if (z[k]>=dmin&&z[k]<=dmax) {
                            for (m=4;m>=0;m--) {
                                if (m>0) {
                                    // The indexing of im and jm should be noted as it has to
                                    // start from zero
                                    h[m] = d[i+im[m-1]][j+jm[m-1]]-z[k];
                                    xh[m] = x[i+im[m-1]];
                                    yh[m] = y[j+jm[m-1]];
                                } else {
                                    h[0] = 0.25*(h[1]+h[2]+h[3]+h[4]);
                                    xh[0]=0.5*(x[i]+x[i+1]);
                                    yh[0]=0.5*(y[j]+y[j+1]);
                                }
                                if (h[m]>0.0) {
                                    sh[m] = 1;
                                } else if (h[m]<0.0) {
                                    sh[m] = -1;
                                } else
                                    sh[m] = 0;
                            }
                            //
                            // Note: at this stage the relative heights of the corners and the
                            // centre are in the h array, and the corresponding coordinates are
                            // in the xh and yh arrays. The centre of the box is indexed by 0
                            // and the 4 corners by 1 to 4 as shown below.
                            // Each triangle is then indexed by the parameter m, and the 3
                            // vertices of each triangle are indexed by parameters m1,m2,and
                            // m3.
                            // It is assumed that the centre of the box is always vertex 2
                            // though this isimportant only when all 3 vertices lie exactly on
                            // the same contour level, in which case only the side of the box
                            // is drawn.
                            //
                            //
                            //     vertex 4 +-------------------+ vertex 3
                            //              | \              / |
                            //              |  \   m-3   /   |
                            //              |    \      /    |
                            //              |      \  /      |
                            //              | m=2   X  m=2  |      the centre is vertex 0
                            //              |      /  \      |
                            //              |    /      \    |
                            //              |  /   m=1   \   |
                            //              | /              \ |
                            //     vertex 1 +-------------------+ vertex 2
                            //
                            //
                            //
                            //              Scan each triangle in the box
                            //
                            for (m=1;m<=4;m++) {
                                m1 = m;
                                m2 = 0;
                                if (m!=4) {
                                    m3 = m+1;
                                } else {
                                    m3 = 1;
                                }
                                case_value = castab[sh[m1]+1][sh[m2]+1][sh[m3]+1];
                                if (case_value!=0) {
                                    switch (case_value) {
                                        case 1: // Line between vertices 1 and 2
                                            x1=xh[m1];
                                            y1=yh[m1];
                                            x2=xh[m2];
                                            y2=yh[m2];
                                            break;
                                        case 2: // Line between vertices 2 and 3
                                            x1=xh[m2];
                                            y1=yh[m2];
                                            x2=xh[m3];
                                            y2=yh[m3];
                                            break;
                                        case 3: // Line between vertices 3 and 1
                                            x1=xh[m3];
                                            y1=yh[m3];
                                            x2=xh[m1];
                                            y2=yh[m1];
                                            break;
                                        case 4: // Line between vertex 1 and side 2-3
                                            x1=xh[m1];
                                            y1=yh[m1];
                                            x2=xsect(m2,m3);
                                            y2=ysect(m2,m3);
                                            break;
                                        case 5: // Line between vertex 2 and side 3-1
                                            x1=xh[m2];
                                            y1=yh[m2];
                                            x2=xsect(m3,m1);
                                            y2=ysect(m3,m1);
                                            break;
                                        case 6: //  Line between vertex 3 and side 1-2
                                            x1=xh[m3];
                                            y1=yh[m3];
                                            x2=xsect(m1,m2);
                                            y2=ysect(m1,m2);
                                            break;
                                        case 7: // Line between sides 1-2 and 2-3
                                            x1=xsect(m1,m2);
                                            y1=ysect(m1,m2);
                                            x2=xsect(m2,m3);
                                            y2=ysect(m2,m3);
                                            break;
                                        case 8: // Line between sides 2-3 and 3-1
                                            x1=xsect(m2,m3);
                                            y1=ysect(m2,m3);
                                            x2=xsect(m3,m1);
                                            y2=ysect(m3,m1);
                                            break;
                                        case 9: // Line between sides 3-1 and 1-2
                                            x1=xsect(m3,m1);
                                            y1=ysect(m3,m1);
                                            x2=xsect(m1,m2);
                                            y2=ysect(m1,m2);
                                            break;
                                        default:
                                            break;
                                    }
                                    // Put your processing code here and comment out the printf
                                    //printf("%f %f %f %f %f\n",x1,y1,x2,y2,z[k]);
                                    render.drawContour(x1,y1,x2,y2,z[k]);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private double xsect(int p1, int p2){
        return   (h[p2]*xh[p1]-h[p1]*xh[p2])/(h[p2]-h[p1]);
    }

    private double ysect(int p1, int p2){
        return (h[p2]*yh[p1]-h[p1]*yh[p2])/(h[p2]-h[p1]);
    }
}