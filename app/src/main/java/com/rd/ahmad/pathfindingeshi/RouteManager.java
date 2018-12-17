package com.rd.ahmad.pathfindingeshi;


import android.os.Debug;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class RouteManager {

    List <Station> stations = new ArrayList<>();

    Station startPoint = new Station();
    Station endPoint = new Station();



    public Direction distance(Station station1, Station station2){

        double lat1 = station1.Lat,  lon1= station1.Lng,  lat2= station2.Lat,  lon2= station2.Lng;

        //Get distance
        double R = 6378.137f; // Radius of earth in KM
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2.0f) + Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;

        //Get Azimuth
        double y = Math.sin(lon2-lon1) * Math.cos(lat2);
        double x = Math.cos(lat1)*Math.sin(lat2) - Math.sin(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1);
        double brng = Math.atan2(y, x) * 180 / Math.PI;

        //Create direction to return
        Direction direction = new Direction();
        direction.Length = d * 1000; // meters
        direction.Azimuth = brng;

        return direction;
        //Reference https://www.movable-type.co.uk/scripts/latlong.html

    }



    public Station nextStation(Station station1,double distance,double brng) {

        double lat1 = station1.Lat, lon1 = station1.Lng;
        double R = 6378.137;

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(distance / R) + Math.cos(lat1) * Math.sin(distance / R) * Math.cos(brng));

        double lon2 = lon1 + Math.atan2(Math.sin(brng) * Math.sin(distance / R) * Math.cos(lat1), Math.cos(distance / R) - Math.sin(lat1) * Math.sin(lat2));

        Station station2 = new Station();
        station2.setLocation(lat2, lon2);

        return station2;
    }

    public void Calculate(){
        stations.clear();

        Station CurrStation = startPoint;
        stations.add(CurrStation);

        getElevation(startPoint);

        while (distance(CurrStation,endPoint).Length <= 30){

            for(int i = 0; i < 360; i=+30){

            }

            Station temp = nextStation(CurrStation,50,distance(CurrStation,endPoint).Azimuth);
            stations.add(temp);
        }
        stations.add(endPoint);
    }

    private void getElevation(Station stn){

        try {
            URL url = new URL("https://api.open-elevation.com/api/v1/lookup?locations=10,10");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            InputStream stream = connection.getInputStream();

            XmlPullParserFactory xmlFactoryObject;
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myParser.setInput(stream, null);

            String Elevation = processParsing(myParser);

            stream.close();

            Log.d("TEST", "getElevation: " + Elevation);

        }catch (Exception  e) {
            e.printStackTrace();
            Log.d("TEST", "getElevation: ERROR");
        }

    }

    private String processParsing(XmlPullParser parser){

        try{
            int event = parser.getEventType();
            String tag = null;

            while (event != XmlPullParser.END_DOCUMENT){

                switch (event) {
                    case XmlPullParser.START_TAG:
                        tag = parser.getName();
                        if (tag == "elevation"){
                            return parser.nextText();
                        }
                        break;
                }
                event = parser.next();
            }

        }catch (Exception  e) {
            e.printStackTrace();
            Log.d("TEST", "getElevation: ERROR");
        }
        return null;

    }




    public class Station{
        double Lat;
        double Lng;

        void setLocation(double lat, double lng){
            Lat = lat;
            Lng = lng;
        }

        LatLng getLatLng(){
            return new LatLng(Lat,Lng);
        }
    }

    public class Direction{
        double Length;
        double Azimuth;
    }
}
