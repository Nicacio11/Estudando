package testtcc.henriqueethiago.com.appmaps;

import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import model.Distancia;
import model.Localizacao;
import repositorio.LocalizacaoRepositorio;

public class ProvideFragment_1 extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private static final String TAG = "ProvideFragment_1";
    private GoogleMap mMap;
    private LocationManager locationManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            //Mostra a localização atual
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            //Serve pra realizar busca para o melhor provide
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);

            //Ira mostrar o nome do Provider
            Toast.makeText(getActivity(), "Provider: " + provider, Toast.LENGTH_LONG).show();

            // instancia do google maps
            mMap = googleMap;

            // Reconheci e guarda os "clicks" no app
            mMap.setOnMapClickListener(this);

            // adiciona opção de zoom
            mMap.getUiSettings().setZoomControlsEnabled(true);

            mMap.setMyLocationEnabled(true);

            LocalizacaoRepositorio lr = new LocalizacaoRepositorio(getActivity());
            Iterator list = lr.getLocations();
            Localizacao localizacao;

            while(list.hasNext()){
                localizacao =(Localizacao) list.next();
                LatLng loc = new LatLng(localizacao.getLatitude(), localizacao.getLongitude());
                mMap.addMarker(new MarkerOptions().position(loc).title(localizacao.getDescricao()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
            }

            long TEMPO = (1000 * 10); // chama o método a cada 10 segundos
            Timer timer = null;
            if (timer == null) {
                timer = new Timer();
                TimerTask tarefa = new TimerTask() {
                    public void run() {
                        try {
                            verificarDistancia();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                timer.scheduleAtFixedRate(tarefa, TEMPO, TEMPO);
            }
        }catch (SecurityException ex)
        {
            Log.e(TAG, "Erro", ex);
        }

    }

    public  void atualizar(){
        onMapReady(mMap);
    }


    @Override
    public void onMapClick(final LatLng latLng) {
        //Toast.makeText(getContext(), "Coordenadas: " + latLng.toString(), Toast.LENGTH_SHORT).show();

        /*
        * Alerta de confirmação
        */
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View mView = getLayoutInflater().inflate(R.layout.localizacao_create, null);
        final EditText editView = mView.findViewById(R.id.descricao);
        builder.setView(mView)
                .setTitle("Salvar Favorito")
                .setMessage("Deseja realmente salvar esse local?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                       // Toast.makeText(getContext(), "Coordenadas: " + latLng.latitude, Toast.LENGTH_LONG).show();

                        String desc = editView.getText().toString();
                        Toast.makeText(getContext(), "Coordenadas: " + desc, Toast.LENGTH_LONG).show();
                        Localizacao localizacao = new Localizacao(latLng.latitude, latLng.longitude, desc);
                        LocalizacaoRepositorio localizacaoRepositorio = new LocalizacaoRepositorio(getContext());

                        localizacaoRepositorio.insertLocalizacao(localizacao);
                        atualizar();
                    }
                })
            .setNegativeButton("Não", null)
            .show();


    }


    private void verificarDistancia(){
        //pegando a localização inicial
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        LatLng latitudeInicial = new LatLng(latitude, longitude);


        //pegando a localização final
        LocalizacaoRepositorio lr = new LocalizacaoRepositorio(getActivity());
        Iterator list = lr.getLocations();
        Localizacao localizacao;

        while(list.hasNext()){
            localizacao =(Localizacao) list.next();
            LatLng localizacaoFinal = new LatLng(localizacao.getLatitude(), localizacao.getLongitude());
            Distancia distancia = comparando(latitudeInicial, localizacaoFinal);
            //Toast.makeText(getContext(), "Você está a "+ (distancia.getDistancia()+distancia.getUnidade()) + " de "+ localizacao.getDescricao(), Toast.LENGTH_SHORT).show();
            Log.i("LOG", "Você está a "+ (distancia.getDistancia()+distancia.getUnidade()) + " de "+ localizacao.getDescricao());
        }

    }
    private Distancia comparando(LatLng inicial, LatLng lFinal) {
        double distancia = SphericalUtil.computeDistanceBetween(inicial, lFinal);
        String unit = "m";
        if (distancia >= 1000){
            distancia /= 1000;
            unit = "km";
        }
        Distancia dist = new Distancia(distancia, unit);
        return dist;
    }
}
