package com.allenwalker.android.mybkinfo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestScheduleFragment extends Fragment {
    private static final String ARG_MSSV = "arg_mssv";

    private final String mUrl = "http://www.aao.hcmut.edu.vn/image/data/Tra_cuu/xem_lt";

    private ProgressDialog mProgressDialog;
    private String mMSSV;
    public String infoSV;
    private TextView infoStudent;
    private RecyclerView mRecyclerView;
    private List<StudentInfo> mStudentInfos;

    public static TestScheduleFragment newInstance(String mssv) {
        Bundle args = new Bundle();
        args.putString(ARG_MSSV, mssv);

        TestScheduleFragment fragment = new TestScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mMSSV = getArguments().getString(ARG_MSSV);
        mStudentInfos = new ArrayList<>();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_save) {
            Toast.makeText(getActivity(), "This is menu_save", Toast.LENGTH_SHORT).show();
        }
        if(id == R.id.menu_print) {
            Toast.makeText(getActivity(), "This is menu_print", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);
        infoStudent = (TextView) v.findViewById(R.id.txtInfo);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.fragment_schedule_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new InfomationLoader().execute();



        return v;
    }

    private class TestScheduleHolder extends RecyclerView.ViewHolder {
        private TextView mMaMHTextView;
        private TextView mTenMHTextView;
        private TextView mNhomTextView;
        private TextView mNgayGiuaKi;
        private TextView mGioGiuaKi;
        private TextView mPhongGiuaKi;
        private TextView mNgayCuoiKi;
        private TextView mGioCuoiKi;
        private TextView mPhongCuoiKi;
        private StudentInfo mStudentInfo;

        public TestScheduleHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.list_item_test_schedule, container, false));

            mMaMHTextView = (TextView) itemView.findViewById(R.id.test_schedule_mamh);
            mTenMHTextView = (TextView) itemView.findViewById(R.id.test_schedule_tenmh);
            mNhomTextView = (TextView) itemView.findViewById(R.id.test_schedule_nhom);
            mNgayGiuaKi = (TextView) itemView.findViewById(R.id.test_schedule_midterm_ngay);
            mGioGiuaKi = (TextView) itemView.findViewById(R.id.test_schedule_midterm_gio);
            mPhongGiuaKi = (TextView) itemView.findViewById(R.id.test_schedule_midterm_phong);
            mNgayCuoiKi = (TextView) itemView.findViewById(R.id.test_schedule_final_ngay);
            mGioCuoiKi = (TextView) itemView.findViewById(R.id.test_schedule_final_gio);
            mPhongCuoiKi = (TextView) itemView.findViewById(R.id.test_schedule_final_phong);
        }

        public void bindTestSchedule(StudentInfo studentInfo) {
            mStudentInfo = studentInfo;
            mMaMHTextView.setText(mStudentInfo.getMaMH());
            mTenMHTextView.setText(mStudentInfo.getTenMH());
            mNhomTextView.setText(mStudentInfo.getNhom());
            mNgayGiuaKi.setText(mStudentInfo.getNgayGiuaKi());
            mGioGiuaKi.setText(mStudentInfo.getGioGiuaKi());
            mPhongGiuaKi.setText(mStudentInfo.getPhongGiuaKi());
            mNgayCuoiKi.setText(mStudentInfo.getNgayCuoiKi());
            mGioCuoiKi.setText(mStudentInfo.getGioCuoiKi());
            mPhongCuoiKi.setText(mStudentInfo.getPhongCuoiKi());
        }
    }

    private class TestScheduleAdapter extends RecyclerView.Adapter<TestScheduleHolder> {
        private List<StudentInfo> mStudentInfos;

        public TestScheduleAdapter(List<StudentInfo> studentInfos) {
            mStudentInfos = studentInfos;
        }

        @Override
        public TestScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new TestScheduleHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(TestScheduleHolder holder, int position) {
            StudentInfo studentInfo = mStudentInfos.get(position);
            holder.bindTestSchedule(studentInfo);
        }

        @Override
        public int getItemCount() {
            return mStudentInfos.size();
        }
    }



    private class InfomationLoader extends AsyncTask<Void, Void, Void> {

        private Element mSource;
        private int mNumOfSubject;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setTitle(R.string.get_information);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(mUrl)
                        .data("mssv", mMSSV)
                        .data("HOC_KY", "20151")
                        .post();
                mSource = doc.select("div[align=center]").get(1);
                String info = mSource.select("font > div").text();
                String tenSV = "";
                String lop = "";
                boolean isTenSV = true;
                boolean isLop = false;
                for(int i = 0; i < info.length();i++){
                    if(isTenSV) tenSV = tenSV.concat(String.valueOf(info.charAt(i)));
                    if(info.charAt(i+1) == '(') break;
                    /*if(info.charAt(i+2) == '(') isTenSV = false;
                    if(info.charAt(i + 1) == ':') isLop = true;
                    if(isLop == true){
                        lop = lop.concat(String.valueOf(info.charAt(i + 3)));
                        if(info.charAt(i + 4) == ' ') break;
                    }*/
                }
                infoSV = "Sinh viên: "+tenSV+"\nMSSV: "+mMSSV+"\nHọc kỳ: I (2015 - 2016)";
                mNumOfSubject = mSource.select("table > tbody > tr > td > table > tbody > tr").size() - 2;;

                for(int i = 0; i < mNumOfSubject; i++) {
                    StudentInfo studentInfo = new StudentInfo();
                    studentInfo.setMaMH(mSource.select("table > tbody > tr > td > table > tbody > tr").get(2 + i).select("td").get(0).text().toString());
                    studentInfo.setTenMH(mSource.select("table > tbody > tr > td > table > tbody > tr").get(2 + i).select("td").get(1).text().toString());
                    studentInfo.setNhom(mSource.select("table > tbody > tr > td > table > tbody > tr").get(2 + i).select("td").get(2).text().toString());
                    studentInfo.setNgayGiuaKi(mSource.select("table > tbody > tr > td > table > tbody > tr").get(2 + i).select("td").get(3).text().toString());
                    studentInfo.setGioGiuaKi(mSource.select("table > tbody > tr > td > table > tbody > tr").get(2 + i).select("td").get(4).text().toString());
                    studentInfo.setPhongGiuaKi(mSource.select("table > tbody > tr > td > table > tbody > tr").get(2 + i).select("td").get(5).text().toString());
                    studentInfo.setNgayCuoiKi(mSource.select("table > tbody > tr > td > table > tbody > tr").get(2 + i).select("td").get(6).text().toString());
                    studentInfo.setGioCuoiKi(mSource.select("table > tbody > tr > td > table > tbody > tr").get(2 + i).select("td").get(7).text().toString());
                    studentInfo.setPhongCuoiKi(mSource.select("table > tbody > tr > td > table > tbody > tr").get(2 + i).select("td").get(8).text().toString());
                    mStudentInfos.add(studentInfo);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            infoStudent.setText(infoSV);
            mRecyclerView.setAdapter(new TestScheduleAdapter(mStudentInfos));
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_type_function, menu);
    }
}
