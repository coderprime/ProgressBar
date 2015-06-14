package ray.cyberpup.com.progressbardrill;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created on 6/13/15
 *
 * @author Raymond Tong
 */
public class PrimesFragment extends Fragment {

    TaskListener mListener;
    PrimesTask mTask;
    interface TaskListener{

        public void onPreExecute();
        public void onProgressUpdate(Integer... progress);
        public void onPostExecute(Integer result);
        public void onCancelled(Integer result);
    }

    public static PrimesFragment getInstance(int input){

        PrimesFragment taskFragment = new PrimesFragment();
        Bundle args = new Bundle();
        args.putInt("input",input);

        taskFragment.setArguments(args);
        return taskFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        mTask = new PrimesTask();
        mInput = getArguments().getInt("input");
        mTask.execute(mInput);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{

            mListener = (TaskListener)activity;
        } catch(ClassCastException e){
            throw new ClassCastException(activity.toString() +
                    " must implement TaskListener");
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    void cancel() {
        mTask.cancel(true);
    }


    int mInput;
    private class PrimesTask extends AsyncTask<Integer, Integer, Integer>{

        @Override
        protected void onPreExecute() {

            if(mListener!=null)
                mListener.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(mListener!=null)
                mListener.onPostExecute(result);

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

            int percentCompleted = (int)(progress[0]*100f/mInput);
            if(mListener!=null)
                mListener.onProgressUpdate(percentCompleted);



        }

        @Override
        protected Integer doInBackground(Integer... params) {

            int allPrimesUpToThisNumber = params[0];
            calculatePrimes(allPrimesUpToThisNumber);


            return 0;

        }

        private void calculatePrimes(int n) {

            for (int i = 0; i <= n; i++) {

                isPrime(i);

                publishProgress(i);

                if (isCancelled()) {
                    break;
                }
            }

        }

        private boolean isPrime(int n) {

            if (n < 2)
                return false;
            int i = 2;
            while (i * i <= n) {
                if (n % i == 0)
                    return false;

                i++;
            }
            return true;
        }

        @Override
        protected void onCancelled(Integer result) {
            if(mListener!=null)
                mListener.onCancelled(result);


        }
    }
}
