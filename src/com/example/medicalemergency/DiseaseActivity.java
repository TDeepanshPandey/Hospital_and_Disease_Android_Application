package com.example.medicalemergency;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DiseaseActivity extends AppCompatActivity {

    TextView hypothermia,heatstroke,heartattack,diabetes,seizure,asthama,triggers,roadaccident;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);

        hypothermia=(TextView)findViewById(R.id.hypothermia);
        heatstroke=(TextView)findViewById(R.id.heatstroke);
        heartattack=(TextView)findViewById(R.id.heartattack);
        diabetes=(TextView)findViewById(R.id.diabetes);
        seizure=(TextView)findViewById(R.id.seizures);
        asthama=(TextView)findViewById(R.id.asthama);
        triggers=(TextView)findViewById(R.id.triggers);
        roadaccident=(TextView)findViewById(R.id.roadaccident);

        /**
         * manage onclick of that button....
         */

        hypothermia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(DiseaseActivity.this,Hypothermia.class);
                startActivity(ii);
            }
        });

        heatstroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(DiseaseActivity.this,HeatStrokeActivity.class);
                startActivity(ii);
            }
        });

        heartattack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(DiseaseActivity.this,HeartAttackActivity.class);
                startActivity(ii);
            }
        });

        diabetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(DiseaseActivity.this,DiabetesActivity.class);
                startActivity(ii);
            }
        });

        seizure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(DiseaseActivity.this,SeizuresActivity.class);
                startActivity(ii);
            }
        });

        asthama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(DiseaseActivity.this,AsthamaActivity.class);
                startActivity(ii);
            }
        });

        triggers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(DiseaseActivity.this,TriggersActivity.class);
                startActivity(ii);
            }
        });

        roadaccident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(DiseaseActivity.this,RoadAttackActivity.class);
                startActivity(ii);
            }
        });

    }
}
