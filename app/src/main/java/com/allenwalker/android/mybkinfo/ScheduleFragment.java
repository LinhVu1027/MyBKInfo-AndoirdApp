package com.allenwalker.android.mybkinfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleFragment extends Fragment {

    private static final String ARG_MSSV = "arg_mssv";

    private final String mUrl = "http://www.aao.hcmut.edu.vn/image/data/Tra_cuu/xem_tkb";

    private ProgressDialog mProgressDialog;
    private String mMSSV;
    public String infoSV;
    private TextView infoStudent;
    private RecyclerView mRecyclerView;
    private List<StudentInfo> mStudentInfos;

    public static ScheduleFragment newInstance(String mssv) {
        Bundle args = new Bundle();
        args.putString(ARG_MSSV, mssv);

        ScheduleFragment fragment = new ScheduleFragment();
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

    private class ScheduleHolder extends RecyclerView.ViewHolder {
        private TextView mMaMHTextView;
        private TextView mTenMHTextView;
        private TextView mTinChiTextView;
        private TextView mTinChiHocTextView;
        private TextView mNhomTextView;
        private TextView mThuTextView;
        private TextView mTietTextView;
        private TextView mPhongTextView;
        private TextView mTuanHocTextView;
        private StudentInfo mStudentInfo;

        public ScheduleHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.list_item_schedule, container, false));

            mMaMHTextView = (TextView) itemView.findViewById(R.id.schedule_mamh);
            mTenMHTextView = (TextView) itemView.findViewById(R.id.schedule_tenmh);
            mTinChiTextView = (TextView) itemView.findViewById(R.id.schedule_tinchi);
            mTinChiHocTextView = (TextView) itemView.findViewById(R.id.schedule_tinchihoc);
            mNhomTextView = (TextView) itemView.findViewById(R.id.schedule_nhom);
            mThuTextView = (TextView) itemView.findViewById(R.id.schedule_thu);
            mTietTextView = (TextView) itemView.findViewById(R.id.schedule_tiet);
            mPhongTextView = (TextView) itemView.findViewById(R.id.schedule_phong);
            mTuanHocTextView = (TextView )itemView.findViewById(R.id.schedule_tuanhoc);
        }

        public void bindSchedule(StudentInfo studentInfo) {
            mStudentInfo = studentInfo;
            mMaMHTextView.setText(mStudentInfo.getMaMH());
            mTenMHTextView.setText(mStudentInfo.getTenMH());
            mTinChiTextView.setText(mStudentInfo.getTinChi());
            mTinChiHocTextView.setText(mStudentInfo.getTinChiHocPhi());
            mNhomTextView.setText(mStudentInfo.getNhom());
            mThuTextView.setText(mStudentInfo.getThu());
            mTietTextView.setText(mStudentInfo.getTiet());
            mPhongTextView.setText(mStudentInfo.getPhongHoc());
            mTuanHocTextView.setText(mStudentInfo.getTuanHoc());
        }
    }

    private class ScheduleAdapter extends RecyclerView.Adapter<ScheduleHolder> {
        private List<StudentInfo> mStudentInfos;

        public ScheduleAdapter(List<StudentInfo> studentInfos) {
            mStudentInfos = studentInfos;
        }

        @Override
        public ScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new ScheduleHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(ScheduleHolder holder, int position) {
            StudentInfo studentInfo = mStudentInfos.get(position);
            holder.bindSchedule(studentInfo);
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
                    if(info.charAt(i+2) == '(') isTenSV = false;
                    if(info.charAt(i + 1) == ':') isLop = true;
                    if(isLop == true){
                        lop = lop.concat(String.valueOf(info.charAt(i + 3)));
                        if(info.charAt(i + 4) == ' ') break;
                    }
                }
                infoSV = "Sinh viên: "+tenSV+"\nMSSV: "+mMSSV+"\nLớp: "+lop+"\nHọc kỳ: I (2015 - 2016)";
                mNumOfSubject = mSource.select("table > tbody > tr > td > table > tbody > tr").size() - 1;

                for(int i = 0; i < mNumOfSubject; i++) {
                    StudentInfo studentInfo = new StudentInfo();
                    studentInfo.setMaMH(mSource.select("table > tbody > tr > td > table > tbody > tr").get(1 + i).select("td").get(0).text().toString());
                    studentInfo.setTenMH(mSource.select("table > tbody > tr > td > table > tbody > tr").get(1 + i).select("td").get(1).text().toString());
                    studentInfo.setTinChi(mSource.select("table > tbody > tr > td > table > tbody > tr").get(1 + i).select("td").get(2).text().toString());
                    studentInfo.setTinChiHocPhi(mSource.select("table > tbody > tr > td > table > tbody > tr").get(1 + i).select("td").get(3).text().toString());
                    studentInfo.setNhom(mSource.select("table > tbody > tr > td > table > tbody > tr").get(1 + i).select("td").get(5).text().toString());
                    studentInfo.setThu(mSource.select("table > tbody > tr > td > table > tbody > tr").get(1 + i).select("td").get(6).text().toString());
                    studentInfo.setTiet(mSource.select("table > tbody > tr > td > table > tbody > tr").get(1 + i).select("td").get(7).text().toString());
                    studentInfo.setPhongHoc(mSource.select("table > tbody > tr > td > table > tbody > tr").get(1 + i).select("td").get(8).text().toString());
                    studentInfo.setTuanHoc(mSource.select("table > tbody > tr > td > table > tbody > tr").get(1 + i).select("td").get(9).text().toString());
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
            mRecyclerView.setAdapter(new ScheduleAdapter(mStudentInfos));
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_type_function, menu);
    }
}
