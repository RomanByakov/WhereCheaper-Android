package ru.codegeek.food;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ThreeFragment extends Fragment{

    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    LinearLayout conteiner;
    Context mContext;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_three,
                container, false);

        MainActivity activity = (MainActivity)getActivity();

        mContext = this.getActivity().getApplicationContext();

        conteiner = (LinearLayout) view.findViewById(R.id.conteiner);
        textView = (TextView) view.findViewById(R.id.title);

//        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // camera start
//                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                // end camera start
//            }
//        });

        return view;
    }

}
