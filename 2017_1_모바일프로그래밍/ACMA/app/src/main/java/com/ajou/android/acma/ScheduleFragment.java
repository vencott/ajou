package com.ajou.android.acma;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
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

    private TextView monday[] = new TextView[8];
    private TextView tuesday[] = new TextView[8];
    private TextView wednesday[] = new TextView[8];
    private TextView thursday[] = new TextView[8];
    private TextView friday[] = new TextView[8];
    private Schedule schedule = new Schedule();

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
        monday[0] = (TextView) getView().findViewById(R.id.mondayA);
        monday[1] = (TextView) getView().findViewById(R.id.mondayB);
        monday[2] = (TextView) getView().findViewById(R.id.mondayC);
        monday[3] = (TextView) getView().findViewById(R.id.mondayD);
        monday[4] = (TextView) getView().findViewById(R.id.mondayE);
        monday[5] = (TextView) getView().findViewById(R.id.mondayF);
        monday[6] = (TextView) getView().findViewById(R.id.mondayG);
        monday[7] = (TextView) getView().findViewById(R.id.mondayH);
        tuesday[0] = (TextView) getView().findViewById(R.id.tuesdayA);
        tuesday[1] = (TextView) getView().findViewById(R.id.tuesdayB);
        tuesday[2] = (TextView) getView().findViewById(R.id.tuesdayC);
        tuesday[3] = (TextView) getView().findViewById(R.id.tuesdayD);
        tuesday[4] = (TextView) getView().findViewById(R.id.tuesdayE);
        tuesday[5] = (TextView) getView().findViewById(R.id.tuesdayF);
        tuesday[6] = (TextView) getView().findViewById(R.id.tuesdayG);
        tuesday[7] = (TextView) getView().findViewById(R.id.tuesdayH);
        wednesday[0] = (TextView) getView().findViewById(R.id.wednesdayA);
        wednesday[1] = (TextView) getView().findViewById(R.id.wednesdayB);
        wednesday[2] = (TextView) getView().findViewById(R.id.wednesdayC);
        wednesday[3] = (TextView) getView().findViewById(R.id.wednesdayD);
        wednesday[4] = (TextView) getView().findViewById(R.id.wednesdayE);
        wednesday[5] = (TextView) getView().findViewById(R.id.wednesdayF);
        wednesday[6] = (TextView) getView().findViewById(R.id.wednesdayG);
        wednesday[7] = (TextView) getView().findViewById(R.id.wednesdayH);
        thursday[0] = (TextView) getView().findViewById(R.id.thursdayA);
        thursday[1] = (TextView) getView().findViewById(R.id.thursdayB);
        thursday[2] = (TextView) getView().findViewById(R.id.thursdayC);
        thursday[3] = (TextView) getView().findViewById(R.id.thursdayD);
        thursday[4] = (TextView) getView().findViewById(R.id.thursdayE);
        thursday[5] = (TextView) getView().findViewById(R.id.thursdayF);
        thursday[6] = (TextView) getView().findViewById(R.id.thursdayG);
        thursday[7] = (TextView) getView().findViewById(R.id.thursdayH);
        friday[0] = (TextView) getView().findViewById(R.id.fridayA);
        friday[1] = (TextView) getView().findViewById(R.id.fridayB);
        friday[2] = (TextView) getView().findViewById(R.id.fridayC);
        friday[3] = (TextView) getView().findViewById(R.id.fridayD);
        friday[4] = (TextView) getView().findViewById(R.id.fridayE);
        friday[5] = (TextView) getView().findViewById(R.id.fridayF);
        friday[6] = (TextView) getView().findViewById(R.id.fridayG);
        friday[7] = (TextView) getView().findViewById(R.id.fridayH);

        schedule.addSchedule("월:[6][7],화:[5],목:[4]", "모바일", "경민호");
        schedule.addSchedule("화:[1],목:[0]", "알고리즘", "경민호");
        schedule.addSchedule("수:[4],금:[4]", "현대인의", "김민재");
        schedule.setting(monday, tuesday, wednesday, thursday, friday, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
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
}
