package br.com.gjmoretti;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.ListActivity;

public class PreloadedDatabase extends ListActivity {
    
	/** Called when the activity is first created. */
	
	// ABRE o banco de dados:
	SQLiteDatabase bancoDados = null;	
	
	// O cursor permite a busca dos dados através de instruções SQL e a navegação:
	Cursor cursor;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.main);
        
        // Locale local = new Locale("pt","BR");         
        // Locale.setDefault(local);    
        // Configuration config = new Configuration();
        // config.locale = local;
        // getBaseContext().getResources().updateConfiguration(config, null);
        
        criarDatabase();
        abrirDatabase();
        
        if (buscarDados()) {
        	String[] dados = new String[cursor.getCount()]; // Reserva a quantidade de registros do cursor na variável "dados"
        	cursor.moveToFirst();        	
        	        	
            while (cursor.isAfterLast() == false) {
            	dados[cursor.getPosition()] = cursor.getString(cursor.getColumnIndex("NOME"));                
           	    cursor.moveToNext();
            }
        	
            cursor.close(); 
            
            fecharDatabase();
        	
        	setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, dados));

            ListView lista = getListView();
            lista.setTextFilterEnabled(true);
            
            lista.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {                      
                      Toast.makeText(getApplicationContext(), ((TextView) view).getText(),Toast.LENGTH_SHORT).show();
                    }
            	
    		});
        }
    }
    
    
    public void  criarDatabase()
    {
        DataBaseHelper myDbHelper = new DataBaseHelper(this);
        // myDbHelper = new DataBaseHelper(this);
 
        try {
        	myDbHelper.createDataBase();        	
        	// ExibirMensagem("Atenção", "Banco de dados criado com sucesso.");        	 
 	    }
        catch (Exception erro) {
        	ExibirMensagem("Atenção", "Não foi possível criar o banco de dados.");        	
		}
    }
    

    public void  abrirDatabase()
    {
    	try {
    		// Abre o banco de dados:    		
    		bancoDados = openOrCreateDatabase("contatos.sqlite", MODE_WORLD_READABLE, null);
    		// bancoDados.setLocale(Locale.getDefault());
    		
    	}
    	catch (Exception erro) {
			ExibirMensagem("Atenção", "Ocorreu um problema ao abrir o banco de dados: " + erro.getMessage());			
		}
    }
    
    public void  fecharDatabase()
    {
    	try {
    		// Fecha o banco de dados: 
    		bancoDados.close();
    	}
    	catch (Exception erro) {
			ExibirMensagem("Atenção", "Ocorreu um problema ao fechar o banco de dados: " + erro.getMessage());			
		}
    }
    
    
    
    private boolean buscarDados()
    {
    	try {    		
    		
    		// A select é criada aqui:
    		cursor = bancoDados.query("CONTATOS", 
    				                  new String [] {"ID_CONTATO", "NOME", "ENDERECO", "DATA_NASCIMENTO"}, 
    				                  null,  // selection, 
    				                  null,  // selectionArgs, 
    				                  null,  // groupBy, 
    				                  null,  // having, 
    				                  null); // orderBy);
    		
    		int numeroRegistros = cursor.getCount();
    		
    		if (numeroRegistros > 0) // Nota: "!=" é o mesmo que "<>" no Pascal
    		{
    			// cursor.moveToFirst();
    			// cursor.close();
    			return true;
    		}
    		else
    		return false;
    		
    	}
    	catch (Exception erro) {
    		ExibirMensagem("Atenção", "Ocorreu um problema ao buscar os dados no banco: " + erro.getMessage());
    		return false;
		}
    }
    
    
    // Método para mensagens dinâmicas:    
    public void ExibirMensagem(String pTitulo, String pMensagem)
    {
		AlertDialog.Builder Mensagem = new AlertDialog.Builder(PreloadedDatabase.this);
		Mensagem.setTitle(pTitulo);
		Mensagem.setMessage(pMensagem);
		Mensagem.setNeutralButton("Ok", null);
		Mensagem.show();
    }
    
}