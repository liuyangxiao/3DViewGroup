package com.test.foethedog;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.burning.foethedog.Rota3DSwithView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Rota3DSwithView rota3DSwithView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rota3DSwithView = (Rota3DSwithView) findViewById(R.id.roat_main);
        for (int i = 0; i < rota3DSwithView.getChildCount(); i++) {
            if (rota3DSwithView.getChildAt(i) instanceof TextView) {
                TextView tx = (TextView) rota3DSwithView.getChildAt(i);
                tx.setOnClickListener(this);
            }
        }
        findViewById(R.id.star_stop_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rota3DSwithView.setAutoscroll(!rota3DSwithView.isAutoscroll());
            }
        });
        findViewById(R.id.next_button_scoll_Direction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rota3DSwithView.setIsleftortop(!rota3DSwithView.isIsleftortop());
            }
        });
        findViewById(R.id.next_button_layout_Direction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rota3DSwithView.setRotateV(!rota3DSwithView.isRotateV());
            }
        });
        findViewById(R.id.next_button_startList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, View3DListActivity.class));
            }
        });


        findViewById(R.id.retupage_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rota3DSwithView.returnPage();
            }
        });
        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rota3DSwithView.nextPage();
            }
        });

        reclerview = (RecyclerView) findViewById(R.id.test_recylerview);
        reclerview.setLayoutManager(new GridLayoutManager(reclerview.getContext(), Integer.valueOf(2)));
        reclerview.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                TextView textView = new TextView(MainActivity.this);

                RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(textView) {
                };
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView).setText("item=====" + position + "lllllllllllllllllllllllllllllllla");
            }

            @Override
            public int getItemCount() {
                return 90;
            }
        });


    }

    RecyclerView reclerview;

    @Override
    public void onClick(View v) {
        Toast.makeText(this, ((TextView) v).getText().toString(), Toast.LENGTH_SHORT).show();
    }
}
