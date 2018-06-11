package com.ajou.android.acma;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    LectureLab lectureLab;

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
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

        lectureLab = LectureLab.get(getActivity());
    }


    private ArrayAdapter termAdapter;
    private Spinner termSpinner;

    private String courseUniversity = "";

    private ListView courseListView;
    private CourseAdapter adapter;
    private List<Lecture> courseList;

    @Override
    public void onActivityCreated(Bundle b){
        super.onActivityCreated(b);

        courseListView = (ListView)getView().findViewById(R.id.courseListView);
        lectureLab = LectureLab.get(getActivity());
        courseList = lectureLab.getLectures();
        adapter = new CourseAdapter(getContext().getApplicationContext(), courseList);
        courseListView.setAdapter(adapter);

        final RadioGroup courseUniversityGroup = (RadioGroup)getView().findViewById(R.id.courseUniversityGroup);
        termSpinner = (Spinner)getView().findViewById(R.id.termSpinner);

        courseUniversityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radiogroup, int i) {
                RadioButton courseButton = (RadioButton) getView().findViewById(i);
                courseUniversity = courseButton.getText().toString();

                termAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.term, android.R.layout.simple_spinner_dropdown_item);
                termSpinner.setAdapter(termAdapter);

                if (courseUniversity.equals("전공")) {
                    final List<Lecture> majorList = lectureLab.getMajorLectures();

                    termSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(termSpinner.getSelectedItem().equals("2017-1학기")) {
                                courseList = lectureLab.getTermLectures("2017-1학기", majorList);
                            }
                            else if(termSpinner.getSelectedItem().equals("2017-2학기")) {
                                courseList = lectureLab.getTermLectures("2017-2학기", majorList);
                            }
                            else if(termSpinner.getSelectedItem().equals("2016-1학기")) {
                                courseList = lectureLab.getTermLectures("2016-1학기", majorList);
                            }
                            else if(termSpinner.getSelectedItem().equals("2016-2학기")) {
                                courseList = lectureLab.getTermLectures("2016-2학기", majorList);
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
                else if (courseUniversity.equals("교양")){
                    final List<Lecture> refinementList = lectureLab.getRefinementLectures();

                    termSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(termSpinner.getSelectedItem().equals("2017-1학기")) {
                                courseList = lectureLab.getTermLectures("2017-1학기", refinementList);
                            }
                            else if(termSpinner.getSelectedItem().equals("2017-2학기")) {
                                courseList = lectureLab.getTermLectures("2017-2학기", refinementList);
                            }
                            else if(termSpinner.getSelectedItem().equals("2016-1학기")) {
                                courseList = lectureLab.getTermLectures("2016-1학기", refinementList);
                            }
                            else if(termSpinner.getSelectedItem().equals("2016-2학기")) {
                                courseList = lectureLab.getTermLectures("2016-2학기", refinementList);
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else {
                    courseList = lectureLab.getLectures();
                }
            }
        });

        Button searchButton = (Button)getView().findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter = new CourseAdapter(getContext().getApplicationContext(), courseList);
                courseListView.setAdapter(adapter);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course, container, false);
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

    /*
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
}
