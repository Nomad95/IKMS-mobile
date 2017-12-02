/*
package com.pollub.ikms.ikms_mobile;

import android.app.Fragment;
import android.view.View;

*/
/**
 * Created by ATyKondziu on 11.11.2017.
 *//*


public class MyNotificationFragment extends Fragmentimplements implements TaskListener, View.OnClickListener {

    private ProgressDialog progressDialog;
    private boolean isTaskRunning = false;
    private AsyncTaskExample asyncTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // If we are returning here from a screen orientation
        // and the AsyncTask is still working, re-create and display the
        // progress dialog.
        if (isTaskRunning) {
            progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please wait a moment!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        Button button = (Button) view.findViewById(R.id.start);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (!isTaskRunning) {
            asyncTask = new AsyncTaskExample(this);
            asyncTask.execute();
        }
    }

    @Override
    public void onTaskStarted() {
        isTaskRunning = true;
        progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please wait a moment!");
    }

    @Override
    public void onTaskFinished(String result) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        isTaskRunning = false;
    }

    @Override
    public void onDetach() {
        // All dialogs should be closed before leaving the activity in order to avoid
        // the: Activity has leaked window com.android.internal.policy... exception
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDetach();
    } {
}
*/
