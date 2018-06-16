package com.project.sagi.queue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.List;

public class AdapterBusiness extends ArrayAdapter<clsBusiness> {
    private List<clsBusiness> _businesses;

    public AdapterBusiness(Context context, List<clsBusiness> businesses) {
        super(context, 0, businesses);
        _businesses = businesses;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int finalPosition = position;
        clsBusiness business = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.worker_card_row, parent, false); //-----
        }
//        TextView lblUserName = (TextView) convertView.findViewById(R.id.lblUserName);
//        TextView lblWorkerWorkTypes = (TextView) convertView.findViewById(R.id.lblWorkerWorkTypes);
//        CircularImageView imgWorkerImage = (CircularImageView) convertView.findViewById(R.id.imgWorkerImage);
//
//        if (worker.WorkerName != null) {
//            lblUserName.setText(worker.WorkerName.toString());
//        } else {
//            lblUserName.setText("");
//        }
//        if (worker.WorkerWorkTypes != null && worker.WorkerWorkTypes.size() > 0) {
//            StringBuilder workerTypeDesc = new StringBuilder();
//
//            for(clsWorkType workerType : worker.WorkerWorkTypes){
//                workerTypeDesc.append(workerType.WorkType);
//                workerTypeDesc.append(",");
//            }
//            String workerTypeDescStr = workerTypeDesc.substring(0, workerTypeDesc.length() - 1);
//
//            lblWorkerWorkTypes.setText(workerTypeDescStr);
//        } else {
//            lblWorkerWorkTypes.setText("");
//        }

        return convertView;
    }
}