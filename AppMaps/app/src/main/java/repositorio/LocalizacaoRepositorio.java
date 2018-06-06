package repositorio;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Database.Database;
import model.Localizacao;

public class LocalizacaoRepositorio {
    private Database dbHelper;
    private SQLiteDatabase database;


    public LocalizacaoRepositorio(Context context){
        dbHelper = new Database(context);
        database = dbHelper.getWritableDatabase();
    }

    /*
    * Inserindo no banco de dados
    */
    public void insertLocalizacao(Localizacao localizacao){
        database.execSQL("INSERT INTO localizacao (latitude, longitude, descricao) " +
                "VALUES ('"+localizacao.getLatitude()+"'," +
                " '"+localizacao.getLongitude()+"'," +
                " '"+localizacao.getDescricao()+"')");
    }

    /*
    *Buscanco no banco  de dados
    */
    public Iterator<Localizacao> getLocations(){
        List<Localizacao> lista = new ArrayList();
        try{


            Cursor cursor = database.rawQuery("SELECT id, latitude, longitude, descricao FROM localizacao"
                    ,null);

            //recuperando indices da coluna
            int indiceId = cursor.getColumnIndex("id");
            int indiceLatitude = cursor.getColumnIndex("latitude");
            int indiceLongitude = cursor.getColumnIndex("longitude");
            int indiceDescricao = cursor.getColumnIndex("descricao");
            cursor.moveToFirst();


            while ( cursor != null  ){
                int id = cursor.getInt(indiceId);
                double lat = cursor.getDouble(indiceLatitude);
                double lng = cursor.getDouble(indiceLongitude);
                String desc = cursor.getString(indiceDescricao);

                Localizacao localizacao = new Localizacao(id, lat, lng, desc);
                lista.add(localizacao);
                cursor.moveToNext();
            }
            cursor.moveToFirst();
        }catch (Exception e){
            e.printStackTrace();
        }

        return lista.iterator();
    }
}
