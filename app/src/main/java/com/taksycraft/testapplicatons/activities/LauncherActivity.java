package com.taksycraft.testapplicatons.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.taksycraft.testapplicatons.R;

import java.util.ArrayList;
import java.util.List;
//https://www.androidauthority.com/make-a-custom-android-launcher-837342-837342/
public class LauncherActivity extends AppCompatActivity {

    private ArrayList<AppInfo> appsList;
    private RecyclerView rv;
    private RAdapter adapter;
    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());
        setContentView(R.layout.activity_launcher);
        getSupportActionBar().hide();
        ImageView chromeIcon = (ImageView) findViewById(R.id.chromeButton);
        rv = (RecyclerView) findViewById(R.id.rv);
        chromeIcon.setImageDrawable(  getActivityIcon(this,"com.android.chrome",
                "com.google.android.apps.chrome.Main"));

        rv.setLayoutManager(new GridLayoutManager(this,5));
        adapter = new RAdapter(this);
        rv.setAdapter(adapter);



    }
    public static Drawable getActivityIcon(Context context, String packageName, String activityName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityName));
        ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);

        return resolveInfo.loadIcon(pm);
    }
    public class AppInfo {
        CharSequence label;
        CharSequence packageName;
        Drawable icon;
    }
    public class RAdapter extends RecyclerView.Adapter<RAdapter.ViewHolder> {
        private List<AppInfo> appsList;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView textView;
            public ImageView img;


            //This is the subclass ViewHolder which simply
            //'holds the views' for us to show on each row
            public ViewHolder(View itemView) {
                super(itemView);

                //Finds the views from our row.xml
                textView = (TextView) itemView.findViewById(R.id.text);
                img = (ImageView) itemView.findViewById(R.id.img);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick (View v) {
                int pos = getAdapterPosition();
                Context context = v.getContext();

                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(appsList.get(pos).packageName.toString());
                context.startActivity(launchIntent);
                Toast.makeText(v.getContext(), appsList.get(pos).label.toString(), Toast.LENGTH_LONG).show();

            }
        }



        public RAdapter(Context c) {

            //This is where we build our list of app details, using the app
            //object we created to store the label, package name and icon

            PackageManager pm = c.getPackageManager();
            appsList = new ArrayList<AppInfo>();

            Intent i = new Intent(Intent.ACTION_MAIN, null);
            i.addCategory(Intent.CATEGORY_LAUNCHER);

            List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);
            for(ResolveInfo ri:allApps) {
                AppInfo app = new AppInfo();
                app.label = ri.loadLabel(pm);
                app.packageName = ri.activityInfo.packageName;
                app.icon = ri.activityInfo.loadIcon(pm);
                appsList.add(app);
            }

        }

        @Override
        public void onBindViewHolder(RAdapter.ViewHolder viewHolder, int i) {

            //Here we use the information in the list we created to define the views

            String appLabel = appsList.get(i).label.toString();
            String appPackage = appsList.get(i).packageName.toString();
            Drawable appIcon = appsList.get(i).icon;

            TextView textView = viewHolder.textView;
            textView.setText(appLabel);
            ImageView imageView = viewHolder.img;
            imageView.setImageDrawable(appIcon);
        }


        @Override
        public int getItemCount() {

            //This method needs to be overridden so that Androids knows how many items
            //will be making it into the list

            return appsList.size();
        }


        @Override
        public RAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            //This is what adds the code we've written in here to our target view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View view = inflater.inflate(R.layout.app_row, parent, false);

            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }
    }
}
