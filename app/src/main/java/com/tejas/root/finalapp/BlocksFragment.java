package com.tejas.root.finalapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tejas.root.finalapp.databases.DatabaseHandler;

/**
 * Created by root on 24/12/18.
 */

public class BlocksFragment extends Fragment implements View.OnClickListener {

    TextView java,cplus,signin,settings,php,share,removeAdd,javascript,jquery;
    Context context;
    MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.blocks_fragment,container,false);

        java = view.findViewById(R.id.javaButton);
        cplus = view.findViewById(R.id.cPlusButton);
        javascript = view.findViewById(R.id.javascriptButton);
        jquery = view.findViewById(R.id.jqueryButton);
        signin = view.findViewById(R.id.signinButton);
        settings = view.findViewById(R.id.settingsButton);
        php = view.findViewById(R.id.phpButton);
        share = view.findViewById(R.id.shareButton);
        removeAdd = view.findViewById(R.id.removeAdds);

        settings.setOnClickListener(this);
        java.setOnClickListener(this);
        signin.setOnClickListener(this);
        cplus.setOnClickListener(this);
        php.setOnClickListener(this);
        javascript.setOnClickListener(this);
        jquery.setOnClickListener(this);
        share.setOnClickListener(this);
        removeAdd.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        activity = (MainActivity) context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.javaButton:
                Fragment fragment = new ListFragment();
                Bundle javaBundle = new Bundle();
                javaBundle.putString("type","java");
                fragment.setArguments(javaBundle);
                FragmentTransaction tx = getFragmentManager().beginTransaction().
                        replace(R.id.below_fragment,fragment).addToBackStack(null);
                tx.commit();
                break;
            case R.id.signinButton:
                Fragment signInFragment = new SignInFragment();

                FragmentTransaction tx1 = getFragmentManager().beginTransaction().
                        replace(R.id.below_fragment,signInFragment).addToBackStack(null);
                tx1.commit();
                break;
            case R.id.settingsButton:
                Fragment settingsFragment = new SettingsFragment();

                FragmentTransaction tx2 = getFragmentManager().beginTransaction().
                        replace(R.id.below_fragment,settingsFragment).addToBackStack(null);
                tx2.commit();
                break;

            case R.id.cPlusButton:
                /*
                DatabaseHandler.getInstance(context).getAppDatabase()
                        .getTestDao().deleteRecords(activity.paperInformations);
                */

                Fragment cplusFragment = new ListFragment();
                Bundle cplusBundle = new Bundle();
                cplusBundle.putString("type","c++");
                cplusFragment.setArguments(cplusBundle);
                FragmentTransaction txcplus = getFragmentManager().beginTransaction().
                        replace(R.id.below_fragment,cplusFragment).addToBackStack(null);
                txcplus.commit();
                break;
            case R.id.phpButton:
                Fragment phpFragment = new ListFragment();
                Bundle phpBundle = new Bundle();
                phpBundle.putString("type","php");
                phpFragment.setArguments(phpBundle);
                FragmentTransaction txPhp = getFragmentManager().beginTransaction().
                        replace(R.id.below_fragment,phpFragment).addToBackStack(null);
                txPhp.commit();
                break;
                /*
                Fragment phpFragment = new PHPFragment();

                FragmentTransaction tx3 = getFragmentManager().beginTransaction().
                        replace(R.id.below_fragment,phpFragment).addToBackStack(null);
                tx3.commit();*/

            case R.id.removeAdds:

                if(DatabaseHandler.getInstance(context).getAppDatabase().getTestDao()
                        .getTotalCoins()>1000){
                    Toast.makeText(context,"This option is under development",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context,"You haven't reach upto 1000 coins...",Toast.LENGTH_LONG).show();
                }

                /*
                final Dialog dialog = new Dialog(context);
                Button processButton;
                final ImageView closeImage;

                dialog.setContentView(R.layout.remove_add_layout);
                processButton = dialog.findViewById(R.id.proceedButton);
                closeImage = dialog.findViewById(R.id.closeImg);

                closeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                processButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context,"Payment processed", Toast.LENGTH_SHORT).show();
                        Order order = new Order(context);
                        order.startOrder("10.00");
                        order.intiatePayment();
                    }
                });

                dialog.show();
*/
                break;
            case R.id.shareButton:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.tejas.root.finalapp");

                startActivity(Intent.createChooser(intent,"Choose the application"));

                break;
            case R.id.javascriptButton:
                Fragment javascriptFragment = new ListFragment();
                Bundle javascriptBundle = new Bundle();
                javascriptBundle.putString("type","javascript");
                javascriptFragment.setArguments(javascriptBundle);
                FragmentTransaction txJavascript = getFragmentManager().beginTransaction().
                        replace(R.id.below_fragment,javascriptFragment).addToBackStack(null);
                txJavascript.commit();
                break;
            case R.id.jqueryButton:
                Fragment jqueryFragment = new ListFragment();
                Bundle jqueryBundle = new Bundle();
                jqueryBundle.putString("type","jquery");
                jqueryFragment.setArguments(jqueryBundle);
                FragmentTransaction txJquery = getFragmentManager().beginTransaction().
                        replace(R.id.below_fragment,jqueryFragment).addToBackStack(null);
                txJquery.commit();
                break;
        }
    }
}
