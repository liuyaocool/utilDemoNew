package com.liuyao.demo.gis;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import wcontour.Contour;
import wcontour.Interpolate;
import wcontour.global.Border;
import wcontour.global.PointD;
import wcontour.global.PolyLine;
import wcontour.global.Polygon;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Lyu
 * @date 2019/11/5  14:37
 * @Description: 等高线实现
 **/
public class EquiSurface {


    /**
     * 生成等值面
     *
     * @param trainData    训练数据
     * @param dataInterval 数据间隔
     * @param size         大小，宽，高
     * @param brounds      边界
     * @return
     */
    public String calEquiSurface(double[][] trainData, double[] dataInterval, int[] size, double[] brounds) {
        String geojsonpogylon = "";
        try {
            double _undefData = -9999.0;

            List<PolyLine> cPolylineList = new ArrayList<PolyLine>();
            List<Polygon> cPolygonList = new ArrayList<Polygon>();

            int width = size[0],
                    height = size[1];
            double[] _X = new double[width];
            double[] _Y = new double[height];

//            File file = new File(boundryFile);

            double minX = brounds[0];
            double minY = brounds[1];
            double maxX = brounds[2];
            double maxY = brounds[3];

            //创建grid矩阵 将数据写进矩阵里
            Interpolate.createGridXY_Num(minX, minY, maxX, maxY, _X, _Y);

            double[][] _gridData = new double[width][height];

            int nc = dataInterval.length;

            _gridData = Interpolate.interpolation_IDW_Neighbor(trainData,
                    _X, _Y, 12, _undefData);// IDW插值

            int[][] S1 = new int[_gridData.length][_gridData[0].length];
            List<Border> _borders = Contour.tracingBorders(_gridData, _X, _Y,
                    S1, _undefData);

            cPolylineList = Contour.tracingContourLines(_gridData, _X, _Y, nc,
                    dataInterval, _undefData, _borders, S1);// 生成等值线

            cPolylineList = Contour.smoothLines(cPolylineList);// 平滑

            cPolygonList = Contour.tracingPolygons(_gridData, cPolylineList,
                    _borders, dataInterval);

            geojsonpogylon = getPolygonGeoJson(cPolygonList);


            System.out.println(geojsonpogylon);
//            if (isclip) {
//                polygonCollection = GeoJSONUtil.readGeoJsonByString(geojsonpogylon);
//                FeatureSource dc = clipFeatureCollection(fc, polygonCollection);
//                geojsonpogylon = getPolygonGeoJson(dc.getFeatures());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return geojsonpogylon;
    }


//
    private String getPolygonGeoJson(List<Polygon> cPolygonList) {

        StringBuffer geobody = new StringBuffer();
        StringBuffer geo = new StringBuffer();
        String geometry = " { \"type\":\"Feature\",\"geometry\":";
        String properties = ",\"properties\":{ \"hvalue\":";

        String head = "{\"type\": \"FeatureCollection\"," + "\"features\": [";
        String end = "  ] }";
        if (cPolygonList == null || cPolygonList.size() == 0) {
            return null;
        }
        try {
            for (Polygon pPolygon : cPolygonList) {

                List<Object> ptsTotal = new ArrayList<Object>();
                List<Object> pts = new ArrayList<Object>();

                PolyLine pline = pPolygon.OutLine;

                for (PointD ptD : pline.PointList) {
                    List<Double> pt = new ArrayList<Double>();
                    pt.add(ptD.X);
                    pt.add(ptD.Y);
                    pts.add(pt);
                }

                ptsTotal.add(pts);

                if (pPolygon.HasHoles()) {
                    for (PolyLine cptLine : pPolygon.HoleLines) {
                        List<Object> cpts = new ArrayList<Object>();
                        for (PointD ccptD : cptLine.PointList) {
                            List<Double> pt = new ArrayList<Double>();
                            pt.add(ccptD.X);
                            pt.add(ccptD.Y);
                            cpts.add(pt);
                        }
                        if (cpts.size() > 0) {
                            ptsTotal.add(cpts);
                        }
                    }
                }

                JSONObject js = new JSONObject();
                js.put("type", "Polygon");
                js.put("coordinates", ptsTotal);
                double hv = pPolygon.HighValue;
                double lv = pPolygon.LowValue;

                if (hv == lv) {
                    if (pPolygon.IsClockWise) {
                        if (!pPolygon.IsHighCenter) {
                            hv = hv - 0.1;
                            lv = lv - 0.1;
                        }

                    } else {
                        if (!pPolygon.IsHighCenter) {
                            hv = hv - 0.1;
                            lv = lv - 0.1;
                        }
                    }
                } else {
                    if (!pPolygon.IsClockWise) {
                        lv = lv + 0.1;
                    } else {
                        if (pPolygon.IsHighCenter) {
                            hv = hv - 0.1;
                        }
                    }

                }

                geo.append( geometry);
                geo.append( js.toString());
                geo.append(  properties);
                geo.append(hv);
                geo.append( ", \"lvalue\":");
                geo.append(  lv);
                geo.append(  "} }," );
                geo.append(  geo);

            }
            if (geo.lastIndexOf(",")==geo.length()-1) {
                geo.deleteCharAt(geo.length()-1);
//                geo = geo.substring(0, geo.lastIndexOf(","));
            }

            geobody.append(head);
            geobody.append(geo).append(end);
        } catch (Exception e) {
            e.printStackTrace();
            return geobody.toString();
        }
        return geobody.toString();
    }

    public static void write2File(String filePath,String buf){
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
