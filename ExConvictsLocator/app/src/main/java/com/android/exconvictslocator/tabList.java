package com.android.exconvictslocator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.exconvictslocator.entities.ExConvict;
import com.android.exconvictslocator.entities.ExConvictReport;
import com.android.exconvictslocator.entities.Report;
import com.android.exconvictslocator.entities.User;
import com.android.exconvictslocator.repositories.impl.ExConvictRepository;
import com.android.exconvictslocator.repositories.impl.ReportRepository;
import com.android.exconvictslocator.repositories.impl.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class tabList extends Fragment {
    ListView listView;
    SearchView sv;
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
        View rootView=inflater.inflate(R.layout.fragment_tab_list, container, false);
        MyDatabase db =  MyDatabase.getDatabase(getActivity().getApplication());
        exConvictRepo = ExConvictRepository.getInstance(db.exConvictDao());
        reportRepo = ReportRepository.getInstance(db.reportDao());
        userRepo= UserRepository.getInstance(db.userDao());
        exConvicts= exConvictRepo.getExConvictReports();
        try{
        for(ExConvictReport er :exConvicts) {
            Collections.sort(er.getReports(), new Comparator<Report>() {
                public int compare(Report r1, Report r2) {
                    return (new Date(r2.getDate())).compareTo(new Date(r1.getDate()));
                }
            });

        }
        }catch (Exception e){}

        sv=(SearchView) rootView.findViewById(R.id.searchView1);
        //db.clearAllTables();
        //populateDbInit();
       // populateDbInit2();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = getView().findViewById(R.id.convicts_list_view);
        sv = getView().findViewById(R.id.searchView1);
        int id = sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) sv.findViewById(id);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.WHITE);
        sv.setQueryHint("Pretraga...");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String txt) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String txt) {

                List<String> convictsNames = new ArrayList<String>();
                List<ExConvictReport> filtered = new ArrayList<>();
                for (ExConvictReport e : exConvicts) {
                    if ( e.getExConvict().getFirstName().toLowerCase().contains(txt.toLowerCase()) || (e.getExConvict().getLastName().toLowerCase().contains(txt.toLowerCase()))) {
                        filtered.add(e);                    }
                }
                for (ExConvictReport r:
                        filtered) {
                    convictsNames.add(r.getExConvict().getFirstName() + " " + r.getExConvict().getLastName());
                }
                String[] names = new String[convictsNames.size()];
                MyAdapter adapter = new MyAdapter(getActivity(),filtered, convictsNames.toArray(names));
                listView.setAdapter(adapter);
                return false;
            }
        });

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
            locations.setText(convicts.get(position).getReports().size() > 0?  convicts.get(position).getReports().get(0).getLocation() : "-");
            return row;
        }
    }

    public void openDetailsActivity(ExConvictReport exConvict){
        Intent intent = new Intent(getActivity(), ExConvictDetailsActivity.class);
        Bundle b = new Bundle();

        b.putInt("idExConvict", exConvict.getExConvict().getId());
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
        b.putDouble("lat", (exConvict.getReports() != null &&  exConvict.getReports().size()> 0 )?
                exConvict.getReports().get(0).getLat() : 0);
        b.putDouble("lang", (exConvict.getReports() != null &&  exConvict.getReports().size()> 0 )?
                exConvict.getReports().get(0).getLang() : 0);
        intent.putExtras(b);
        startActivity(intent);
    }

    private  void populateDbInit(){
       ExConvict exc1 = new ExConvict( "Pera", "Peric", "-",
                R.drawable.img1, "Topolska 18", "M", "1955", "ubistvo", "opis..........1");
        ExConvict exc2 = new ExConvict( "Mika", "Mikic", "-",
                R.drawable.img2, "Topolska 19", "M", "1968", "ubistvo", "opis..........2");
        ExConvict exc3 = new ExConvict( "Zika", "Zikic", "-",
                R.drawable.img3, "Topolska 20", "M", "1985", "silovanje", "opis..........3");
exConvictRepo.insertExConvict(exc1);
exConvictRepo.insertExConvict(exc2);
exConvictRepo.insertExConvict(exc3);

        User user = new User("Jovana", "Novakovic", "password123", "jo@mailinator.com");
userRepo.insertUser(user);
    Report report1 = new Report("Zeleznicka stanica",new Date().toString(), "Novi Sad","-", 1, 1, 45.264251,  19.827240);
    Report report2 = new Report("Ruzin gaj",new Date().toString(), "Novi Sad","-", 1, 2, 45.245686,  19.815030);
    Report report3 = new Report("Kamenicki park",new Date().toString(), "Novi Sad","-", 1, 1,  45.227990,   19.849182);
reportRepo.insertReport(report1);
reportRepo.insertReport(report2);
reportRepo.insertReport(report3);


    }

    private  void populateDbInit2(){
        ExConvict exc1 = new ExConvict( "Miloš", "Petrović", "Pele",
                R.drawable.img1, "Bulevar patrijarha Pavla 14, 21 000, Novi Sad, Srbija", "Muški",
                "12.12.1955.", "ubistvo sa predumišljajem, pljačka, učešće u organizovanom kriminalu",
                "Visina 185cm, težina 80 kg, boja očiju: plava, plava kosa vezana u rep, tetovaža u obliku zmaja na vratu, slika džokera na levoj potkolenici, ožiljci na licu, potiljku i gornjem delu leve ruke, beleg na stomaku.");
        ExConvict exc2 = new ExConvict( "Nikola", "Milošević", "Džoni",
                R.drawable.img2, "Bulevar kralja Aleksandra 19, 11 000, Beograd, Srbija", "Muški", "14.04.1968.",
                "ubistvo iz nehata, ubistvo sa predumišljajem", "Visina 185cm, težina 80 kg, boja očiju: plava, plava kosa vezana u rep, tetovaža u obliku zmaja na vratu, slika džokera na levoj potkolenici, ožiljci na licu, potiljku i gornjem delu leve ruke, beleg na stomaku.");
        ExConvict exc3 = new ExConvict( "Nenad", "Mićić", "Velja",
                R.drawable.img3, "Braće Dronjak 26, 21 000, Novi Sad, Srbija", "Muški", "27.03.1985.",
                "silovanje, ubistvo iz nehata", "Visina 185cm, težina 80 kg, boja očiju: plava, plava kosa vezana u rep, tetovaža u obliku zmaja na vratu, slika džokera na levoj potkolenici, ožiljci na licu, potiljku i gornjem delu leve ruke, beleg na stomaku.");
       exc1.setId(1);
       exc2.setId(2);
       exc3.setId(3);
        exConvictRepo.insertExConvict(exc1);
        exConvictRepo.insertExConvict(exc2);
        exConvictRepo.insertExConvict(exc3);

        User user = new User("Jovana", "Novakovic", "password123", "jo@mailinator.com");
      userRepo.insertUser(user);


        Report report1 = new Report("Zeleznicka stanica",new Date().toString(), "Novi Sad","-", 1, 1, 45.264251,  19.827240);
        Report report2 = new Report("Ruzin gaj",new Date().toString(), "Novi Sad","-", 1, 2, 45.245686,  19.815030);
        Report report3 = new Report("Kamenicki park",new Date().toString(), "Novi Sad","-", 1, 1,  45.227990,   19.849182);
        reportRepo.insertReport(report1);
        reportRepo.insertReport(report2);
        reportRepo.insertReport(report3);


    }
}
