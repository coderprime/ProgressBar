package ray.cyberpup.com.progressbardrill;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

/**
 * Created on 6/13/15
 *
 * @author Raymond Tong
 */
public class Progress extends AppCompatActivity implements PrimesFragment.TaskListener {

    private static final String PRIMES = "primesfrag";

    private ProgressBar mBar=null;
    private EditText mEntry=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_main);

        final Button start = (Button) findViewById(R.id.button_start);
        final Button cancel = (Button) findViewById(R.id.button_cancel);


        mEntry = (EditText)findViewById(R.id.input);
        mBar = (ProgressBar) findViewById(R.id.progress);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                beginTask();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PrimesFragment frag = (PrimesFragment)
                        getSupportFragmentManager().findFragmentByTag(PRIMES);
                if(frag !=null)
                    frag.cancel();

            }
        });

    }

    int mInput;
    private void beginTask(){
        if(mEntry.getText().length()!=0) {

            System.out.println("starting task");

            mInput = Integer.parseInt(mEntry.getText().toString());
            PrimesFragment primeFrag = (PrimesFragment)
                    getSupportFragmentManager().findFragmentByTag(PRIMES);

            if (primeFrag == null) {
                FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
                PrimesFragment frag = PrimesFragment.getInstance(mInput);
                tran.add(frag, PRIMES).commit();
            }

        }
    }


    @Override
    public void onPreExecute() {
        mBar = (ProgressBar) findViewById(R.id.progress);
        mBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressUpdate(Integer... progress) {

        mBar.setProgress(progress[0]);

    }

    @Override
    public void onPostExecute(Integer result) {

        System.out.println("onPostExecute Activity");
        mBar.setVisibility(View.INVISIBLE);
        cleanUp();

    }

    @Override
    public void onCancelled(Integer result) {

        System.out.println("onCancelled Activity");
        mBar.setVisibility(View.INVISIBLE);
        cleanUp();
    }

    private void cleanUp(){

        FragmentManager fm = getSupportFragmentManager();
        Fragment primeFrag = fm.findFragmentByTag(PRIMES);
        fm.beginTransaction().remove(primeFrag).commit();

    }
}
