package alberapps.android.cinturondeorion.noticias;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import alberapps.android.cinturondeorion.R;
import alberapps.android.cinturondeorion.noticias.NoticiasFragment.OnListFragmentInteractionListener;
import alberapps.android.cinturondeorion.noticias.dummy.DummyContent.DummyItem;
import alberapps.java.noticias.NoticiasCDO;
import alberapps.java.noticias.rss.NoticiaRss;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyNoticiaRecyclerViewAdapter extends RecyclerView.Adapter<MyNoticiaRecyclerViewAdapter.ViewHolder> {

    private final List<NoticiaRss> mValues;
    private final OnListFragmentInteractionListener mListener;

    public HashMap<String, Bitmap> imagenesCache = new HashMap<String, Bitmap>();

    private Context context;

    private int lastPosition = -1;

    public MyNoticiaRecyclerViewAdapter(List<NoticiaRss> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_noticia, parent, false);

        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getTitle());
        holder.mContentView.setText(mValues.get(position).getDescription());
        holder.position = position;
        holder.mImgView.setTag(mValues.get(position).getUrlPrimeraImagen());


        if (mValues.get(position).getUrlPrimeraImagen() != null && !mValues.get(position).getUrlPrimeraImagen().equals("")) {

            if (imagenesCache.containsKey((String) holder.mImgView.getTag())) {
                holder.mImgView.setVisibility(View.VISIBLE);
                holder.mImgView.setImageBitmap(imagenesCache.get((String) holder.mImgView.getTag()));
            } else {

                // Using an AsyncTask to load the slow images in a
                // background thread
                new AsyncTask<ViewHolder, Void, Bitmap>() {
                    private ViewHolder v;

                    @Override
                    protected Bitmap doInBackground(ViewHolder... params) {
                        v = params[0];

                        Bitmap imagenRecuperada = null;

                        try {
                            imagenRecuperada = NoticiasCDO.recuperaImagen((String) v.mImgView.getTag());

                            Log.d("Twitter2", "Imagen recuperada: " + v.mImgView.getTag());

                        } catch (Exception e) {
                            return null;
                        }

                        return imagenRecuperada;
                    }

                    @Override
                    protected void onPostExecute(Bitmap result) {
                        super.onPostExecute(result);
                        if (v.position == position) {
                            // If this item hasn't been recycled
                            // already,
                            // hide the
                            // progress and set and show the image
                            // v.progress.setVisibility(View.GONE);
                            v.mImgView.setVisibility(View.VISIBLE);
                            v.mImgView.setImageBitmap(result);

                            imagenesCache.put((String) v.mImgView.getTag(), result);

                            Log.d("Twitter2", "Carga imagen");

                        }
                    }
                }.execute(holder);

            }

        }else{

            holder.mImgView.setVisibility(View.GONE);

        }







        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });


        //Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_up);
        //holder.mView.startAnimation(animation);

        /*Animator set = AnimatorInflater.loadAnimator(context, R.animator.slide_in_from_bottom);
        set.setTarget(holder.mView);
        set.start();*/

        setAnimation(holder.mView, position);

    }

    private void setAnimation(View view, int position){

        if(position > lastPosition){
            //Animator set = AnimatorInflater.loadAnimator(context, R.animator.slide_in_from_bottom);

            //set.setTarget(view);
            //set.start();

            //Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_up);
            view.startAnimation(animation);
            lastPosition = position;
        }

    }


    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        ((ViewHolder)holder).clearAnimation();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImgView;
        public NoticiaRss mItem;
        public int position;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mImgView = (ImageView) view.findViewById(R.id.imgNoticia);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        public void clearAnimation(){
            mView.clearAnimation();
        }

    }

    /**
     * Actualizacion de datos
     *
     * @param mDataSet2
     */
    public void addAll(List<NoticiaRss> mDataSet2) {

        mValues.clear();
        mValues.addAll(mDataSet2);
        notifyDataSetChanged();

    }

}
