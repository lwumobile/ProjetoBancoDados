// http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/

package br.com.gjmoretti;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper{
	 
	// O caminho (path) padr�o para o banco de dados da aplica��o no Android: /data/data/package/databases/
    private static String DB_PATH = "/data/data/br.com.gjmoretti/databases/";

    // Nome do arquivo de banco de dados SqLite 
    private static String DB_NAME = "contatos.sqlite";

    private SQLiteDatabase myDataBase; 
    private final Context myContext;
 
    public DataBaseHelper(Context context) {
        /**
         * Constructor
         * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
         * @param context
         */ 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
 
    
    public void createDataBase() throws IOException{
        /**
         * Cria um banco de dados vazio no sistema, que � substituido posteriormente com seu pr�prio banco de dados, copiado do diret�rio "assets".
         * */
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		// Nada a ser feito - O banco de dados j� existe
    	}else{
 
    		// O m�todo getReadableDatabase criar� um banco de dados vazio no caminho (path) padr�o do aplicativo. 
            // Este banco de dados ser� subustituido posteriormente pelo banco de dados pr�-carregado:            
    		this.getReadableDatabase();
 
        	try {
        		
        		// Faz a c�pia do banco de dados pr�-carregado no diret�rio "assets" para caminho (path) padr�o do aplicativo, substituindo o banco de dados criado anteriormente:                
    			copyDataBase();    			
 
    		} catch (IOException e) {
 
        		throw new Error("Erro ao copiar o Banco de Dados.");

        	}
    	}
     }
   
    
    private boolean checkDataBase(){
        /**
         * Verifica se o banco de dados j� existe para evitar copiar o arquivo cada vez que voc� abrir o aplicativo.
         * @return "true" se o banco de dados j� existir, "false" se n�o existir
        */
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		// Tenta abrir o banco de dados no caminho (path) padr�o do aplicativo...
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){ 
    		// Se ocorrer uma exce��o significa que o bnco de dados n�o foi criado ainda. 
    	}
 
    	if (checkDB != null){
    		// Se "checkDB" n�o estiver com valor nulo, significa que um banco de dados foi encontrado e aberto. 
    		// Como se trata apenas de um teste, devemos fechar a conex�o que foi aberta com o banco de dados:
    		checkDB.close(); 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    private void copyDataBase() throws IOException{
        /**
         * Faz a c�pia o banco de dados do diret�rio "assets" para o banco de dados vazio e rec�m-criado.     
         * */    	
    	
    	// Acessa o banco de dados no diret�rio "assets", que � o nosso arquivo origem (inputfile)
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Monta o caminho para acessar o banco de dados vazio, criado anteriormente (ver m�todo "createDataBase"):
    	String outFileName = DB_PATH + DB_NAME;
 
    	// Acessa o banco de dados vazio, que � o nosso arquivo destino (outputfile):
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	// Transfere os bytes do arquivo origem (inputfile) para o arquivo de destino (outputfile):
    	byte[] buffer = new byte[1024];
    	int length;
    	// Enquanto houverem bytes a serem transferidos, escreve o arquivo de destino (outputfile):
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	// Fecha os fluxos de tranferencia de dados e libera os arquivos da mem�ria ap�s a realiza��o da c�pia:
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{ 
        String myPath = DB_PATH + DB_NAME;
    	// Abre o Banco de Dados:
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY); 
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		// db.setLocale(Locale.getDefault());	 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}

	// Adicione aqui os seus m�todos auxiliares p�blicos para acessar e obter conte�do do banco de dados.
    // Voc� pode retornar cursores "return myDataBase.query (....)", de modo que seria f�cil para criar "adaptadores" para suas Views.
 
}
