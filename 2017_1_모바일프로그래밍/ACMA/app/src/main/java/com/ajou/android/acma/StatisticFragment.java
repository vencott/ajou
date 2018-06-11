package com.ajou.android.acma;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatisticFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatisticFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public StatisticFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticFragment newInstance(String param1, String param2) {
        StatisticFragment fragment = new StatisticFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ArrayAdapter rankAdapter;
    private Spinner rankSpinner;
    private ListView rankListView;
    private RankAdapter adapter;
    private List<Lecture> beforeList;
    private RankLecture[] rankLectures;
    private List<Lecture> rankList;
    public long TotalCredit;
    LectureLab lectureLab;

    @Override
    public void onActivityCreated(Bundle b){
        super.onActivityCreated(b);

        rankListView = (ListView)getView().findViewById(R.id.rankListView);

        lectureLab = LectureLab.get(getActivity());
        beforeList = lectureLab.getLectures();
        rankList = new ArrayList<Lecture>();
        rankLectures = new RankLecture[DatabaseManager.totalLectureNum];

        sort(DatabaseManager.totalLectureNum);

        final TextView totalCredit = (TextView)getView().findViewById(R.id.totalCredit);
        String userID = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String UserID = userID.replaceAll("[.]","");
        DatabaseManager.databaseReference.child("users").child(UserID).child("totalCredit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TotalCredit = (Long) dataSnapshot.getValue();
                totalCredit.setText(Long.toString(TotalCredit)+ "학점");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        adapter = new RankAdapter(getView().getContext(), rankList, rankLectures);
        rankListView.setAdapter(adapter);

        rankSpinner = (Spinner) getView().findViewById(R.id.rankSpinner);
        rankAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.rank,android.R.layout.simple_spinner_dropdown_item);
        rankSpinner.setAdapter(rankAdapter);
        rankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(rankSpinner.getSelectedItem().equals("전체 순위")){
                    beforeList = lectureLab.getLectures();
                    sort(DatabaseManager.totalLectureNum);
                }
                else if (rankSpinner.getSelectedItem().equals("전공 순위")) {
                    beforeList = lectureLab.getMajorLectures();
                    sort(DatabaseManager.majorLectureNum);
                }
                else if (rankSpinner.getSelectedItem().equals("교양 순위")){
                    beforeList = lectureLab.getRefinementLectures();
                    sort(DatabaseManager.refinementLectureNum);
                }

                adapter = new RankAdapter(getView().getContext(), rankList, rankLectures);
                rankListView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistic, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void sort(int num) {
        rankList.clear();
        rankLectures = new RankLecture[num];

        for(int i = 0; i < num; i++) {
            double rate = (double)beforeList.get(i).getCourseRegister() / (double)beforeList.get(i).getCourseLimit();
            rankLectures[i] = new RankLecture(beforeList.get(i).getCount(), rate);
        }

        Arrays.sort(rankLectures);

        for(int i = 0; i < num; i++)
            rankList.add(lectureLab.getLectureByCount(rankLectures[i].getCount()));
    }
}