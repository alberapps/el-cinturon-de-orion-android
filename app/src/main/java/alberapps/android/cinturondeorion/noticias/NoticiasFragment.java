/**
 * El Cinturon de Orion - Inoformacion y podcast del programa de radio El Cinturon de Orion de Radio San Vicente
 * Copyright © 2016 Alberto Montiel
 * <p/>
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package alberapps.android.cinturondeorion.noticias;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import alberapps.android.cinturondeorion.R;
import alberapps.android.cinturondeorion.tasks.LoadNoticiasRssAsyncTask;
import alberapps.java.noticias.rss.NoticiaRss;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class NoticiasFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private AsyncTask<Object, Void, List<NoticiaRss>> loadNoticiasRssTask;
    private List<NoticiaRss> noticiasRss = new ArrayList<>();

    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefresh = null;

    private View emptyView = null;
    private View cargandoView = null;

    private int filtroNoticias = 0;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NoticiasFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NoticiasFragment newInstance(int columnCount) {
        NoticiasFragment fragment = new NoticiasFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noticia_list, container, false);

        View recyler = view.findViewById(R.id.list);

        // Set the adapter
        if (recyler instanceof RecyclerView) {
            Context context = recyler.getContext();
            recyclerView = (RecyclerView) recyler;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyNoticiaRecyclerViewAdapter(noticiasRss, mListener));

            // Swipe para recargar
            swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshPrincipal);
            swipeRefresh.setColorSchemeResources(R.color.material_grey_400, R.color.material_grey_200, R.color.material_grey_400, R.color.material_grey_200);
            swipeRefresh.setOnRefreshListener(this);

            emptyView = view.findViewById(R.id.empty_view);
            cargandoView = view.findViewById(R.id.cargando_view);


            consultarNoticias(filtroNoticias);

        }




        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {

        consultarNoticias(filtroNoticias);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(NoticiaRss item);
    }


    /**
     * Carga el listado
     */
    public void cargarListadoRss() {

        try {

            if (noticiasRss != null) {

                //cargarHeaderNoticiasRss();

                ((MyNoticiaRecyclerViewAdapter)recyclerView.getAdapter()).addAll(noticiasRss);
                //recyclerView.getAdapter().notifyDataSetChanged();



            } else {
                Toast.makeText(getActivity(), getString(R.string.error_datos), Toast.LENGTH_SHORT).show();
            }

            if(noticiasRss == null || noticiasRss.isEmpty()){
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                cargandoView.setVisibility(View.GONE);
            }else{
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                cargandoView.setVisibility(View.GONE);
            }


        } catch (Exception e) {

            // Para evitar fallos en caso de volver antes de terminar
            e.printStackTrace();

        }

    }


    public void consultarNoticias(int filtro){


        filtroNoticias = filtro;

        swipeRefresh.setRefreshing(true);

        /**
         * Sera llamado cuando la tarea de cargar las noticias
         */
        LoadNoticiasRssAsyncTask.LoadNoticiasRssAsyncTaskResponder loadNoticiasRssAsyncTaskResponder = new LoadNoticiasRssAsyncTask.LoadNoticiasRssAsyncTaskResponder() {
            public void noticiasRssLoaded(List<NoticiaRss> noticias) {

                if (noticias != null && !noticias.isEmpty()) {
                    noticiasRss = noticias;
                    cargarListadoRss();

                } else {

                    noticiasRss = null;
                    // Error al recuperar datos
                    cargarListadoRss();

                }

                swipeRefresh.setRefreshing(false);

                /*if (noticiasRssView != null) {
                    // Quitar barra progreso inicial
                    ProgressBar lpb = (ProgressBar) findViewById(R.id.progreso_rss);
                    lpb.clearAnimation();
                    lpb.setVisibility(View.INVISIBLE);

                    if (noticias == null || noticias.isEmpty()) {
                        TextView vacio = (TextView) findViewById(R.id.vacio_noticias_rss);
                        noticiasRssView.setEmptyView(vacio);
                    }
                }*/

            }
        };


        // Control de disponibilidad de conexion
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            loadNoticiasRssTask = new LoadNoticiasRssAsyncTask(loadNoticiasRssAsyncTaskResponder).execute(filtroNoticias);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_red), Toast.LENGTH_LONG).show();

            //setRefreshActionItemState(false);

            /*if (dialog != null && dialog.isShowing()) {

                dialog.dismiss();

            }*/
        }





    }



}
