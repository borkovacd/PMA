package com.android.exconvictslocator;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class tabList extends Fragment {
    ListView listView;
    String convictsNames [] = {"Pera Perić", "Mika Mikić", "Žika Žikić"};
    String convictsCrimes [] = {"silovanje", "ubistvo", "ubistvo"};
    int convictsImages[] = {R.drawable.img1, R.drawable.img2, R.drawable.img3};


    public tabList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_tab_list, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = getView().findViewById(R.id.convicts_list_view);
        MyAdapter adapter = new MyAdapter(this.getActivity(), convictsNames, convictsCrimes, convictsImages);
        listView.setAdapter(adapter);
    }

    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        String rNames[];
        String rCrimes[];
        int rImgs[];

        public MyAdapter(@NonNull Context context, String names[], String crimes[], int imgs[] ) {
            super(context, R.layout.row_in_list, R.id.convict_name, names);
            this.context = context;
            this.rCrimes = crimes;
            this.rNames = names;
            this.rImgs = imgs;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_in_list, parent, false);
            ImageView images = row.findViewById(R.id.convict_image);
            TextView names = row.findViewById(R.id.convict_name);
            TextView crimes = row.findViewById(R.id.convict_crime);
            images.setImageResource(rImgs[position]);
            names.setText(rNames[position]);
            crimes.setText(rCrimes[position]);

            return row;
        }
    }
}
