package com.burning.foethedog;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class View3DListActivity extends AppCompatActivity {
    Rota3DSwithViewList mRota3DSwithViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view3_d_list);
        findViewById(R.id.star_stop_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRota3DSwithViewList.setAutoscroll(!mRota3DSwithViewList.isAutoscroll());
            }
        });
        findViewById(R.id.next_button_scoll_Direction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRota3DSwithViewList.setIsleftortop(!mRota3DSwithViewList.isIsleftortop());
            }
        });
        findViewById(R.id.next_button_layout_Direction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRota3DSwithViewList.setRotateV(!mRota3DSwithViewList.isRotateV());
            }
        });

        findViewById(R.id.retupage_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRota3DSwithViewList.returnPage();
            }
        });
        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRota3DSwithViewList.nextPage();
            }
        });


        mRota3DSwithViewList = (Rota3DSwithViewList) findViewById(R.id.rota3DSwithViewList_test);
        mRota3DSwithViewList.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


                View inflate = View.inflate(parent.getContext(), R.layout.test_item, null);

                RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(inflate) {


                };
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                System.out.println("=======onBindViewHolder====" + position);
                ((TextView) holder.itemView.findViewById(R.id.action_text))
                        .setText("你好啊=====" + position + "你好你说的是什么的东西我看看长度够不够不够我再加一点");

            }

            @Override
            public int getItemCount() {
                return 200;
            }
        });
    }
}