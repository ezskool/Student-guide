//package studentguiden.ntnu.storage;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//import studentguiden.ntnu.misc.Util;
//
//import android.content.Context;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteException;
//import android.database.sqlite.SQLiteOpenHelper;
//
//public class DatabaseHandler extends SQLiteOpenHelper {
//
//    private static final int DATABASE_VERSION = 1;
//    private static final String DATABASE_TABLE = "course";
//    private static final String DATABASE_NAME = "studentguide";
//    private static final String DATABASE_PATH = "/data/data/studentguiden.ntnu.storage/";
//    private static final String DATABASE_FULLPATH = DATABASE_PATH + DATABASE_NAME;
//    private SQLiteDatabase database;
//    private Context context;
//
//    public DatabaseHandler(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        this.context = context;
//    }
//
//    public void createDatabase() throws IOException{
//    	if(!checkDataBase()){
//    		this.getReadableDatabase();
//    		 
//        	try {
// 
//    			copyDataBase();
// 
//    		} catch (IOException e) {
// 
//        		throw new Error("Error copying database");
// 
//        	}
//    	}
//    }
//    
//    /**
//     * Check if the database already exist to avoid re-copying the file each time you open the application.
//     * @return true if it exists, false if it doesn't
//     */
//    private boolean checkDataBase(){
// 
//    	SQLiteDatabase db = null;
// 
//    	try{
//    		String path = DATABASE_PATH + DATABASE_NAME;
//    		db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
// 
//    	}catch(SQLiteException e){
//    		Util.log("database not created");
//    	}
// 
//    	if(db != null){
// 
//    		db.close();
// 
//    	}
// 
//    	return db != null ? true : false;
//    }
//    
//    private void copyDataBase() throws IOException{
//    	 
//    	//Open your local db as the input stream
//    	InputStream myInput = context.getAssets().open(DATABASE_NAME);
// 
//    	// Path to the just created empty db
//    	String outFileName = DATABASE_PATH+DATABASE_NAME;
// 
//    	//Open the empty db as the output stream
//    	OutputStream myOutput = new FileOutputStream(outFileName);
// 
//    	//transfer bytes from the inputfile to the outputfile
//    	byte[] buffer = new byte[1024];
//    	int length;
//    	while ((length = myInput.read(buffer))>0){
//    		myOutput.write(buffer, 0, length);
//    	}
// 
//    	//Close the streams
//    	myOutput.flush();
//    	myOutput.close();
//    	myInput.close();
// 
//    }
//    
//    public void openDataBase() throws SQLException{
//    	 
//    	//Open the database
//        String myPath = DB_PATH + DB_NAME;
//    	database= SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
// 
//    }
// 
//    @Override
//	public synchronized void close() {
// 
//    	    if(myDataBase != null)
//    		    myDataBase.close();
// 
//    	    super.close();
// 
//	}
//    
//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		// TODO Auto-generated method stub
//		
//	} 
//
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//
//
//}