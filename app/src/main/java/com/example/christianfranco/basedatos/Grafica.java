package com.example.christianfranco.basedatos;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class Grafica extends AppCompatActivity {

    LineChartView lineChartView;
    ArrayList<String> Datos= new ArrayList<>();
    ArrayList<String> axisData= new ArrayList<>();
    ArrayList<Integer> yAxisData= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica);

        lineChartView = findViewById(R.id.chart);


        Datos=Consulta.datos;//obtengo los datos de la base
        for(int i=0;i<Datos.size();i++){
            String[] parts = Datos.get(i).split(" ");//para tomar solo la palabra y no la unidad lo llamo con parts[0]
            axisData.add(parts[0]);
            yAxisData.add(Integer.valueOf(parts[1]));
        }


        //datos del eje x
        //String[] axisData = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
        //datos del eje y
        //int[] yAxisData = {50, 20, 15, 30, 20, 60, 15, 40, 45, 10, 90, 18};

        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();

        //Line line = new Line(yAxisValues);
        Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));

       // for(int i = 0; i < axisData.length; i++){
        //    axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
      //  }


        for(int i = 0; i < axisData.size(); i++){
            axisValues.add(i, new AxisValue(i).setLabel(axisData.get(i)));
        }

       // for (int i = 0; i < yAxisData.length; i++){
        //    yAxisValues.add(new PointValue(i, yAxisData[i]));
       // }

        for (int i = 0; i < yAxisData.size(); i++){
            yAxisValues.add(new PointValue(i, yAxisData.get(i)));
        }

        List lines = new ArrayList();
        lines.add(line);


        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis Xaxis = new Axis();
        Xaxis.setValues(axisValues);
        data.setAxisXBottom(Xaxis);
        Xaxis.setTextSize(16);
        Xaxis.setName("Fecha");
        Xaxis.setTextColor(Color.parseColor("#03A9F4"));

        Axis yAxis = new Axis();
        data.setAxisYLeft(yAxis);

        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);
        yAxis.setName("Valores");

        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top =110;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);



        lineChartView.setLineChartData(data);




    }
}
