package com.android.exconvictslocator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.exconvictslocator.entities.ExConvict;
import com.android.exconvictslocator.entities.ExConvictReport;
import com.android.exconvictslocator.entities.Report;
import com.android.exconvictslocator.repositories.impl.ExConvictRepository;
import com.android.exconvictslocator.repositories.impl.ReportRepository;
import com.android.exconvictslocator.repositories.impl.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class tabList extends Fragment {
    ListView listView;
    private UserRepository userRepo;
    private ExConvictRepository exConvictRepo;
    private ReportRepository reportRepo;
    private List<ExConvictReport> exConvicts;


    public tabList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        MyDatabase db =  MyDatabase.getDatabase(getActivity().getApplication());
        exConvictRepo = ExConvictRepository.getInstance(db.exConvictDao());
        reportRepo = ReportRepository.getInstance(db.reportDao());
        exConvicts= exConvictRepo.getExConvictReports();

        return inflater.inflate(R.layout.fragment_tab_list, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = getView().findViewById(R.id.convicts_list_view);
        List<String> convictsNames = new ArrayList<String>();
        for (ExConvictReport r:
             exConvicts) {
            convictsNames.add(r.getExConvict().getFirstName() + " " + r.getExConvict().getLastName());
        }


        String[] names = new String[convictsNames.size()];
        MyAdapter adapter = new MyAdapter(this.getActivity(),this.exConvicts, convictsNames.toArray(names));
        listView.setAdapter(adapter);
    }

    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        List<ExConvictReport> convicts;

        public MyAdapter(@NonNull Context context,List<ExConvictReport> exConvicts, String names[]) {
            super(context, R.layout.row_in_list, R.id.convict_name, names);
            this.context = context;
        this.convicts= exConvicts;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_in_list, parent, false);
            ImageView images = row.findViewById(R.id.convict_image);
            TextView names = row.findViewById(R.id.convict_name);
            TextView crimes = row.findViewById(R.id.convict_crime);
            TextView locations = row.findViewById(R.id.convict_last_location);

           Button allLocationsBtn = (Button) row.findViewById(R.id.convict_details);

            allLocationsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDetailsActivity(convicts.get(position));
                }
            });


            images.setImageResource(convicts.get(position).getExConvict().getPhoto());
            names.setText(convicts.get(position).getExConvict().getFirstName() + " " + convicts.get(position).getExConvict().getLastName());
            crimes.setText(convicts.get(position).getExConvict().getCrime());
            locations.setText(convicts.get(position).getExConvict().getAddress());

            return row;
        }
    }

    public void openDetailsActivity(ExConvictReport exConvict){
        Intent intent = new Intent(getActivity(), ExConvictDetailsActivity.class);
        Bundle b = new Bundle();

        b.putString("name", exConvict.getExConvict().getFirstName() + " " + exConvict.getExConvict().getLastName());
        b.putString("nickname", exConvict.getExConvict().getPseudonym());
        b.putInt("image", exConvict.getExConvict().getPhoto());
        b.putString("address", exConvict.getExConvict().getAddress());
        b.putString("birth", exConvict.getExConvict().getDateOfBirth());
        b.putString("gender", exConvict.getExConvict().getGender());
        b.putString("crime",exConvict.getExConvict().getCrime() );
        b.putString("updatedAt",  (exConvict.getReports() != null &&  exConvict.getReports().size()> 0 )?
                exConvict.getReports().get(0).getDate() : "-");
        b.putString("desc", exConvict.getExConvict().getDescription());
        b.putString("lastLocation", (exConvict.getReports() != null &&  exConvict.getReports().size()> 0 )?
                exConvict.getReports().get(0).getLocation() : "-");
        intent.putExtras(b);
        startActivity(intent);
    }
}
